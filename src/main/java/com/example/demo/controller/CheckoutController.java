package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("checkout")
public class CheckoutController {

	@GetMapping("/")
	@ResponseBody
//	@RolesAllowed("admin")
//	@PreAuthorize("hasAuthority('SCOPE_write-order')")
	Map<String, Object> getCheckout(HttpServletRequest request){
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
	
	@PostMapping("/product")
	@ResponseBody
	void ra() {
		
	}
	
}
