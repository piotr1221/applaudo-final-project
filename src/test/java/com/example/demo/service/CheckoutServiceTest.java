package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.dto.checkout.OrderDTO;
import com.example.demo.dto.checkout.ShoppingCartDTO;
import com.example.demo.entity.checkout.Order;
import com.example.demo.entity.checkout.OrderDetail;
import com.example.demo.entity.checkout.Product;
import com.example.demo.entity.checkout.ShoppingCart;
import com.example.demo.entity.checkout.ShoppingCartDetail;
import com.example.demo.entity.payment.BankAccount;
import com.example.demo.entity.user.Address;
import com.example.demo.entity.user.User;
import com.example.demo.exception.responsestatus.BadRequestStatusException;
import com.example.demo.exception.responsestatus.ConflictStatusException;
import com.example.demo.exception.responsestatus.NotFoundStatusException;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ShoppingCartDetailRepository;
import com.example.demo.repository.ShoppingCartRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utility.MyModelMapper;
import com.example.demo.utility.ScopeVerifier;

@SpringBootTest
@DisplayName("Test suite for CheckoutService")
class CheckoutServiceTest {

	@Autowired
	CheckoutService checkoutService;
	
	@Autowired
	private MyModelMapper myModelMapper;
	
	@MockBean
	ShoppingCartRepository shoppingCartRepository;
	
	@MockBean
	ShoppingCartDetailRepository shoppingCartDetailRepository;
	
	@MockBean
	OrderDetailRepository orderDetailRepository;
	
	@MockBean
	OrderRepository orderRepository;
	
	@Autowired
	private UserService userService;
	
	@MockBean
	private ProductService productService;
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private ScopeVerifier scopeVerifier;
	
	@MockBean
	private KeycloakAuthenticationToken principal;
	
	User user;
	ShoppingCart shoppingCart;
	ShoppingCartDetail shoppingCartDetail;
	List<ShoppingCartDetail> shoppingCartDetails;
	Product product;
	
	@BeforeEach
	void init() {
		user = new User();
		user.setId(1L);
		user.setUsername("edwin@gmail.com");
		user.setPassword("123");
		user.setFullName("Edwin Aguilar");
		product = new Product();
		product.setId(1L);
		product.setName("Product");
		product.setPrice(5d);
		product.setStock(100);
		shoppingCartDetail = new ShoppingCartDetail();
		shoppingCartDetail.setId(1L);
		shoppingCartDetail.setProduct(product);
		shoppingCartDetail.setQuantity(2);
		shoppingCartDetail.setSubtotal(product.getPrice() * shoppingCartDetail.getQuantity());
		shoppingCart = new ShoppingCart();
		shoppingCart.setId(user.getId());
		shoppingCartDetails = new ArrayList<>();
		shoppingCartDetails.add(shoppingCartDetail);
		shoppingCart.setShoppingCartDetails(shoppingCartDetails);
		shoppingCartDetail.setShoppingCart(shoppingCart);
		user.setShoppingCart(shoppingCart);
	}

	@Test
	@DisplayName("Get checkout")
	void getCheckout() {
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		
		ShoppingCartDTO otherShoppingCart = checkoutService.getCheckout(principal);
		ShoppingCartDTO shoppingCart = checkoutService.getShoppingCartDTO(user);
		
		assertThat(shoppingCart, equalTo(otherShoppingCart));
	}
	
	@Test
	@DisplayName("Get null checkout")
	void getNullCheckout() {
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		user.setShoppingCart(null);
		
		assertThrows(NotFoundStatusException.class, () -> checkoutService.getCheckout(principal));
	}
	
	@Test
	@DisplayName("Start checkout")
	void startCheckout() {
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		doReturn(shoppingCart).when(shoppingCartRepository).save(any(ShoppingCart.class));
		doReturn(user).when(userRepository).save(any(User.class));
		doReturn(product).when(productService).getProduct(anyLong());
		user.setShoppingCart(null);
		
		Map<String, Object> map = new HashMap<>();
		map.put("productId", 1);
		map.put("quantity", 1);
		
		ShoppingCartDTO otherShoppingCart = checkoutService.startCheckout(List.of(map), principal);
		user.setShoppingCart(shoppingCart);
		ShoppingCartDTO shoppingCart = checkoutService.getShoppingCartDTO(user);

		assertThat(shoppingCart, equalTo(otherShoppingCart));
	}
	
	@Test
	@DisplayName("Try to start an already started checkout")
	void tryStartCheckout() {
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		doReturn(shoppingCart).when(shoppingCartRepository).save(any(ShoppingCart.class));
		doReturn(user).when(userRepository).save(any(User.class));
		doReturn(product).when(productService).getProduct(anyLong());
		
		Map<String, Object> map = new HashMap<>();
		map.put("productId", 1);
		map.put("quantity", 1);
		
		List<Map<String, Object>> products = List.of(map);
		
		assertThrows(ConflictStatusException.class, () -> checkoutService.startCheckout(products, principal));
	}
	
	@Test
	@DisplayName("Add product to checkout")
	void addProductToCheckout() {
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		
		Product otherProduct = new Product();
		otherProduct.setId(2L);
		otherProduct.setName("Other product");
		otherProduct.setPrice(4d);
		otherProduct.setStock(100);
		
		doReturn(otherProduct).when(productService).getProduct(anyLong());
		doReturn(shoppingCart).when(shoppingCartRepository).save(any(ShoppingCart.class));
		
		Map<String, Object> map = new HashMap<>();
		map.put("productId", 1);
		map.put("quantity", 1);
		
		ShoppingCartDTO otherShoppingCart = checkoutService.addProductToShoppingCart(List.of(map), principal);
		ShoppingCartDTO shoppingCart = checkoutService.getShoppingCartDTO(user);
		
		assertThat(shoppingCart, equalTo(otherShoppingCart));
	}
	
	@Test
	@DisplayName("Add product already in checkout")
	void addDuplicatedProductToCheckout() {
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		doReturn(product).when(productService).getProduct(anyLong());
		doReturn(shoppingCart).when(shoppingCartRepository).save(any(ShoppingCart.class));
		
		Map<String, Object> map = new HashMap<>();
		map.put("productId", 1);
		map.put("quantity", 1);
		List<Map<String, Object>> repeatedProduct = List.of(map);
		
		assertThrows(ConflictStatusException.class, () -> checkoutService.addProductToShoppingCart(repeatedProduct, principal));
	}
	
	@Test
	@DisplayName("Update product in checkout")
	void updateProduct() {
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		
		Map<String, Object> map = new HashMap<>();
		map.put("productId", 1);
		map.put("quantity", 10);
		
		ShoppingCartDTO otherShoppingCart = checkoutService.updateProductQuantity(1, map, principal);
		ShoppingCartDTO shoppingCart = checkoutService.getShoppingCartDTO(user);
		
		assertThat(shoppingCart, equalTo(otherShoppingCart));
	}
	
	@Test
	@DisplayName("Remove only product in checkout")
	void removeOnlyProduct() {
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		doNothing().when(shoppingCartDetailRepository).delete(any(ShoppingCartDetail.class));
		doNothing().when(shoppingCartRepository).delete(any(ShoppingCart.class));
		
		ShoppingCartDTO otherShoppingCart = checkoutService.removeProductFromShoppingCart(1, principal);
		
		assertThat(otherShoppingCart, equalTo(null));
	}
	
	@Test
	@DisplayName("Remove product in checkout")
	void removeProduct() {
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		doNothing().when(shoppingCartDetailRepository).delete(any(ShoppingCartDetail.class));
		doNothing().when(shoppingCartRepository).delete(any(ShoppingCart.class));
		
		Product product = new Product();
		product.setId(2L);
		product.setName("Other product");
		product.setPrice(5d);
		product.setStock(100);
		ShoppingCartDetail shoppingCartDetail = new ShoppingCartDetail();
		shoppingCartDetail.setId(1L);
		shoppingCartDetail.setProduct(product);
		shoppingCartDetail.setQuantity(2);
		user.getShoppingCart().addShoppingCartDetails(List.of(shoppingCartDetail));
		ShoppingCartDTO otherShoppingCart = checkoutService.removeProductFromShoppingCart(1, principal);
		ShoppingCartDTO shoppingCart = checkoutService.getShoppingCartDTO(user);
		
		assertThat(shoppingCart, equalTo(otherShoppingCart));
	}
	
	@Test
	@DisplayName("Add address to checkout") 
	void addAddress() {
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		
		Map<String, Object> map = new HashMap<>();
		map.put("addressId", 1);
		Address address = new Address();
		address.setAddress("Address");
		address.setId(1L);
		user.setAddresses(List.of(address));
		
		ShoppingCartDTO otherShoppingCart = checkoutService.addShippingAddress(map, principal);
		ShoppingCartDTO shoppingCart = checkoutService.getShoppingCartDTO(user);
		
		assertThat(shoppingCart, equalTo(otherShoppingCart));
	}
	
	@Test
	@DisplayName("Add payment method to checkout")
	void addPaymentMethod() {
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		
		Map<String, Object> map = new HashMap<>();
		map.put("paymentMethodId", 1);
		BankAccount paymentMethod = new BankAccount();
		paymentMethod.setAccountNumber("123 456789");
		paymentMethod.setFunds(1000d);
		paymentMethod.setId(1L);
		user.setPaymentMethods(List.of(paymentMethod));
		
		ShoppingCartDTO otherShoppingCart = checkoutService.addPaymentMethod(map, principal);
		ShoppingCartDTO shoppingCart = checkoutService.getShoppingCartDTO(user);
		
		assertThat(shoppingCart, equalTo(otherShoppingCart));
	}
	
	@Test
	@DisplayName("Create order")
	void createOrder() {
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		
		BankAccount paymentMethod = new BankAccount();
		paymentMethod.setAccountNumber("123 456789");
		paymentMethod.setFunds(1000d);
		paymentMethod.setId(1L);
		Address address = new Address();
		address.setAddress("Address");
		address.setId(1L);
		shoppingCart.setPaymentMethod(paymentMethod);
		shoppingCart.setAddress(address);
		
		List<OrderDetail> orderDetails = shoppingCartDetails.stream()
															.map(OrderDetail::new)
															.collect(Collectors.toList());
		
		doReturn(orderDetails).when(orderDetailRepository).saveAll(anyList());
		doAnswer(returnsFirstArg()).when(orderRepository).save(any(Order.class));
		doNothing().when(shoppingCartRepository).delete(any(ShoppingCart.class));
		
		OrderDTO otherOrder = checkoutService.createOrder(principal);
		OrderDTO order = checkoutService.getOrderDTO(new Order(shoppingCart, orderDetails));
		
		assertThat(order, equalTo(otherOrder));
	}
	
	@Test
	@DisplayName("Create order")
	void createOrderWithoutAddress() {
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		
		BankAccount paymentMethod = new BankAccount();
		paymentMethod.setAccountNumber("123 456789");
		paymentMethod.setFunds(1000d);
		paymentMethod.setId(1L);
		shoppingCart.setPaymentMethod(paymentMethod);
		
		assertThrows(BadRequestStatusException.class, () -> checkoutService.createOrder(principal));
	}
	
	@Test
	@DisplayName("Create order")
	void createOrderWithoutPayment() {
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		
		Address address = new Address();
		address.setAddress("Address");
		address.setId(1L);
		shoppingCart.setAddress(address);
		
		assertThrows(BadRequestStatusException.class, () -> checkoutService.createOrder(principal));
	}
	
	@Test
	@DisplayName("Get orders")
	void getOrders() {
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		
		BankAccount paymentMethod = new BankAccount();
		paymentMethod.setAccountNumber("123 456789");
		paymentMethod.setFunds(1000d);
		paymentMethod.setId(1L);
		Address address = new Address();
		address.setAddress("Address");
		address.setId(1L);
		shoppingCart.setPaymentMethod(paymentMethod);
		shoppingCart.setAddress(address);
		
		List<OrderDetail> orderDetails = shoppingCartDetails.stream()
															.map(OrderDetail::new)
															.collect(Collectors.toList());
		List<Order> orders = List.of(new Order(shoppingCart, orderDetails));
		
		doReturn(orders).when(orderRepository).findByUser(any(User.class));
		
		List<OrderDTO> otherOrders = checkoutService.getOrders(principal);
		OrderDTO orderDTO = checkoutService.getOrderDTO(new Order(shoppingCart, orderDetails));

		assertThat(List.of(orderDTO), equalTo(otherOrders));
	}
		
}
