package com.zwap.product_search_service.service.impl;

import com.zwap.product_search_service.dto.ProductSearchQry;
import com.zwap.product_search_service.repository.ProductEsRepository;
import com.zwap.product_search_service.service.IProductService;
import com.zwap.product_search_service.vo.ProductSearchVOs;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProductSearchService implements IProductService {

    @Resource
    private ProductEsRepository productEsRepository;

    @Override
    public ProductSearchVOs search(ProductSearchQry request) {
        // simple search by title
        var products = productEsRepository.findByTitleContaining(request.getSearchParam());

        // map to VO
        ProductSearchVOs result = new ProductSearchVOs();
        result.setProducts(products); // adjust according to your VO structure
        return result;
    }
}
