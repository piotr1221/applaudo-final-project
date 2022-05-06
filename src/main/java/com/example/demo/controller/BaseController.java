package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

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
	public Map<String, Object> ra() {
		Map<String, Object> map = new HashMap<>();
		map.put("ra", "xd");
		return map;
	}
}
