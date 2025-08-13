package com.zwap.product_read_service.controller;

import com.zwap.product_read_service.service.IProductService;
import com.zwap.product_common.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @Resource
    IProductService productService;

    @GetMapping("/{id}")
    public ProductVO getById(@PathVariable String id) {
        return productService.getVOById(id);
    }
}
