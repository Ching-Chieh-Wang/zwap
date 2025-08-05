package com.zwap.product_read_service.converter;

import com.zwap.product_common.entity.Product;
import com.zwap.product_read_service.vo.ProductVO;
import org.springframework.beans.BeanUtils;

public class ProductConverter {
    public static ProductVO toProductVO(Product product) {
        if (product==null) return ProductVO.NOT_FOUND;
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product, productVO);
        return productVO;
    }
}
