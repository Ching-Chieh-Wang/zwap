package com.zwap.product.product_database.product_read_service.service;

import com.zwap.product.product_database.product_read_service.vo.ProductVO;

public interface IProductService {
    ProductVO getVOById(String id);
    boolean isProductOwnder(String id, String userId);
}
