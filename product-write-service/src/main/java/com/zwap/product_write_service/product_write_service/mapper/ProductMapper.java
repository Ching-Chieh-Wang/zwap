package com.zwap.product_write_service.product_write_service.mapper;

import com.zwap.product_common.product_common.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMapper extends MongoRepository<Product, String> {
}
