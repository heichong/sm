package com.zongb.sm.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zongb.sm.entity.User;
import com.zongb.sm.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService ;
	
	@RequestMapping("/list")
	public String getAllUser(Model model){
		System.out.println("--------getAllUser-------------") ;
		model.addAttribute("userList", userService.getAllUser()) ;
		return "user/userList" ;
	}
	
	@RequestMapping(value="/save")
	public String saveUser(@ModelAttribute("user") User user,Model model) {
		System.out.println("--------saveUser-------------"+user) ;
		if(null == user.getId() || "".equals(user.getId())){
			user.setId(UUID.randomUUID().toString()) ;
			userService.saveUser(user) ;
		}else{
			userService.updateUser(user) ;
		}
		user = null ;
		return "redirect:/user/list" ;
	}
	
	@RequestMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") String id,Model model){
		System.out.println("--------deleteUser-------------") ;
		userService.deleteUserById(id) ;
		return "redirect:/user/list" ;
	}
	
	@RequestMapping("/updateInit/{id}")
	public String updateInitUser(@PathVariable("id") String id,Model model){
		System.out.println("--------updateInitUser-------------"+ userService.getUserById(id)) ;
		model.addAttribute("user", userService.getUserById(id)) ;
		return "user/userEdit" ;
	}

	@RequestMapping("/addInit")
	public String addInitUser(Model model){
		System.out.println("--------addInitUser-------------") ;
		model.addAttribute("user", new User()) ;
		return "user/userEdit" ;
	}
	
}
