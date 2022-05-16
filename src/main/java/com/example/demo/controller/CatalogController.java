package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.checkout.ProductDTO;
import com.example.demo.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("catalog")
public class CatalogController {

	@Autowired
	private ProductService productService;
	
	@Operation(summary = "Get all registered products")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "All registered products",
					content = {@Content(mediaType = "application/json",
										array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class)))
							})
	})
	@GetMapping("/")
	@ResponseBody
	public ResponseEntity<List<ProductDTO>> getCatalog(){
		return ResponseEntity.ok(productService.getProducts());
	}
	
}
