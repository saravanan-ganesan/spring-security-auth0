package com.sara.security.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class LoginController {

	@GetMapping("/login")
    public String login() {
        
		return "redirect:/oauth2/authorization/auth0";
    }
}
