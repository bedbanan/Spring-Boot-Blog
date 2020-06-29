package com.lalala.controller;



import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lalala.pojo.User;
import com.lalala.service.BlogService;
import com.lalala.service.VoteService;
import com.lalala.util.ConstraintViolationExceptionHandler;
/*
 * 点赞控制器
 */
import com.lalala.vo.Response;
@Controller
@RequestMapping("/votes")
public class VoteController {
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private VoteService voteService;
	
	/*
	 * 发表点赞
	 */
	@PostMapping
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  //指定权限用户才能拥有以下操作
	public ResponseEntity<Response> createVote(Long blogId){
		try {
			blogService.createVote(blogId);
		} catch (ConstraintViolationException e) {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		}catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		return ResponseEntity.ok().body(new Response(true, "点赞成功!",null));
	}
	
	/*
	 * 取消点赞
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  //指定权限用户才能拥有以下操作
	public ResponseEntity<Response> delete(@PathVariable("id") Long id,Long blogId){
		boolean isOwner=false;
		User user=voteService.getVoteById(blogId).getUser(); //通过vote的id查询到相对应的user用户
		//判断取消点赞的用户是否是点赞用户
		////getPrincipal使用security进行安全操作，获取信息，获取认证信息，然后获取准则
		//isAuthenticated()当前用户是否已通过身份验证  getAuthentication()获取认证信息
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if (principal !=null && user.getUsername().equals(principal.getUsername())) { //是操作对象
				isOwner = true;
			} 
		} 
			if(!isOwner) {
				return ResponseEntity.ok().body(new Response(false, "操作失败没有权限!"));
			}
			try {
				blogService.removeVote(blogId, id);
				voteService.removeVote(id);
			} catch (ConstraintViolationException e) {
				return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
			}catch (Exception e) {
				return ResponseEntity.ok().body(new Response(false, e.getMessage()));
			}
			return ResponseEntity.ok().body(new Response(true,"点赞取消！",null));
		
	}
 
}
