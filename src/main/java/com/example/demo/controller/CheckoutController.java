package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

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
	Map<String, Object> getCheckout(){
		Map<String, Object> map = new HashMap<>();
		map.put("ra", "xd");
		return map;
	}
	
	@PostMapping("/product")
	@ResponseBody
	void ra() {
		
	}
	
}
