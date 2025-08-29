package com.zwap.product.product_search_service.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchVOs {
    private List<ProductSearchVO> products;
}
