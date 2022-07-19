package com.example.BareBoneImage.controller;

import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.BareBoneImage.model.Role;
import com.example.BareBoneImage.model.User;
import com.example.BareBoneImage.service.UserService;


@Controller
public class MainController {
	
	@Autowired
	private UserService userService;

//	@GetMapping("/")
//	public String root()
//	{
//		return "redirect:/students";
//	}
	
    @GetMapping("/")
    public String root(HttpSession session,Authentication authentication) {
        //return "index";
    	System.out.println("IN  MainController->root()");
    	System.out.println(">>>>>>>USER ="+authentication.getName());
    	User existing = userService.findByEmail(authentication.getName());
    	System.out.println("User firstName="+existing.getFirstName());
    	System.out.println("User lastName="+existing.getLastName());
    	System.out.println("User Id="+existing.getId());
    	
		System.out.println("USER ROLE="+existing.getRoles());
    	
        // IN DB: update role set name = "ROLE_SUPER" where id = 12;
      
		java.util.Collection<Role> roles = existing.getRoles();
		String userRole = roles.toString();
		System.out.println("COLLECTION USER ROLE="+userRole);
		
		if(userRole.equals("[ROLE_SUPER]")) {
			return "redirect:/admin";
		}
		
		return "redirect:/students";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
    	System.out.println("IN  MainController->admin()");
        return "admin";
    }
    
    @GetMapping("/login")
    public String login(Model model) {
    	System.out.println("IN  MainController->login()");
        return "login";
    }

    @GetMapping("/user")
    public String userIndex() {
    	System.out.println("IN  MainController->userIndex()");
        return "user/index";
    }

    @ResponseBody
	@GetMapping("/logoutSuccess")
    public String logoutResponse()
    {
    	System.out.println("IN  MainController->logoutResponse()");
        return "Logged Out!!!!";
    }

}
