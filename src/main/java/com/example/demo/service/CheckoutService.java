package com.example.demo.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.checkout.OrderDTO;
import com.example.demo.dto.checkout.OrderDetailDTO;
import com.example.demo.dto.checkout.ShoppingCartDTO;
import com.example.demo.entity.checkout.Order;
import com.example.demo.entity.checkout.OrderDetail;
import com.example.demo.entity.checkout.ShoppingCart;
import com.example.demo.entity.checkout.ShoppingCartDetail;
import com.example.demo.entity.user.User;
import com.example.demo.exception.responsestatus.BadRequestStatusException;
import com.example.demo.exception.responsestatus.ConflictStatusException;
import com.example.demo.exception.responsestatus.NotFoundStatusException;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ShoppingCartDetailRepository;
import com.example.demo.repository.ShoppingCartRepository;
import com.example.demo.utility.EntityToDTOMap;
import com.example.demo.utility.MyModelMapper;
import com.example.demo.utility.ScopeVerifier;

@Service
public class CheckoutService {
	
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;
	
	@Autowired
	private ShoppingCartDetailRepository shoppingCartDetailRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MyModelMapper myModelMapper;
	
	@Autowired
	private ScopeVerifier scopeVerifier;
	
	public ShoppingCartDTO getCheckout(Principal principal) {
		User user = userService.getUser(principal.getName());
		checkoutNotNull(user);
		
		return getShoppingCartDTO(user);
	}
	
	public ShoppingCartDTO startCheckout(List<Map<String, Object>> checkoutDetails, Principal principal) {
		User user = userService.getUser(principal.getName());
		if(user.getShoppingCart() != null) throw new ConflictStatusException("The checkout process is already started");
		
		ShoppingCart shoppingCart = new ShoppingCart(user.getId(), createShoppingCartDetailsFromRequestBody(checkoutDetails), user);
		shoppingCart = shoppingCartRepository.save(shoppingCart);
		user.setShoppingCart(shoppingCart);
		userService.save(user);
		
		return getShoppingCartDTO(user);
	}
	
	public ShoppingCartDTO addProductToShoppingCart(List<Map<String, Object>> checkoutDetails, Principal principal) {
		User user = userService.getUser(principal.getName());
		checkoutNotNull(user);
		
		List<ShoppingCartDetail> shoppingCartDetails = createShoppingCartDetailsFromRequestBody(checkoutDetails);
		List<ShoppingCartDetail> repeatedShoppingCartDetails = shoppingCartDetails.stream()
																				.filter(user.getShoppingCart()
																							.getShoppingCartDetails()::contains)
																				.collect(Collectors.toList());
		if (!repeatedShoppingCartDetails.isEmpty()) {
			throw new ConflictStatusException("The products: "
											+ repeatedShoppingCartDetails.stream()
																		.map(e -> e.getProduct().toString())
																		.collect(Collectors.toList())
											+ " are already in the checkout");
		}

		user.getShoppingCart()
			.addShoppingCartDetails(shoppingCartDetails);
		shoppingCartRepository.save(user.getShoppingCart());
		
		return getShoppingCartDTO(user);
	}
	
	public ShoppingCartDTO updateProductQuantity(Integer productId, Map<String, Object> productQuantity, Principal principal) {
		User user = userService.getUser(principal.getName());
		checkoutNotNull(user);
		
		ShoppingCartDetail shoppingCartDetail = findShoppingCartDetailByProductId(productId, user);
		shoppingCartDetail.setQuantity( (Integer) productQuantity.get("quantity") );
		shoppingCartDetailRepository.save(shoppingCartDetail);
		
		return getShoppingCartDTO(user);
	}
	
	public ShoppingCartDTO removeProductFromShoppingCart(Integer productId, Principal principal) {
		User user = userService.getUser(principal.getName());
		checkoutNotNull(user);
		
		ShoppingCartDetail shoppingCartDetail = findShoppingCartDetailByProductId(productId, user);
		user.getShoppingCart().removeShoppingCartDetail(shoppingCartDetail);
		shoppingCartDetailRepository.delete(shoppingCartDetail);
		
		if (user.getShoppingCart().isEmpty()) {
			ShoppingCart shoppingCart = user.getShoppingCart();
			user.setShoppingCart(null);
			shoppingCartRepository.delete(shoppingCart);
			return null;
		}
		
		return getShoppingCartDTO(user);
	}
	
	public ShoppingCartDTO addShippingAddress(Map<String, Object> address, Principal principal) {
		User user = userService.getUser(principal.getName());
		checkoutNotNull(user);
		
		Long addressId = ( (Integer) address.get("addressId") ).longValue();
		user.getShoppingCart().setAddress(userService.getAddress(addressId, principal));
		shoppingCartRepository.save(user.getShoppingCart());
		
		return getShoppingCartDTO(user);
	}
	
	public ShoppingCartDTO addPaymentMethod(Map<String, Object> paymentMethod, Principal principal) {
		User user = userService.getUser(principal.getName());
		checkoutNotNull(user);
		
		Long paymentMethodId = ( (Integer) paymentMethod.get("paymentMethodId") ).longValue();
		user.getShoppingCart().setPaymentMethod(userService.getPaymentMethod(paymentMethodId, principal));
		shoppingCartRepository.save(user.getShoppingCart());
		
		return getShoppingCartDTO(user);
	}
	
	public OrderDTO createOrder(Principal principal) {
		scopeVerifier.hasScope((KeycloakAuthenticationToken) principal, "write-order");
		User user = userService.getUser(principal.getName());
		checkoutNotNull(user);
		
		List<String> exceptions = new ArrayList<>();
		
		if (user.getShoppingCart().getAddress() == null) exceptions.add("Shipping address is missing");
		
		if (user.getShoppingCart().getPaymentMethod() == null) exceptions.add("Payment method is missing");
		
		if (!exceptions.isEmpty()) throw new BadRequestStatusException(exceptions.toString());
		
		List<OrderDetail> orderDetails = user.getShoppingCart().getShoppingCartDetails().stream()
																						.map(OrderDetail::new)
																						.collect(Collectors.toList());
		orderDetails = orderDetailRepository.saveAll(orderDetails);
		Order order = new Order(user.getShoppingCart(), orderDetails);
		order = orderRepository.save(order);

		return getOrderDTO(order);
	}
	
	public List<OrderDTO> getOrders(Principal principal) {
		User user = userService.getUser(principal.getName());
		return orderRepository.findByUser(user).stream()
												.map(this::getOrderDTO)
												.collect(Collectors.toList());
	}
	
	private List<ShoppingCartDetail> createShoppingCartDetailsFromRequestBody(List<Map<String, Object>> checkoutDetails) {
		return checkoutDetails.stream()
								.map(e -> new ShoppingCartDetail(
										productService.getProduct( ((Integer) e.get("productId")).longValue() ), 
										(Integer) e.get("quantity")
									)
								).collect(Collectors.toList());
	}
	
	private ShoppingCartDetail findShoppingCartDetailByProductId(Integer productId, User user) {
		return user.getShoppingCart()
					.getShoppingCartDetails().stream()
											.filter(e -> e.getProduct().getId().equals(productId.longValue()))
											.findFirst()
											.orElseThrow();
	}
	
	private void checkoutNotNull(User user) {
		if(user.getShoppingCart() == null) throw new NotFoundStatusException("The user hasn't started the checkout process");
	}
	
	private ShoppingCartDTO getShoppingCartDTO(User user) {
		ShoppingCartDTO shoppingCartDTO = myModelMapper.map(user.getShoppingCart(), ShoppingCartDTO.class);
		
		if (user.getShoppingCart().getPaymentMethod() != null) {
			shoppingCartDTO.setPaymentMethod(myModelMapper.map(user.getShoppingCart().getPaymentMethod(), EntityToDTOMap.paymentMethods));
		}
		
		return shoppingCartDTO;
	}
	
	private OrderDTO getOrderDTO(Order order) {
		OrderDTO orderDTO = myModelMapper.map(order, OrderDTO.class);
		orderDTO.setPaymentMethod(myModelMapper.map(order.getPaymentMethod(), EntityToDTOMap.paymentMethods));
		orderDTO.setShoppingCartDetails(myModelMapper.convertAllEntitiesToDTO(order.getOrderDetails(), OrderDetailDTO.class));
		return orderDTO;
	}
	
}
