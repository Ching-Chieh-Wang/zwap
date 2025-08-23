package com.zwap.product.product_database.product_read_service.converter;

import com.zwap.product.product_database.product_database_common.entity.ProductMongo;
import com.zwap.product.product_database.product_read_service.vo.ProductVO;
import org.springframework.beans.BeanUtils;

public class ProductConverter {
    public static ProductVO toVO(ProductMongo product) {
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product, productVO);
        return productVO;
    }
}
