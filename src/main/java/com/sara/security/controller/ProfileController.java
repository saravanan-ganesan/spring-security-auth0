package com.sara.security.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ProfileController {

    
    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal OidcUser oidcUser) {
    	
        model.addAttribute("profile", oidcUser.getClaims());
        model.addAttribute("email", oidcUser.getAttribute("email"));
		model.addAttribute("userId", oidcUser.getAttribute("sub"));
		model.addAttribute("given_name", oidcUser.getAttribute("name"));
		Instant authTime = oidcUser.getAttribute("iat");
		LocalDateTime datetime = LocalDateTime.ofInstant(authTime, ZoneOffset.systemDefault());
		model.addAttribute("loginDtTime", datetime.toString());
        
        
        return "profile";
    }

}
