package com.techchallenge.application.usecases.interactor;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.techchallenge.application.gateways.CustomerGateway;
import com.techchallenge.application.gateways.OrderGateway;
import com.techchallenge.application.gateways.ProductGateway;
import com.techchallenge.application.usecases.OrderUseCase;
import com.techchallenge.domain.entity.Customer;
import com.techchallenge.domain.entity.Order;
import com.techchallenge.domain.entity.Product;
import com.techchallenge.domain.errors.NotFoundException;

public class OrderUseCaseInteractor implements OrderUseCase {

	private OrderGateway crderGateway;
	private CustomerGateway customerGateway;
	private ProductGateway productGateway;
	
	

	public OrderUseCaseInteractor(OrderGateway crderGateway, CustomerGateway customerGateway,
			ProductGateway productGateway) {
		super();
		this.crderGateway = crderGateway;
		this.customerGateway = customerGateway;
		this.productGateway = productGateway;
	}

	public Order insert(String cpf, List<String> productsId) {
		Optional<Customer> customer = customerGateway.findByCpf(cpf);

		List<Product> products = productGateway.findByIds(productsId)
				.orElseThrow(() -> new NotFoundException("Any product found!"));

		Order order = new Order().startOrder(customer.orElse(null), products);

		return crderGateway.insert(order);
	}

	public List<Order> findAll(int page, int size, List<String> sort) {
		return crderGateway.findAll(page, size, sort).orElseThrow(() -> new NotFoundException("Order not found"));
	}
	
	public Order ready(String id) {
		Order order = findById(id);
		return crderGateway.insert(order.ready());
	}

	public Order finish(String id) {
		Order order = findById(id);
		return crderGateway.insert(order.finishOrder(id));
	}

	public Order findById(String id) {
		return crderGateway.findbyId(id).orElseThrow(() -> new NotFoundException("Order not found!"));
	}

	@Override
	public List<Order> findAllByStatusAndDate() {
		return crderGateway.findByStatusAndDate();
	}

}
