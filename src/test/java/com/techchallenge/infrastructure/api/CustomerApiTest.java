package com.techchallenge.infrastructure.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.techchallenge.application.usecases.CustomerUseCase;
import com.techchallenge.application.usecases.interactor.CustomerUseCaseInteractor;
import com.techchallenge.config.JsonUtils;
import com.techchallenge.config.infra.ExceptionHandlerConfig;
import com.techchallenge.config.infra.Result;
import com.techchallenge.domain.entity.Customer;
import com.techchallenge.infrastructure.api.mapper.CustomerMapper;
import com.techchallenge.infrastructure.api.request.CustomerRequest;
import com.techchallenge.infrastructure.api.request.CustomerResponse;
import com.techchallenge.infrastructure.gateways.CustomerRepositoryGateway;
import com.techchallenge.infrastructure.persistence.entity.CustomerEntity;
import com.techchallenge.infrastructure.persistence.mapper.CustomerEntityMapper;
import com.techchallenge.infrastructure.persistence.repository.CustomerRepository;


@ExtendWith(SpringExtension.class)
class CustomerApiTest {

	private MockMvc mockMvc;
	private CustomerApi customerApi;
	private CustomerUseCase customerUseCase;
	private CustomerGateway customerGateway; 
	
	@MockBean
	private CustomerRepository repository;
	
	private CustomerEntityMapper entityMapper;
	private CustomerMapper mapper;
	
	private JsonUtils jsonUtils;
	
	@BeforeEach
	public void init() {		
		ObjectMapper objectMapper = new ObjectMapper();	
		jsonUtils = new JsonUtils(objectMapper);
		mapper = new CustomerMapper();
		entityMapper = new CustomerEntityMapper();
		customerGateway = new CustomerRepositoryGateway(repository, entityMapper);
		customerUseCase = new CustomerUseCaseInteractor(customerGateway);
		customerApi = new CustomerApi(customerUseCase, mapper);
		mockMvc = MockMvcBuilders.standaloneSetup(customerApi).setControllerAdvice(new ExceptionHandlerConfig()).build();
		
	}
	
	@Test
	public void testInsertCustumerFieldsInvalidCpf() throws Exception {
		CustomerRequest request = new CustomerRequest("12345568911", "Ze Comeia", "comeia@email.com");		
		String jsonRequest = jsonUtils.toJson(request).orElse("");
		
		MvcResult mvcResult = mockMvc.perform(post("/api/v1/custumers").contentType(MediaType.APPLICATION_JSON)
					.content(jsonRequest))
					.andExpect(status().isBadRequest()).andReturn();
		
		Optional<Result<CustomerResponse>> response = jsonUtils.parse(mvcResult.getResponse().getContentAsString(), 
				new TypeReference<Result<CustomerResponse>>(){});
		int code = response.get().getCode();
		
		assertEquals(400, code, "Must Be Equals");
		assertEquals(List.of("Invalid Cpf!"), response.get().getErrors(), "Must Be Equals");
		
	}
	
	@Test
	public void testInsertCustumerFieldsInvalidRequest() throws Exception {
		CustomerRequest request = new CustomerRequest("", "", "comeia@email.com");		
		String jsonRequest = jsonUtils.toJson(request).orElse("");
		
		MvcResult mvcResult = mockMvc.perform(post("/api/v1/custumers").contentType(MediaType.APPLICATION_JSON)
					.content(jsonRequest))
					.andExpect(status().isBadRequest()).andReturn();
		
		Optional<Result<CustomerResponse>> response = jsonUtils.parse(mvcResult.getResponse().getContentAsString(), 
				new TypeReference<Result<CustomerResponse>>(){});
		int code = response.get().getCode();
		
		assertEquals(400, code, "Must Be Equals");
		assertEquals(List.of("Cpf is required!","Cpf without correct number of digits!","Name is required!"), response.get().getErrors(), "Must Be Equals");	
		
		
	}
	
	@Test
	public void testInsertCustumerFieldsValid() throws Exception {
		CustomerRequest request = new CustomerRequest("02974127010", "Ze Comeia", "comeia@email.com");
		Customer customer = mapper.toCustomer(request);
		CustomerEntity entity = entityMapper.toCustomerEntity(customer);
		
		when(repository.save(entity)).thenReturn(entity);
		
		String jsonRequest = jsonUtils.toJson(request).orElse("");
		
		MvcResult mvcResult = mockMvc.perform(post("/api/v1/custumers").contentType(MediaType.APPLICATION_JSON)
					.content(jsonRequest))
					.andExpect(status().isCreated()).andReturn();		
		
		Optional<Result<CustomerResponse>> response = jsonUtils.parse(mvcResult.getResponse().getContentAsString(), 
				new TypeReference<Result<CustomerResponse>>(){});
		
		int code = response.get().getCode();
		String cpf = response.get().getResult().cpf();
		String name = response.get().getResult().name();
		String email = response.get().getResult().email();
		
		assertEquals(201, code, "Must Be Equals");
		
		assertEquals("029.741.270-10", cpf, "Must Be Equals");	
		assertEquals("Ze Comeia", name, "Must Be Equals");	
		assertEquals("comeia@email.com", email, "Must Be Equals");	
		
		
	}
	
	@Test
	public void testUpdatetCustumerFieldsValid() throws Exception {
		CustomerRequest request = new CustomerRequest("02974127010", "Ze Comeia", "ze@email.com");
		Customer customer = mapper.toCustomer(request);
		CustomerEntity entity = entityMapper.toCustomerEntity(customer);		
		when(repository.save(entity)).thenReturn(entity);
		
		String jsonRequest = jsonUtils.toJson(request).orElse("");
		
		MvcResult mvcResult = mockMvc.perform(put("/api/v1/custumers").contentType(MediaType.APPLICATION_JSON)
					.content(jsonRequest))
					.andExpect(status().isOk()).andReturn();		
		
		Optional<Result<CustomerResponse>> response = jsonUtils.parse(mvcResult.getResponse().getContentAsString(), 
				new TypeReference<Result<CustomerResponse>>(){});
		
		int code = response.get().getCode();
		String cpf = response.get().getResult().cpf();
		String name = response.get().getResult().name();
		String email = response.get().getResult().email();
		
		assertEquals(200, code, "Must Be Equals");
		
		assertEquals("029.741.270-10", cpf, "Must Be Equals");	
		assertEquals("Ze Comeia", name, "Must Be Equals");	
		assertEquals("ze@email.com", email, "Must Be Equals");	
		
		
	}
	
	
	@Test
	public void testFindByCpf() throws Exception {
		Customer customer1 = new Customer("02974127010", "Ze Comeia", "ze@email.com");
		
		CustomerEntity entity = entityMapper.toCustomerEntity(customer1);
		
		when(repository.findByCpf("02974127010")).thenReturn(Optional.ofNullable(entity));		
			
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/custumers/find/02974127010").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andReturn();		
		
		Optional<Result<CustomerResponse>> response = jsonUtils.parse(mvcResult.getResponse().getContentAsString(), 
				new TypeReference<Result<CustomerResponse>>(){});
		
		int code = response.get().getCode();
		String cpf = response.get().getResult().cpf();
		String name = response.get().getResult().name();
		String email = response.get().getResult().email();
		
		assertEquals(200, code, "Must Be Equals");
		
		assertEquals("029.741.270-10", cpf, "Must Be Equals");	
		assertEquals("Ze Comeia", name, "Must Be Equals");	
		assertEquals("ze@email.com", email, "Must Be Equals");	
		
		
	}
	
	@Test
	public void testFindByCpfNotFound() throws Exception {
		Customer customer1 = new Customer("02974127010", "Ze Comeia", "ze@email.com");
		
		CustomerEntity entity = entityMapper.toCustomerEntity(customer1);
		
		when(repository.findByCpf("02974127010")).thenReturn(Optional.ofNullable(entity));		
			
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/custumers/find/123232323").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound()).andReturn();		
		
		Optional<Result<CustomerResponse>> response = jsonUtils.parse(mvcResult.getResponse().getContentAsString(), 
				new TypeReference<Result<CustomerResponse>>(){});
		
		int code = response.get().getCode();
		
		assertEquals(404, code, "Must Be Equals");
		assertEquals(List.of("Customer not found!"), response.get().getErrors(), "Must Be Equals");
		
		
	}
}
