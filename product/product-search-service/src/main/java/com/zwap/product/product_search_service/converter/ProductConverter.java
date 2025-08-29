package com.zwap.product.product_search_service.converter;

import org.springframework.beans.BeanUtils;
import com.zwap.product.product_search_service.entity.ProductEs;
import com.zwap.product.product_search_service.vo.ProductSearchVO;

public class ProductConverter {

    public static ProductSearchVO toVO(ProductEs entity) {
        ProductSearchVO vo = new ProductSearchVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}