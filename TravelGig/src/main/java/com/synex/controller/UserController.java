package com.synex.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.synex.domain.Role;
import com.synex.domain.User;
import com.synex.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
@SessionAttributes("user")
public class UserController {
	
	@Autowired UserService userService;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/home",method = RequestMethod.GET)
	public String home() {
		return "Home";
	}

	
	@GetMapping("/fetchUser")
	public String fetchUserPage(Principal principal) {
		return "fetchUser";
	}

	@GetMapping(value = "/login")
	public String login(@RequestParam(required = false) String logout, @RequestParam(required = false) String error,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model model) {
		String message = "";
		if (error != null) {
			message = "Invalid Credentials";
		}
		if (logout != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null) {
				new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, auth);
			}
			message = "Logout";
			return "login";
		}
		model.addAttribute("Message", message);
		return "login";
		//return "Home";

	}
	
	

	@GetMapping(value = "/accessDeniedPage")
	public String accessDenied(Principal principal, Model model) {
		String message = principal.getName() + ", Unauthorised access";
		model.addAttribute("Message", message);
		return "accessDeniedPage";

	}

	
	@PostMapping(value = "/signup")
    public String signup(@RequestParam String userEmail, @RequestParam String userName, @RequestParam String password) {
		 //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		 String encodedPassword = passwordEncoder.encode(password);

	        // Create a new user entity and set all the attributes
	        User newUser = new User();
	        newUser.setUserName(userName);
	        newUser.setUserPassword(encodedPassword);
	        newUser.setEmail(userEmail);

	        // Assign a default role (e.g., "ROLE_USER")
	        //Role defaultRole = userService.findRoleByName("ROLE_USER");
	        Role defaultRole = userService.findRoleByName("USER");
	        Set<Role> roles = new HashSet<>();
	        roles.add(defaultRole);
	        newUser.setRoles(roles);

	        // Save the new user
	        userService.save(newUser);

	        // Redirect to the login page after successful signup
	        return "login";    
	}
	
	@GetMapping("/register")
	public String register() {
		return "signup";
	}
	
	@GetMapping(value = "/user/{username}")
	@ResponseBody
	public String getUserByUsername(@PathVariable String username) {
		return userService.findByUserName(username).getEmail();

	}
	
	/*
	@GetMapping("/Home")
	public String userProfile(Principal principal) {
		if(principal != null)
			return "Home";
		
		return "redirect:NotFound";
	}*/
	
	@GetMapping("/userProfile")
	public String userProfile(Principal principal) {
		if(principal != null)
			return "userProfile";
		
		return "redirect:NotFound";
	}


//	@Bean
//	public BCryptPasswordEncoder bCryptpeasswordEncoder() {
//		return new BCryptPasswordEncoder();
//	}

}
