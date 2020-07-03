package com.cn.bjut.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.bjut.pojo.User;
import com.cn.bjut.service.IUserService;

@Controller
@RequestMapping("/user")
public class UserController {

	//第一次修改
	@Resource
	private IUserService userService;
	
	@RequestMapping("/firstPage")
	public String toIndex(HttpServletRequest request,Model model){  
        int userId = Integer.parseInt(request.getParameter("id"));  
        User user = this.userService.getUserById(userId);  
        model.addAttribute("user", user);  
        return "firstPage";  
	}
	
	
	
	
	
}
