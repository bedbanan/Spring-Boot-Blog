package com.lalala.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.lalala.pojo.Blog;
import com.lalala.pojo.Catalog;
import com.lalala.pojo.User;
import com.lalala.pojo.Vote;
import com.lalala.service.BlogService;
import com.lalala.service.CatalogService;
import com.lalala.service.UserService;
import com.lalala.util.ConstraintViolationExceptionHandler;
import com.lalala.vo.Response;
/*
 * 用户主页的控制器
 * @PathVariable URI模板指定了一个变量名为id的变量，当控制器处理请求时会将 id 替换为正确的值
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private CatalogService catalogService;
	
	@Value("${file.server.url}")
	private String fileServerUrl;
	
	/**
	 * 用户的主页
	 * @param username
	 * @param model
	 * @return
	 */
	
     @GetMapping("/{username}")
     public String userSpace(@PathVariable("username") String username,Model model) { 
    	 User user=(User)userDetailsService.loadUserByUsername(username);
    	 model.addAttribute("user",user);
    	 return "redirect:/u/" + username + "/blogs";
     }

	
     /*
      * 获取个人设置页面
      */
     @GetMapping("/{username}/profile")
     @PreAuthorize("authentication.name.equals(#username)")  //判断username是否是当前的授权用户
     public ModelAndView peofile(@PathVariable("username") String username,Model model) {
    	 User user=(User)userDetailsService.loadUserByUsername(username);
    	 model.addAttribute("user",user);
    	 model.addAttribute("fileServerUrl",fileServerUrl); //文件服务器的地址返回给客户端
    	 return new ModelAndView("/userspace/profile","userModel",model);
     }

	/**
	 * 保存个人设置
	 * @param username
	 * @param user
	 * @return
	 */
     @PostMapping("/{username}/profile")
     @PreAuthorize("authentication.name.equals(#username)")  //识别是否是本人
     public String saveProfile(@PathVariable("username") String username,User user) {
    	 User originalUser=userService.getUserById(user.getId());  //根据用户ID查询数据库原始的user的信息
    	 originalUser.setEmail(user.getEmail());    
    	 originalUser.setName(user.getName());
    	 
    	 String rawPassword=originalUser.getPassword(); //判断密码是否更改
    	 PasswordEncoder encoder=new BCryptPasswordEncoder();
    	 String encodePassword=encoder.encode(user.getPassword());
    	 boolean isMatch=encoder.matches(rawPassword, encodePassword);
    	 if(!isMatch) {
    		 originalUser.setEncodePassword(user.getPassword());
    	 }
    	 userService.saveOrUpdateUser(originalUser);
    	 return "redirect:/u/"+username+"/profile";
         
     }

     /*
      * 获取编辑用户头像的界面
      */
     @GetMapping("/{username}/avatar")
     @PreAuthorize("authentication.name.equals(#username)")
     public ModelAndView avatar(@PathVariable("username") String username,Model model) {
    	 User user=(User)userDetailsService.loadUserByUsername(username);
    	 model.addAttribute("user",user);
    	 return new ModelAndView("/userspace/avatar","userModel",model);
     }
     
     /*
      * 保存头像
      */
     @PostMapping("/{username}/avatar")
     @PreAuthorize("authentication.name.equals(#username)")
     public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username,@RequestBody User user){
    	 String avatarUrl=user.getAvatar(); //获取用户头像返回的图片的地址
    	 
    	 User originalUser=userService.getUserById(user.getId()); //获取要保存头像地址的用户Id
    	 originalUser.setAvatar(avatarUrl);//将地址保存至数据库中
    	 userService.saveOrUpdateUser(originalUser);
    	 return ResponseEntity.ok().body(new Response(true, "处理成功",avatarUrl));
     }
     /*
      * 获取用户的博客列表
      * 根据order顺序，catalogId类别，keyword关键字进行查询
      * 将三个接口统一到一个请求返回至/userspace/u
      */
 	@GetMapping("/{username}/blogs")
 	public String listBlogsByOrder(@PathVariable("username") String username,
 			@RequestParam(value="order",required=false,defaultValue="new") String order,
 			@RequestParam(value="catalog",required=false ) Long catalogId,
 			@RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,
 			@RequestParam(value="async",required=false) boolean async,
 			@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
 			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
 			Model model) {
 		
 		User  user = (User)userDetailsService.loadUserByUsername(username);
  		
 		Page<Blog> page = null;
 		
 		if (catalogId != null && catalogId > 0) { // 分类查询
 			Catalog catalog=catalogService.getCatalogById(catalogId); //查询当前分类的博客
 			Pageable pageable=new PageRequest(pageIndex, pageSize);
 			page=blogService.listBlogsByCatalog(catalog, pageable);
 			order="";
 		} else if (order.equals("hot")) { // 最热查询
 			Sort sort = new Sort(Direction.DESC,"readSize","commentSize","voteSize"); 
 			Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
 			page = blogService.listBlogsByTitleVoteAndSort(user, keyword, pageable);
 			//根据用户进行博客名称分页的模糊查询。按最热排序
 		} else if (order.equals("new")) { // 最新查询
 			Pageable pageable = new PageRequest(pageIndex, pageSize);
 			page = blogService.listBlogsByTitleVote(user, keyword, pageable);
 			//根据用户进行博客名称分页的模糊查询，按最新排序
 		}
  
 		
 		List<Blog> list = page.getContent();	// 当前所在页面数据列表
 		
 		model.addAttribute("user", user);
 		model.addAttribute("order", order);
 		model.addAttribute("catalogId", catalogId);
 		model.addAttribute("keyword", keyword);
 		model.addAttribute("page", page);
 		model.addAttribute("blogList", list);
 		return (async==true?"/userspace/u :: #mainContainerRepleace":"/userspace/u");
 		//把相关信息导入到U界面中 
 	}

     /**
      *  获取博客展示界面
      * @param username
      * @param id
      * @return
      */
     
     @GetMapping("/{username}/blogs/{id}") //查询某个id的博客返回至
     public String listBlogsByOrder(@PathVariable("username") String username,@PathVariable("id") Long id,Model model) {
    	 User principal=null;
    	 Blog blog=blogService.getBlogById(id);
    	 //每次读取，阅读量自动+1
    	 blogService.readingIncrease(id);
    	 boolean isBlogOwner=false; //判断博客是否是操作用户的
    	 if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if (principal !=null && username.equals(principal.getUsername())) {
				isBlogOwner = true;
			} 
		}
    	 //判断用户的点赞情况
    	 List<Vote> votes=blog.getVotes();  //获取当前博客用户的点赞情况
    	 Vote currentVote=null; //当前用户的点赞情况
    	 
    	 if(principal!=null) {  //判断当前用户的点赞情况有的话 如果有传入这里来判断
    		 for (Vote vote : votes) {
    			 if (vote.getUser().getUsername().equals(principal.getUsername())) {
				currentVote=vote;
				break;
    			 }
			}
    	 }
    	 model.addAttribute("currentVote", currentVote); 
    	 model.addAttribute("isBlogOwner",isBlogOwner);
    	 model.addAttribute("blogModel",blog);
    	 return "/userspace/blog";
     }
     
     /*
      * 获取新增博客的界面
      */
     @GetMapping("/{username}/blogs/edit")
     public ModelAndView createBlog(@PathVariable("username")String username,Model model) {
    	 //获取用户的列表
    	 User user =(User)userDetailsService.loadUserByUsername(username);
    	 List<Catalog> catalogs=catalogService.listCatalogs(user);
    	 
    	 model.addAttribute("catalogs",catalogs);
    	 model.addAttribute("blog",new Blog(null, null, null));
    	 model.addAttribute("fileServerUrl",fileServerUrl);//文件服务器的地址返回给客户端
    	 return new ModelAndView("/userspace/blogedit","blogModel",model);
     }
     
     /*
      * 获取博客编辑界面
      */
     @GetMapping("/{username}/blogs/edit/{id}") //进入博客页面编辑的请求
     public ModelAndView editBlog(@PathVariable("username") String username,@PathVariable("id") Long id,Model model) {
    	 //获取用户列表
    	 User user=(User)userDetailsService.loadUserByUsername(username);
    	 List<Catalog> catalogs=catalogService.listCatalogs(user);
    	 
    	 model.addAttribute("catalogs",catalogs);
    	 model.addAttribute("blog",blogService.getBlogById(id));
    	 model.addAttribute("fileServerUrl",fileServerUrl);//文件服务器的地址返回给客户端
         
    	 
    	 return new ModelAndView("/userspace/blogedit","blogModel",model);
     }
     
     /*
      * 保存博客
      */
     @PostMapping("/{username}/blogs/edit")
     @PreAuthorize("authentication.name.equals(#username)")
     public ResponseEntity<Response> saveBlog(@PathVariable("username") String username,@RequestBody Blog blog){
    	 
    	// 对 Catalog 进行空处理
 		if (blog.getCatalog().getId() == null) {
 			return ResponseEntity.ok().body(new Response(false,"未选择分类"));
 		}
 		
    	 try {
			if(blog.getId()!=null) {  //判断是修改还是新增滴,先判断是否存在
				Blog orignalBlog=blogService.getBlogById(blog.getId());
				orignalBlog.setTitle(blog.getTitle());
				orignalBlog.setContent(blog.getContent());
				orignalBlog.setSummary(blog.getSummary());
				orignalBlog.setTags(blog.getTags());
				blogService.saveBlog(orignalBlog);
			}else {  //不存在
				User user=(User)userDetailsService.loadUserByUsername(username);
				blog.setUser(user);
				blogService.saveBlog(blog);
			}
		} catch (ConstraintViolationException e) {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		}catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
    	 
    	 String redirectUrl ="/u/"+username+"/blogs/"+blog.getId();
    	 return ResponseEntity.ok().body(new Response(true, "处理成功",redirectUrl));
     }
     
     /*
      * 删除博客
      */
     @DeleteMapping("/{username}/blogs/{id}")
     @PreAuthorize("authentication.name.equals(#username)")
     public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username,@PathVariable("id") Long id){
    	 try {
			blogService.removeBlog(id);
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false,e.getMessage()));
		}
    	 String redirectUrl="/u/"+username+"/blogs";
    	 return ResponseEntity.ok().body(new Response(true, "处理成功",redirectUrl));
     }
}
