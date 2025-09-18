package com.techchallenge.infrastructure.persistence.repository;

import com.mongodb.client.result.UpdateResult;
import com.techchallenge.infrastructure.persistence.entity.PaymentEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentCustomRepository {

    private MongoTemplate mongoTemplate;

    public PaymentCustomRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public int updateStatusById(ObjectId id, String status) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().set("status", status);

        UpdateResult result = mongoTemplate.updateFirst(query, update, PaymentEntity.class);
        return Math.toIntExact(result.getModifiedCount());
    }
}
