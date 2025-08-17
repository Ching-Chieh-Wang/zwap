package com.zwap.product_search_service.service;

import com.zwap.product_search_service.dto.ProductSearchQry;
import com.zwap.product_search_service.vo.ProductSearchVOs;

public interface IProductService {
    ProductSearchVOs search(ProductSearchQry request);
}
