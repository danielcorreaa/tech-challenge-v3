package com.techchallenge.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.techchallenge.infrastructure.persistence.entity.OrderEntity;

public interface OrderRepository extends MongoRepository<OrderEntity, ObjectId> {		
	
	//@Query("Select o from OrderEntity o ORDER BY o.dateOrderInit ASC ")
	Page<OrderEntity> findAllOrderByDateOrderInit(Pageable pageable);

	Optional<List<OrderEntity>> findByStatusOrder(String recebido);

	//@Query("Select o from OrderEntity o where o.statusOrder <> 'FINALIZADO' ORDER BY FIELD(o.statusOrder, 'PRONTO', 'EM_PREPARACAO', 'RECEBIDO'), dateOrderInit ASC ")
	Optional<List<OrderEntity>> findByStatusOrderAndDateOrderInit();

	
}
