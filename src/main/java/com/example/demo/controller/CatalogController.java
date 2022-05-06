package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProductDTO;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping("catalog")
public class CatalogController {

	@Autowired
	private ProductService productService;
	
	@GetMapping("/")
	@ResponseBody
	List<ProductDTO> getCatalog(){
		return productService.getProducts();
	}
	
}
