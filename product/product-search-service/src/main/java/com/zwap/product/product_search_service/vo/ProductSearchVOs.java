package com.zwap.product.product_search_service.vo;

import com.zwap.product.product_search_service.entity.ProductEs;
import lombok.Data;

import java.util.List;

@Data
public class ProductSearchVOs {
    List<ProductEs> products;
}
