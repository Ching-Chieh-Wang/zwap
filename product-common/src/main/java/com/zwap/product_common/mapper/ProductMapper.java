package com.zwap.product_common.mapper;

import com.zwap.product_common.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMapper extends MongoRepository<Product, String> {
}
