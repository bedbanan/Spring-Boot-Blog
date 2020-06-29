package com.lalala.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.lalala.pojo.Authority;
import com.lalala.pojo.User;
import com.lalala.service.AuthorityService;
import com.lalala.service.UserService;

/*
 * 主页控制器
 */
@Controller
public class MainController {
	private static final Long ROLE_USER_AUTHORITY_ID = 2L;
    //2为普通用户
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthorityService authorityService;
     @GetMapping("/")
     public String root() {
    	 return "redirect:/index";
     }
     @GetMapping("/index")
     public String index() {
    	 return "redirect:/blogs";
    	 
     }
     /*
      * 获取登录页面
      */
     @GetMapping("/login")
     public String login() {
    	 return "login";
     }
     
     @GetMapping("/login-error")
     public String loginError(Model model) {
    	 model.addAttribute("loginError",true);
    	 model.addAttribute("errorMsg","登录失败，用户名或密码错误!");
         return "login";
     }
     
     @GetMapping("/register")
     public String register() {
    	 return "register";
     }
     
     /*
       * 注册新用户 
     * @param user
	 * @param result
	 * @param redirect
      */
     @PostMapping("/register")
     public String registerUser(User user) {
    	 List<Authority> authorities=new ArrayList<>();
 		authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID));
 		user.setAuthorities(authorities);
    	 userService.registerUser(user);
    	 return "redirect:/login";     //注册成功重定向至login页面
     }

}

