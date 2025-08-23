package com.zwap.product.product_database.product_database_common.mapper;

import com.zwap.product.product_database.product_database_common.entity.ProductMongo;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface IProductMapper extends MongoRepository<ProductMongo, String> {
}
