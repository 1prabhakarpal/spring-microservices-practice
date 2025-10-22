package com.prabhakar.ProductService.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.prabhakar.ProductService.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

}
