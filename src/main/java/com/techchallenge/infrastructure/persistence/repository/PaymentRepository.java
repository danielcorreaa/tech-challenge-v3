package com.techchallenge.infrastructure.persistence.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.techchallenge.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.mongodb.repository.Query;

public interface PaymentRepository extends MongoRepository<PaymentEntity, String>{

    @Query("order._id: ?0")
    Optional<PaymentEntity> findByOrderId(String id);

   //@Transactional
   //@Modifying
   //@Query("Update PaymentEntity pe set pe.status = 'APROVADO' where pe.order.sku = :externalReferencelong")
   // @Modifying
   // @Query(value = "{ 'id': ?0 }, update = { '$set': { 'status': 'APROVADO' } }")
    //int updateStatus(ObjectId externalReferencelong);

}
