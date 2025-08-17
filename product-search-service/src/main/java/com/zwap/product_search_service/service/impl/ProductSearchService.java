package com.zwap.product_search_service.service.impl;

import com.zwap.product_search_service.dto.ProductSearchQry;
import com.zwap.product_search_service.service.IProductService;
import com.zwap.product_search_service.vo.ProductSearchVOs;
import org.springframework.stereotype.Service;

@Service
public class ProductSearchService implements IProductService {

    @Override
    public ProductSearchVOs search(ProductSearchQry request) {
        return new ProductSearchVOs();
    }
}
