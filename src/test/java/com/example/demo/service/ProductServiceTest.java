package com.example.demo.service;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.dto.checkout.ProductDTO;
import com.example.demo.entity.checkout.Product;
import com.example.demo.repository.ProductRepository;

@SpringBootTest
@DisplayName("Test suite for ProductService")
class ProductServiceTest {

	@Autowired
	ProductService productService;
	
	@MockBean
	ProductRepository productRepository;

//	@Autowired
//	MyModelMapper myModelMapper;
	
	Product product;
	ProductDTO productDTO;
	
	@BeforeEach
	void init() {
		product = new Product();
		product.setId(1L);
		product.setName("Product");
		product.setPrice(1d);
		product.setStock(100);
		productDTO = new ProductDTO();
		productDTO.setId(product.getId());
		productDTO.setName(product.getName());
		productDTO.setPrice(product.getPrice());
		productDTO.setStock(product.getStock());
	}
	
	@Test
	@DisplayName("Get products")
	void getProducts() {
		List<Product> products = List.of(product);
		when(productRepository.findAll()).thenReturn(products);
		
		List<ProductDTO> productDtos = productService.getProducts();
		assertThat(productDtos, equalTo(List.of(productDTO)));
	}
	
	@Test
	@DisplayName("Get product")
	void getProduct() {
		when(productRepository.getById(product.getId())).thenReturn(product);
		Product o = productService.getProduct(product.getId());
		assertThat(product, equalTo(o));
	}
}
