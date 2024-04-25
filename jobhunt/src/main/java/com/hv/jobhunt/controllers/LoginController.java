package com.hv.jobhunt.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hv.jobhunt.Models.Login;

@CrossOrigin
@RestController
public class LoginController {
	
	@PostMapping(value = "/logincred")
	public @ResponseBody String logincredentials(@RequestBody Login login) {
		System.out.println("Hello");
		System.out.println(login.getEmail());
		System.out.println(login.getPassword());
		return "success";
	}
	
	

}
