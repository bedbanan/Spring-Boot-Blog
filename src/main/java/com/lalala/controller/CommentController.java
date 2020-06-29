package com.lalala.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lalala.pojo.Blog;
import com.lalala.pojo.Comment;
import com.lalala.pojo.User;
import com.lalala.service.BlogService;
import com.lalala.service.CommentService;
import com.lalala.util.ConstraintViolationExceptionHandler;
import com.lalala.vo.Response;

/**
 * comment控制器
 * @author hasee
 *
 */

@Controller
@RequestMapping("/comments")
public class CommentController {
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private CommentService commentService;
	
	/*
	 * 获取评论列表
	 */
	@GetMapping
	public String listComments(@RequestParam(value="blogId",required=true)Long blogId,Model model) {
		Blog blog=blogService.getBlogById(blogId);
		List<Comment> comments=blog.getComments();
		
		//判断下操作用户是否是评论的所有者,如果是提供删除功能
		//isAuthenticated()当前用户是否已通过身份验证  getAuthentication()获取认证信息
		String commentOwner="";
		if(SecurityContextHolder.getContext().getAuthentication()!=null&&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&&!SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User principal=(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();//使用security进行安全操作，获取信息，获取认证信息，然后获取准则
			if(principal!=null) {  //如果非空
				commentOwner=principal.getUsername();
			}
		}
		model.addAttribute("commentOwner",commentOwner);
		model.addAttribute("comments",comments);
		return "/userspace/blog::#mainContainerRepleace";
		
	}
	
	/**
	 * 发表评论
	 */
	@PostMapping
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER'") //指定角色的权限 才能发表评论
	public ResponseEntity<Response> createComment(Long blogId,String commentContent){
		try {
			blogService.createComment(blogId, commentContent);  //创建评论
		} catch (ConstraintViolationException e) {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
			
		}catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功",null));
	}
	
	/**
	 * 删除评论
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER'")//指定角色的权限 才能删除评论
	public ResponseEntity<Response> delete(@PathVariable("id") Long id,Long blogId){
		boolean isOwner=false;
		User user=commentService.getCommentById(id).getUser();
		
		//判断下操作用户是否是评论的所有者,如果是提供删除功能
		//isAuthenticated()当前用户是否已通过身份验证  getAuthentication()获取认证信息
		if(SecurityContextHolder.getContext().getAuthentication()!=null&&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&&!SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User principal=(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();//使用security进行安全操作，获取信息，获取认证信息，然后获取准则
			if(principal!=null && user.getUsername().equals(principal.getUsername())) {  //如果非空
				isOwner=true;
			}
		}
		if(!isOwner) {
			return ResponseEntity.ok().body(new Response(false, "没有操作权限!"));
		}
		
		try {
			blogService.removeComment(blogId,id);
			commentService.removeComment(id);
			
		} catch (ConstraintViolationException e) {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		}catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功",null));
	}

}
