package com.zwap.product_search_service.controller;

import com.zwap.product_search_service.dto.ProductSearchQry;
import com.zwap.product_search_service.service.IProductService;
import com.zwap.product_search_service.vo.ProductSearchVOs;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    IProductService productService;

    @PostMapping()
    public ProductSearchVOs search(@RequestHeader("user_id") String userId, @RequestBody @Valid ProductSearchQry query) {
        return productService.search(query);
    }

}
