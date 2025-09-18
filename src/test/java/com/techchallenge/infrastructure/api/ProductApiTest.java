package com.techchallenge.infrastructure.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.techchallenge.application.gateways.ProductGateway;
import com.techchallenge.application.usecases.ProductUseCase;
import com.techchallenge.application.usecases.interactor.ProductUseCaseInteractor;
import com.techchallenge.config.JsonUtils;
import com.techchallenge.config.infra.ExceptionHandlerConfig;
import com.techchallenge.config.infra.Result;
import com.techchallenge.domain.entity.Product;
import com.techchallenge.infrastructure.api.mapper.ProductMapper;
import com.techchallenge.infrastructure.api.request.InsertProductRequest;
import com.techchallenge.infrastructure.api.request.ProductResponse;
import com.techchallenge.infrastructure.api.request.UpdateProductRequest;
import com.techchallenge.infrastructure.gateways.ProductRepositoryGateway;
import com.techchallenge.infrastructure.persistence.entity.ProductEntity;
import com.techchallenge.infrastructure.persistence.mapper.ProductEntityMapper;
import com.techchallenge.infrastructure.persistence.repository.ProductRepository;

@ExtendWith(SpringExtension.class)
class ProductApiTest {

	private MockMvc mockMvc;
	private ProductApi productApi;	
	private ProductUseCase productUseCase;
	private ProductGateway productGateway;
	
	private ProductMapper mapper;	
	private JsonUtils jsonUtils;
	
	@MockBean
	private ProductRepository repository;
	private ProductEntityMapper entityMapper;
	
	@BeforeEach
	public void init() {		
		ObjectMapper objectMapper = new ObjectMapper();	
		jsonUtils = new JsonUtils(objectMapper);
		mapper = new ProductMapper();
		entityMapper = new ProductEntityMapper();
		
		productGateway = new ProductRepositoryGateway(repository, entityMapper);
		productUseCase = new ProductUseCaseInteractor(productGateway);
		
		productApi = new ProductApi(productUseCase, mapper);
		mockMvc = MockMvcBuilders.standaloneSetup(productApi)
				.setControllerAdvice(new ExceptionHandlerConfig()).build();
		
	}
	
	@Test
	public void testInsertProductFieldsInvalidFields() throws Exception {
		InsertProductRequest request = new InsertProductRequest("", "", "", new BigDecimal("0"), "");
		String jsonRequest = jsonUtils.toJson(request).orElse("");
		
		MvcResult mvcResult = mockMvc.perform(post("/api/v1/products").contentType(MediaType.APPLICATION_JSON)
					.content(jsonRequest))
					.andExpect(status().isBadRequest()).andReturn();
		
		Optional<Result<ProductResponse>> response = jsonUtils.parse(mvcResult.getResponse().getContentAsString(), 
				new TypeReference<Result<ProductResponse>>(){});
		int code = response.get().getCode();		
	
		assertEquals(400, code, "Must Be Equals");
		assertEquals(List.of("Category is required!", "Description is required!", "Price can't be zero", "Title is required!"), 
				response.get().getErrors(), "Must Be Equals");	
		
		
	}
	
	@Test
	public void testInsertProductFieldsInvalidCategory() throws Exception {
		InsertProductRequest request = new InsertProductRequest("X Salada", "SANDUICHE","Carne com Alface e pao" , new BigDecimal("10.0"), "");
		String jsonRequest = jsonUtils.toJson(request).orElse("");
		
		MvcResult mvcResult = mockMvc.perform(post("/api/v1/products").contentType(MediaType.APPLICATION_JSON)
					.content(jsonRequest))
					.andExpect(status().isBadRequest()).andReturn();
		
		Optional<Result<ProductResponse>> response = jsonUtils.parse(mvcResult.getResponse().getContentAsString(), 
				new TypeReference<Result<ProductResponse>>(){});
		int code = response.get().getCode();		
	
		assertEquals(400, code, "Must Be Equals");
		assertEquals(List.of("Invalid Category!"), 
				response.get().getErrors(), "Must Be Equals");	
		
		
	}
	
	@Test
	public void testInsertProductFieldsValid() throws Exception {
		InsertProductRequest request = new InsertProductRequest("X Salada", "LANCHE","Carne com Alface e pao" , new BigDecimal("10.0"), "");
		String jsonRequest = jsonUtils.toJson(request).orElse("");
		Product product = mapper.toProduct(request);
		ProductEntity productEntity = entityMapper.toProductEntity(product);
		
		when(repository.save(productEntity)).thenReturn(productEntity);
		
		MvcResult mvcResult = mockMvc.perform(post("/api/v1/products").contentType(MediaType.APPLICATION_JSON)
					.content(jsonRequest))
					.andExpect(status().isCreated()).andReturn();
		
		Optional<Result<ProductResponse>> response = jsonUtils.parse(mvcResult.getResponse().getContentAsString(), 
				new TypeReference<Result<ProductResponse>>(){})
				;
		int code = response.get().getCode();
		String category = response.get().getResult().category();
		String description = response.get().getResult().description();
		String title = response.get().getResult().title();
		BigDecimal price = response.get().getResult().price();
		
		assertEquals(201, code, "Must Be Equals");
		
		assertEquals("LANCHE", category, "Must Be Equals");	
		assertEquals("Carne com Alface e pao", description, "Must Be Equals");	
		assertEquals("X Salada", title, "Must Be Equals");	
		assertEquals(new BigDecimal("10.0"), price, "Must Be Equals");		
		
		
	}
	
	@Test
	public void testUpdateProductFieldsValid() throws Exception {
		UpdateProductRequest request = new UpdateProductRequest(1L, "X Salada", "LANCHE","Carne com Alface e pao" , new BigDecimal("10.0"), "");
		String jsonRequest = jsonUtils.toJson(request).orElse("");
		Product product = mapper.toProduct(request);
		ProductEntity entity = entityMapper.toProductEntity(product);
		
		when(repository.save(entity)).thenReturn(entity);
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		MvcResult mvcResult = mockMvc.perform(put("/api/v1/products").contentType(MediaType.APPLICATION_JSON)
					.content(jsonRequest))
					.andExpect(status().isOk()).andReturn();
		
		Optional<Result<ProductResponse>> response = jsonUtils.parse(mvcResult.getResponse().getContentAsString(), 
				new TypeReference<Result<ProductResponse>>(){});
		
		int code = response.get().getCode();
		String category = response.get().getResult().category();
		String description = response.get().getResult().description();
		String title = response.get().getResult().title();
		BigDecimal price = response.get().getResult().price();
		
		assertEquals(200, code, "Must Be Equals");
		
		assertEquals("LANCHE", category, "Must Be Equals");	
		assertEquals("Carne com Alface e pao", description, "Must Be Equals");	
		assertEquals("X Salada", title, "Must Be Equals");	
		assertEquals(new BigDecimal("10.0"), price, "Must Be Equals");	
	}
	
	@Test
	public void testFindProductByCategoryLANCHE() throws Exception {
		UpdateProductRequest request1 = new UpdateProductRequest(1L, "X Salada", "LANCHE", "Carne com Alface e pao" , new BigDecimal("10.0"), "");
		UpdateProductRequest request2 = new UpdateProductRequest(2L, "Coca Cola", "BEBIDA", "Gelada" , new BigDecimal("15.0"), "");
		UpdateProductRequest request3 = new UpdateProductRequest(3L, "Batata", "ACOMPANHAMENTO", "com bacon" , new BigDecimal("8.0"), "");
		UpdateProductRequest request4 = new UpdateProductRequest(4L, "Bolo", "SOBREMESA", "chocolate com creme" , new BigDecimal("20.0"), "");
		List<UpdateProductRequest> products = List.of(request1, request2, request3, request4);
		
		List<Product> toProducts = products.stream().map( product -> mapper.toProduct(product)).collect(Collectors.toList());
		List<ProductEntity> entities = entityMapper.toProductEntityList(toProducts);
		ProductEntity entity = entities.stream().filter( ent -> ent.getCategory().equals("LANCHE")).findFirst().orElse(null);
		
		when(repository.findByCategory("LANCHE")).thenReturn(List.of(entity));
		
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/products/category/LANCHE").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andReturn();
		
		Optional<Result<List<ProductResponse>>> response = jsonUtils.parse(mvcResult.getResponse().getContentAsString(), 
				new TypeReference<Result<List<ProductResponse>>>(){});
		
		int code = response.get().getCode();
		String category = response.get().getResult().get(0).category();
		String description = response.get().getResult().get(0).description();
		String title = response.get().getResult().get(0).title();
		BigDecimal price = response.get().getResult().get(0).price();
		
		assertEquals(200, code, "Must Be Equals");
		
		assertEquals("LANCHE", category, "Must Be Equals");	
		assertEquals("Carne com Alface e pao", description, "Must Be Equals");	
		assertEquals("X Salada", title, "Must Be Equals");	
		assertEquals(new BigDecimal("10.0"), price, "Must Be Equals");	
	}
	
	@Test
	public void testFindProductByCategoryBEBIDA() throws Exception {
		UpdateProductRequest request1 = new UpdateProductRequest(1L, "X Salada", "LANCHE", "Carne com Alface e pao" , new BigDecimal("10.0"), "");
		UpdateProductRequest request2 = new UpdateProductRequest(2L, "Coca Cola", "BEBIDA", "Gelada" , new BigDecimal("15.0"), "");
		UpdateProductRequest request3 = new UpdateProductRequest(3L, "Batata", "ACOMPANHAMENTO", "com bacon" , new BigDecimal("8.0"), "");
		UpdateProductRequest request4 = new UpdateProductRequest(4L, "Bolo", "SOBREMESA", "chocolate com creme" , new BigDecimal("20.0"), "");
		List<UpdateProductRequest> products = List.of(request1, request2, request3, request4);
		
		List<Product> toProducts = products.stream().map( product -> mapper.toProduct(product)).collect(Collectors.toList());
		List<ProductEntity> entities = entityMapper.toProductEntityList(toProducts);
		ProductEntity entity = entities.stream().filter( ent -> ent.getCategory().equals("BEBIDA")).findFirst().orElse(null);
		
		when(repository.findByCategory("BEBIDA")).thenReturn(List.of(entity));
		
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/products/category/BEBIDA").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andReturn();
		
		Optional<Result<List<ProductResponse>>> response = jsonUtils.parse(mvcResult.getResponse().getContentAsString(), 
				new TypeReference<Result<List<ProductResponse>>>(){});
		
		int code = response.get().getCode();
		String category = response.get().getResult().get(0).category();
		String description = response.get().getResult().get(0).description();
		String title = response.get().getResult().get(0).title();
		BigDecimal price = response.get().getResult().get(0).price();
		
		assertEquals(200, code, "Must Be Equals");
		
		assertEquals("BEBIDA", category, "Must Be Equals");	
		assertEquals("Gelada", description, "Must Be Equals");	
		assertEquals("Coca Cola", title, "Must Be Equals");	
		assertEquals(new BigDecimal("15.0"), price, "Must Be Equals");	
	}
	
	@Test
	public void testFindProductByCategoryInvalidCategory() throws Exception {
		UpdateProductRequest request1 = new UpdateProductRequest(1L, "X Salada", "LANCHE", "Carne com Alface e pao" , new BigDecimal("10.0"), "");
		UpdateProductRequest request2 = new UpdateProductRequest(2L, "Coca Cola", "BEBIDA", "Gelada" , new BigDecimal("15.0"), "");
		UpdateProductRequest request3 = new UpdateProductRequest(3L, "Batata", "ACOMPANHAMENTO", "com bacon" , new BigDecimal("8.0"), "");
		UpdateProductRequest request4 = new UpdateProductRequest(4L, "Bolo", "SOBREMESA", "chocolate com creme" , new BigDecimal("20.0"), "");
		List<UpdateProductRequest> products = List.of(request1, request2, request3, request4);
		
		List<Product> toProducts = products.stream().map( product -> mapper.toProduct(product)).collect(Collectors.toList());
		List<ProductEntity> entities = entityMapper.toProductEntityList(toProducts);
		ProductEntity entity = entities.stream().filter( ent -> ent.getCategory().equals("BEBIDA")).findFirst().orElse(null);
		
		when(repository.findByCategory("BEBIDA")).thenReturn(List.of(entity));
		
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/products/category/Test").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound()).andReturn();
		
		Optional<Result<List<ProductResponse>>> response = jsonUtils.parse(mvcResult.getResponse().getContentAsString(), 
				new TypeReference<Result<List<ProductResponse>>>(){});
		
		int code = response.get().getCode();
	
		assertEquals(404, code, "Must Be Equals");
		assertEquals(List.of("Products not found!"), 
				response.get().getErrors(), "Must Be Equals");	
		
		
	}
	
	@Test
	public void testFindProductById() throws Exception {		
		UpdateProductRequest request3 = new UpdateProductRequest(3L, "Batata", "ACOMPANHAMENTO", "com bacon" , new BigDecimal("8.0"), "");			
		Product product = mapper.toProduct(request3);
		ProductEntity entity = entityMapper.toProductEntity(product);
			
		when(repository.findById(3L)).thenReturn(Optional.ofNullable(entity));
		
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/products/find/3").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andReturn();
		
		Optional<Result<ProductResponse>> response = jsonUtils.parse(mvcResult.getResponse().getContentAsString(), 
				new TypeReference<Result<ProductResponse>>(){});
		
		int code = response.get().getCode();
		String category = response.get().getResult().category();
		String description = response.get().getResult().description();
		String title = response.get().getResult().title();
		BigDecimal price = response.get().getResult().price();
		
		assertEquals(200, code, "Must Be Equals");
		
		assertEquals("ACOMPANHAMENTO", category, "Must Be Equals");	
		assertEquals("com bacon", description, "Must Be Equals");	
		assertEquals("Batata", title, "Must Be Equals");	
		assertEquals(new BigDecimal("8.0"), price, "Must Be Equals");	
		
		
	}
	
	@Test
	public void testFindProductByIdNotFound() throws Exception {		
		UpdateProductRequest request3 = new UpdateProductRequest(3L, "Batata", "ACOMPANHAMENTO", "com bacon" , new BigDecimal("8.0"), "");
		
		Product product = mapper.toProduct(request3);
		ProductEntity entity = entityMapper.toProductEntity(product);
			
		when(repository.findById(4L)).thenReturn(Optional.ofNullable(entity));
		
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/products/find/5").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound()).andReturn();
		
		Optional<Result<ProductResponse>> response = jsonUtils.parse(mvcResult.getResponse().getContentAsString(), 
				new TypeReference<Result<ProductResponse>>(){});
		
		int code = response.get().getCode();
		
		assertEquals(404, code, "Must Be Equals");
		assertEquals(List.of("Product not found!"), 
				response.get().getErrors(), "Must Be Equals");	
		
		
	}

}
