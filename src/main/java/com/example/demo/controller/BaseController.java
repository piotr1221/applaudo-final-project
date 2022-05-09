package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class BaseController {

	@GetMapping("/")
	@ResponseBody
	public Map<String, Object> ra(HttpServletRequest request) {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
		System.out.println(principal.getAccount().getKeycloakSecurityContext().getToken().getScope());
		
		Map<String, Object> map = new HashMap<>();

		System.out.println(authentication.getAuthorities());
		System.out.println(authentication.getCredentials());
		System.out.println(authentication.getPrincipal());
		System.out.println(authentication.getDetails());
		System.out.println(authentication.getName());
		return map;
	}
}
