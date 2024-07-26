/*package com.synex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GatewayController {
	
	@RequestMapping(value = "/Home", method = RequestMethod.GET)
	public String home() {
		return "Home";
	}

}*/
package com.synex.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {
	
	@RequestMapping(value = "/test",method = RequestMethod.GET)
	public String test() {
		return "test";
	}
	
	@RequestMapping(value = "/welcome",method = RequestMethod.GET)
	public String welcome(Principal principle) {
		System.out.println("Welcome Mr..................."+principle.getName());
		return "welcome";
	}
	
	
}
