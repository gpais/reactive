package com.example.shoppingservice.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.shoppingservice.aggregate.MessagesAggregate;

@Repository
public interface MessagesAggregateRepo extends CrudRepository<MessagesAggregate,	String>{

}
