package com.zwap.product_read_service.service.impl;

import com.zwap.product_common.product_common.entity.Product;
import com.zwap.product_common.product_common.mapper.ProductMapper;
import com.zwap.product_read_service.converter.ProductConverter;
import com.zwap.product_read_service.service.IProductService;
import com.zwap.product_read_service.vo.ProductVO;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;

public class ProductServiceImpl implements IProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private ProductConverter productConverter;

    @Override
    @Cacheable(value = "product", key = "#id")
    public ProductVO getById(String id) {
        Product product = productMapper.findById(id).orElse(null);
        return ProductConverter.toProductVO(product);
    }
}
