package com.zwap.product_read_service.service;
import com.zwap.product_common.vo.ProductVO;

public interface IProductService {
    ProductVO getVOById(String id);
    boolean checkOwnership(String id, String userId);
}
