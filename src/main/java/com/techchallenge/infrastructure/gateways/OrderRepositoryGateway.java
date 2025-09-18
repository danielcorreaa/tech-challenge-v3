package com.techchallenge.infrastructure.gateways;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.techchallenge.application.gateways.OrderGateway;
import com.techchallenge.domain.entity.Order;
import com.techchallenge.infrastructure.persistence.entity.OrderEntity;
import com.techchallenge.infrastructure.persistence.mapper.OrderEntityMapper;
import com.techchallenge.infrastructure.persistence.repository.OrderRepository;

@Component
public class OrderRepositoryGateway implements OrderGateway {

	private OrderRepository repository;
	private OrderEntityMapper mapper;

	public OrderRepositoryGateway(OrderRepository repository, OrderEntityMapper mapper) {
		super();
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public Order insert(Order order) {
		OrderEntity entity = mapper.toOrderEntity(order);
		entity = repository.save(entity);
		return mapper.toOrder(entity);
	}

	@Override
	public Optional<List<Order>> findAll(int page, int size, List<String> sort) {		
		Pageable pageable = Pageable.ofSize(size).withPage(page);
		var allOrders = repository.findAllOrderByDateOrderInit(pageable);		
		var allOrderByDate = Optional.ofNullable(allOrders.getContent());
		return allOrderByDate.map(all -> mapper.toOrderList(all));
	}

	@Override
	public Optional<List<Order>> findByStatusOrder(String recebido) {
		var findByOrderStatus = repository.findByStatusOrder(recebido);
		return findByOrderStatus.map(all -> mapper.toOrderList(all));
	}

	public Optional<Order> findbyId(String id) {
		var findByOrderStatus = repository.findById(new ObjectId(id));
		return findByOrderStatus.map(all -> mapper.toOrder(all));
	}

	@Override
	public List<Order> findByStatusAndDate() {
		Optional<List<OrderEntity>> findByStatusAndDate = null;//repository.findByStatusAndDate();
		return mapper.toOrderList(findByStatusAndDate.orElse(List.of()));
	}



}
