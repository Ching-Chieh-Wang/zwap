package com.zwap.product.product_database.product_write_service.controller;

import com.zwap.product.product_database.product_write_service.dto.ProductCreateQry;
import com.zwap.product.product_database.product_write_service.dto.ProductUpdateQry;
import com.zwap.product.product_database.product_write_service.service.IProductService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    IProductService productService;

    @PostMapping
    public void create(@RequestHeader("user_id") String userId, @RequestBody @Valid ProductCreateQry productDTO) {
        productService.create(userId, productDTO);
    }

    @PutMapping("/{id}")
    public void update(@RequestHeader("user_id") String userId, @PathVariable("id") String id, @RequestBody @Valid ProductUpdateQry productUpdateQry) {
        productService.update(userId, id, productUpdateQry);
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader("user_id") String userId, @PathVariable("id") String id) {
        productService.delete(userId, id);
    }
}
