package com.lalala.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/*
 * User控制器
 */
import org.springframework.web.servlet.ModelAndView;

import com.lalala.pojo.Authority;
import com.lalala.pojo.User;
import com.lalala.service.AuthorityService;
/*
 * list.html：用于展示用户列表
 * form.html：用于新增或修改用户资料
 * view.html：用于查看某个用户的具体资料
 * header.html：所有页面公用的头部页面
 * footer.html：公用的底部页面
 */
import com.lalala.service.UserService;
import com.lalala.util.ConstraintViolationExceptionHandler;
import com.lalala.vo.Response;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired //自动注入
    private UserService userService;
	
	@Autowired
	private AuthorityService authorityService;

	/**
	 * 将参数
     * 查询所有用户
     * @param async  //是否是异步请求
     * @param pageIndex  //分页首页
     * @param pageSize   //分页大小
     * @param name
     * @param model
     * @return
     * @RequestParam:将请求参数绑定到你控制器的方法参数上（是springmvc中接收普通参数的注解）
     * value：参数名,required：是否包含该参数，默认为true，表示该请求路径中必须包含该参数，如果不包含就报错。
     * defaultValue：默认参数值，如果设置了该值，required=true将失效，自动为false,如果没有传该参数，就使用默认值
     */
	@GetMapping
	public ModelAndView list(@RequestParam(value="async",required=false) boolean async,
			@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
			@RequestParam(value="name",required=false,defaultValue="") String name,Model model) {
		Pageable pageable = new PageRequest(pageIndex, pageSize);  //分页
		Page<User> page=userService.listUsersByNameLike(name, pageable); //模糊查询
		List<User> list=page.getContent(); //当前所在页面数据列表
		model.addAttribute("page",page);
		model.addAttribute("userList",list);
		return new ModelAndView(async==true?"users/list :: #mainContainerRepleace":"users/list","userModel",model);
		//async为true 只返回list界面的部分内容，false的时候返回完整的页面，第一次需要全部返回。后面使用异步更新部分数据
		
	}
	
	/*
	 * 获取创建表单页面
	 */
	@GetMapping("/add")
	public ModelAndView createForm(Model model) {
		model.addAttribute("user",new User(null,null,null,null));
		return new ModelAndView("users/add","userModel",model);
	}
	
	/*
	 * 保存或修改用户
	 */
	@PostMapping
	public ResponseEntity<Response> saveOrUpdateUser(User user,Long authorityId){
		List<Authority> authorities=new ArrayList<>();
		authorities.add(authorityService.getAuthorityById(authorityId));
		user.setAuthorities(authorities);
		try {
			userService.saveOrUpdateUser(user);
		} catch (ConstraintViolationException e) {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功",user));
	}
	
	/*
	 * 删除用户
	 */
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Response> delete(@PathVariable("id") Long id, Model model){
		try {
			userService.removeUser(id);
		} catch (ConstraintViolationException e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功"));
	}
	   /**
     * 获取修改用户的界面
     * @param id
     * @param model
     * @return
     */
    @GetMapping(value = "edit/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return new ModelAndView("users/edit", "userModel", model);
    }
}
