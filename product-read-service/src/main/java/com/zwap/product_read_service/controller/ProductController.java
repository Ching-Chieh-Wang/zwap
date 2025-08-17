package com.zwap.product_read_service.controller;

import com.zwap.product_read_service.service.IProductService;
import com.zwap.product_common.vo.ProductVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    IProductService productService;

    @GetMapping("/{id}")
    public ProductVO getVOById(@PathVariable String id) {
        return productService.getVOById(id);
    }
}
