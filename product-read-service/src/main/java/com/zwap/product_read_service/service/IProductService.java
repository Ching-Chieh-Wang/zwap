package com.zwap.product_read_service.service;

import com.zwap.product_read_service.vo.ProductVO;
import org.springframework.web.bind.annotation.RequestParam;


public interface IProductService {
    ProductVO getById( String id);
}
