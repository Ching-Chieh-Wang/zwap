package com.zwap.product.product_database.product_write_service.service;
import com.zwap.product.product_database.product_write_service.dto.ProductCreateQry;
import com.zwap.product.product_database.product_write_service.dto.ProductUpdateQry;


public interface IProductService {
    void create(String userId, ProductCreateQry productCreateQry);
    void update(String userId, String id, ProductUpdateQry productUpdateQry);
    void delete(String userId, String id);
}
