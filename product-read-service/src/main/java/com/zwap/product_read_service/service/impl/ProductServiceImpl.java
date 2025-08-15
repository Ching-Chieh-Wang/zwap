package com.zwap.product_read_service.service.impl;

import com.zwap.product_common.entity.Product;
import com.zwap.product_common.exception.ProductNotFoundException;
import com.zwap.product_common.mapper.ProductMapper;
import com.zwap.product_read_service.converter.ProductConverter;
import com.zwap.product_read_service.service.IProductService;
import com.zwap.product_common.vo.ProductVO;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class ProductServiceImpl implements IProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    @Cacheable(value = "product", key = "#id")
    public ProductVO getVOById(String id) {
        Product product = getById(id);
        if (product == null) {
            throw new ProductNotFoundException(id);
        }
        return ProductConverter.toProductVO(product);
    }

    @Override
    public Product getById(String id) {
        return productMapper.findById(id).orElse(null);
    }

}


