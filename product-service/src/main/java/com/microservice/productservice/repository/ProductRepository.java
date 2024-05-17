package com.microservice.productservice.repository;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.microservice.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
	

}
