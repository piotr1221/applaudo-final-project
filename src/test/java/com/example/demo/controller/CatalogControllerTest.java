package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.dto.checkout.ProductDTO;
import com.example.demo.service.ProductService;

@WebMvcTest(CatalogController.class)
@AutoConfigureMockMvc(addFilters = false)
//@ActiveProfiles("keycloak")
@ComponentScan(basePackageClasses = { KeycloakSecurityComponents.class, KeycloakSpringBootConfigResolver.class })
@DisplayName("Test suite for CatalogController")
class CatalogControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ProductService productService;
	
	ProductDTO productDTO;
	
	@BeforeEach
	void init() {
		productDTO = new ProductDTO();
		productDTO.setId(1L);
		productDTO.setName("Test product");
		productDTO.setPrice(10d);
		productDTO.setStock(100);
	}

	@Nested
	class Catalog {
		
		@Test
		@DisplayName("Get catalog")
		void getCatalog() throws Exception {
			
			List<ProductDTO> products = new ArrayList<>();
			products.add(productDTO);
			when(productService.getProducts()).thenReturn(products);

			mvc.perform(get("/catalog/").contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id").value(productDTO.getId()))
				.andExpect(jsonPath("$[0].name").value(productDTO.getName()))
				.andExpect(jsonPath("$[0].price").value(productDTO.getPrice()))
				.andExpect(jsonPath("$[0].stock").value(productDTO.getStock()))
				.andExpect(jsonPath("$", hasSize(products.size())))
				.andExpect(status().isOk());
		}
	}
}
