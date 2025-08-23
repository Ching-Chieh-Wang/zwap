package com.zwap.product.product_search_service.controller;

import com.zwap.product.product_search_service.dto.ProductSearchQry;
import com.zwap.product.product_search_service.service.IProductService;
import com.zwap.product.product_search_service.vo.ProductSearchVOs;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    IProductService productService;

    @PostMapping
    public ProductSearchVOs search( @RequestBody @Valid ProductSearchQry query) {
        return productService.search(query);
    }

}
