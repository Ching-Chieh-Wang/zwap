package com.zwap.product_read_service.converter;

import com.zwap.product_common.product_common.entity.Product;
import com.zwap.product_read_service.vo.ProductVO;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

public class ProductConverter {
    public static ProductVO toProductVO(Product product) {
        if (product==null) return ProductVO.NOT_FOUND;
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product, productVO);
        return productVO;
    }
}
