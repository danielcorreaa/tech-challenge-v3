
package com.techchallenge.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.techchallenge.application.usecases.CustomerUseCase;
import com.techchallenge.config.infra.Result;
import com.techchallenge.domain.entity.Customer;
import com.techchallenge.infrastructure.api.mapper.CustomerMapper;
import com.techchallenge.infrastructure.api.request.CustomerRequest;
import com.techchallenge.infrastructure.api.request.CustomerResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/custumers")
@CrossOrigin
public class CustomerApi {

	private CustomerUseCase custumerUseCase;
	private CustomerMapper mapper;

	public CustomerApi(CustomerUseCase custumerUseCase, CustomerMapper mapper) {
		super();
		this.custumerUseCase = custumerUseCase;
		this.mapper = mapper;
	}

	@PostMapping
	public ResponseEntity<Result<CustomerResponse>> insert(@RequestBody @Valid CustomerRequest request,
			UriComponentsBuilder uri) {
		Customer custumer = custumerUseCase.insert(mapper.toCustomer(request));
		UriComponents uriComponents = uri.path("/api/v1/custumers/find/{cpf}")
				.buildAndExpand(custumer.getCpfValue().orElse(""));
		return ResponseEntity.created(uriComponents.toUri()).body(Result.create(mapper.toCustomerResponse(custumer)));
	}

	@PutMapping
	public ResponseEntity<Result<CustomerResponse>> update(@RequestBody @Valid CustomerRequest request) {
		Customer custumer = custumerUseCase.update(mapper.toCustomer(request));
		return ResponseEntity.ok(Result.ok(mapper.toCustomerResponse(custumer)));
	}

	@DeleteMapping("/delete/{cpf}")
	public ResponseEntity<Result<String>> delete(@PathVariable String cpf) {
		custumerUseCase.delete(cpf);
		return ResponseEntity.ok(Result.ok("Custumer delete with success!"));
	}

	@GetMapping("/find/{cpf}")
	public ResponseEntity<Result<CustomerResponse>> find(@PathVariable String cpf) {
		Customer customer = custumerUseCase.findByCpf(cpf);
		return ResponseEntity.ok(Result.ok(mapper.toCustomerResponse(customer)));
	}

}
