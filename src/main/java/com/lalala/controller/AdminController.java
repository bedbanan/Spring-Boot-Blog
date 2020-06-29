package com.lalala.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/*
 * 后台管理的控制器
 */
import org.springframework.web.servlet.ModelAndView;

import com.lalala.vo.Menu;
@Controller
@RequestMapping("/admins")
public class AdminController {
       /*
        * 获取后台管理主页面
        */
	@GetMapping
	public ModelAndView listUsers(Model model) {   //把管理员返回至管理页面
		List<Menu> list = new ArrayList<>();
		list.add(new Menu("用户管理", "/users"));
		model.addAttribute("list", list);
		return new ModelAndView("/admins/index","model",model);
	}
}
