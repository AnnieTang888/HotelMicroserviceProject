package com.synex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ViewController {
	
	 @RequestMapping(value = "/insurance-page",method = RequestMethod.GET)
	 public String home2() {
		return "insurance";
	 }
	 
}
