package com.techchallenge.infrastructure.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.application.gateways.CustomerGateway;
import com.techchallenge.application.gateways.OrderGateway;
import com.techchallenge.application.gateways.ProductGateway;
import com.techchallenge.application.usecases.OrderUseCase;
import com.techchallenge.application.usecases.interactor.OrderUseCaseInteractor;
import com.techchallenge.config.JsonUtils;
import com.techchallenge.config.infra.ExceptionHandlerConfig;
import com.techchallenge.config.infra.ObjectMapperConfig;
import com.techchallenge.config.infra.Result;
import com.techchallenge.domain.entity.Customer;
import com.techchallenge.domain.entity.Order;
import com.techchallenge.domain.entity.Product;
import com.techchallenge.infrastructure.api.mapper.CustomerMapper;
import com.techchallenge.infrastructure.api.mapper.OrderMapper;
import com.techchallenge.infrastructure.api.mapper.ProductMapper;
import com.techchallenge.infrastructure.api.request.CustomerResponse;
import com.techchallenge.infrastructure.api.request.OrderRequest;
import com.techchallenge.infrastructure.api.request.OrderResponse;
import com.techchallenge.infrastructure.api.request.ProductResponse;
import com.techchallenge.infrastructure.api.request.UpdateProductRequest;
import com.techchallenge.infrastructure.gateways.CustomerRepositoryGateway;
import com.techchallenge.infrastructure.gateways.OrderRepositoryGateway;
import com.techchallenge.infrastructure.gateways.ProductRepositoryGateway;
import com.techchallenge.infrastructure.persistence.entity.CustomerEntity;
import com.techchallenge.infrastructure.persistence.entity.OrderEntity;
import com.techchallenge.infrastructure.persistence.entity.ProductEntity;
import com.techchallenge.infrastructure.persistence.mapper.CustomerEntityMapper;
import com.techchallenge.infrastructure.persistence.mapper.OrderEntityMapper;
import com.techchallenge.infrastructure.persistence.mapper.ProductEntityMapper;
import com.techchallenge.infrastructure.persistence.repository.CustomerRepository;
import com.techchallenge.infrastructure.persistence.repository.OrderRepository;
import com.techchallenge.infrastructure.persistence.repository.ProductRepository;

@ExtendWith(SpringExtension.class)
class OrderApiTest {
	// order
	@MockBean
	private OrderRepository orderRepository;
	private OrderApi orderApi;
	private OrderUseCase orderUseCase;
	
	private OrderMapper orderMapper;
	private OrderEntityMapper orderEntityMapper;
	
	private OrderGateway orderGateway;
	private CustomerGateway customerGateway;
	private ProductGateway productGateway;	
	

	// product
	@MockBean
	private ProductRepository productRepository;
	private ProductEntityMapper productEntityMapper;
	private ProductMapper productMapper;
	
	// customer
	@MockBean
	private CustomerRepository customerRepository;
	private CustomerEntityMapper customerEntityMapper;
	
	private CustomerMapper customerMapper;
	
	private JsonUtils jsonUtils;
	private MockMvc mockMvc;

	@BeforeEach
	public void init() {
		ObjectMapper objectMapper = new ObjectMapperConfig().objectMapper();
		jsonUtils = new JsonUtils(objectMapper);

		customerMapper = new CustomerMapper();
		productMapper = new ProductMapper();
		orderMapper = new OrderMapper(customerMapper, productMapper);

		customerEntityMapper = new CustomerEntityMapper();
		productEntityMapper = new ProductEntityMapper();
		orderEntityMapper = new OrderEntityMapper(customerEntityMapper, productEntityMapper);
		
		orderGateway = new OrderRepositoryGateway(orderRepository, orderEntityMapper);
		customerGateway = new CustomerRepositoryGateway(customerRepository, customerEntityMapper);
		productGateway = new ProductRepositoryGateway(productRepository, productEntityMapper);
		
		orderUseCase = new OrderUseCaseInteractor(orderGateway, customerGateway, productGateway);
		
		orderApi = new OrderApi(orderUseCase, orderMapper);

		mockMvc = MockMvcBuilders.standaloneSetup(orderApi).setControllerAdvice(new ExceptionHandlerConfig()).build();

	}

	@Test
	public void testInsertOrderValidateNull() throws Exception {
		OrderRequest request = new OrderRequest("", null);

		Optional<String> jsonRequest = jsonUtils.toJson(request);

		MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders/checkout").contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest.orElse(""))).andExpect(status().isBadRequest()).andReturn();

		Optional<Result<OrderResponse>> response = jsonUtils.parse(mvcResult.getResponse().getContentAsString(),
				new TypeReference<Result<OrderResponse>>() {
				});

		int code = response.get().getCode();
		assertEquals(400, code, "Must Be Equals");
		assertEquals(List.of("Products is required!"), response.get().getErrors(), "Must Be Equals");
	}


	@Test
	public void testInsertOrderWithoutCustumer() throws Exception {
		List<String> productsIds = List.of("1L");
		OrderRequest request = new OrderRequest("", productsIds);

		when(customerRepository.findByCpf("02974127010")).thenReturn(Optional.empty());
		ProductEntity entity = getProductEntity("1L", "LANCHE", "X Salada");
		List<ProductEntity> entities = List.of(entity);
		when(productRepository.findByIdIn(productsIds)).thenReturn(entities);

		List<Product> products = productEntityMapper.toProductList(entities);
		Order startOrder = new Order().startOrder(null, products);

		OrderEntity orderEntity = orderEntityMapper.toOrderEntity(startOrder);

		when(orderRepository.save(any())).thenReturn(orderEntity);

		Optional<String> jsonRequest = jsonUtils.toJson(request);

		MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders/checkout").contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest.orElse(""))).andExpect(status().isCreated()).andReturn();

		Optional<Result<OrderResponse>> response = jsonUtils.parse(mvcResult.getResponse().getContentAsString(),
				new TypeReference<Result<OrderResponse>>() {
				});

		int code = response.get().getCode();
		CustomerResponse custumer = response.get().getResult().custumer();
		List<ProductResponse> productsResponse = response.get().getResult().products();
		LocalDateTime dateOrderInit = response.get().getResult().dateOrderInit();
		String statuOrder = response.get().getResult().statuOrder();
		LocalDateTime dateOrderFinish = response.get().getResult().dateOrderFinish();

		assertEquals(201, code, "Must Be Equals");

		assertNull(custumer, "Must Be Null");
		assertEquals(1, productsResponse.size(), "Must Be 1 product");
		assertEquals("X Salada", productsResponse.get(0).title(), "Must Be Equals");
		assertNotNull(dateOrderInit, "Must Be not null");
		assertEquals("RECEBIDO", statuOrder, "Must Be Equals");
		assertNull(dateOrderFinish, "Must Be Null");
	}

	@Test
	public void testInsertOrderWithCustumer() throws Exception {
		List<String> productsIds = List.of("1L");
		OrderRequest request = new OrderRequest("02974127010", productsIds);

		Customer customer = new Customer("02974127010", "Doug Funnt", "doug@emai.com");
		CustomerEntity customerEntity = customerEntityMapper.toCustomerEntity(customer);
		when(customerRepository.findByCpf("02974127010")).thenReturn(Optional.of(customerEntity));

		ProductEntity entity = getProductEntity("1L", "LANCHE", "X Salada");
		List<ProductEntity> entities = List.of(entity);
		when(productRepository.findByIdIn(productsIds)).thenReturn(entities);

		List<Product> products = productEntityMapper.toProductList(entities);

		Order startOrder = new Order().startOrder(customer, products);

		OrderEntity orderEntity = orderEntityMapper.toOrderEntity(startOrder);

		when(orderRepository.save(any())).thenReturn(orderEntity);

		Optional<String> jsonRequest = jsonUtils.toJson(request);

		MvcResult mvcResult = mockMvc.perform(post("/api/v1/orders/checkout").contentType(MediaType.APPLICATION_JSON)
				.content(jsonRequest.orElse(""))).andExpect(status().isCreated()).andReturn();

		Optional<Result<OrderResponse>> response = jsonUtils.parse(mvcResult.getResponse().getContentAsString(),
				new TypeReference<Result<OrderResponse>>() {
				});

		int code = response.get().getCode();
		CustomerResponse custumer = response.get().getResult().custumer();
		List<ProductResponse> productsResponse = response.get().getResult().products();
		LocalDateTime dateOrderInit = response.get().getResult().dateOrderInit();
		String statuOrder = response.get().getResult().statuOrder();
		LocalDateTime dateOrderFinish = response.get().getResult().dateOrderFinish();

		assertEquals(201, code, "Must Be Equals");

		assertEquals("029.741.270-10", custumer.cpf(), "Must Be Equalst Be 1 product");
		assertEquals("X Salada", productsResponse.get(0).title(), "Must Be Equals");
		assertNotNull(dateOrderInit, "Must Be not null");
		assertEquals("RECEBIDO", statuOrder, "Must Be Equals");
		assertNull(dateOrderFinish, "Must Be Null");
	}

	
	private ProductEntity getProductEntity(String id, String status, String title) {
		UpdateProductRequest request3 = new UpdateProductRequest(id, title, status, "com bacon", new BigDecimal("8.0"),
				"");
		Product product = productMapper.toProduct(request3);
		ProductEntity entity = productEntityMapper.toProductEntity(product);
		return entity;
	}

	

}
