package com.zwap.product_read_service.service;

import com.zwap.product_read_service.vo.ProductVO;


public interface IProductService {
    ProductVO getById( String id);
    boolean isUserOwner(String userId,  String productId);
}
