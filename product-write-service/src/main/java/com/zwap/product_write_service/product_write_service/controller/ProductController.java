package com.zwap.product_write_service.product_write_service.controller;

import com.zwap.product_common.product_common.entity.Product;
import com.zwap.product_write_service.product_write_service.dto.ProductCreateDTO;
import com.zwap.product_write_service.product_write_service.converter.ProductConverter;
import com.zwap.product_common.product_common.mapper.ProductMapper;
import com.zwap.product_write_service.product_write_service.dto.ProductUpdateDTO;
import com.zwap.product_write_service.product_write_service.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    ProductService productService;

    @PostMapping("/")
    public void create(@RequestHeader("user_id") String userId, @RequestBody ProductCreateDTO productDTO) {
        productService.create(userId, productDTO);
    }

//    @PutMapping("/")
//    public Product update(@RequestHeader("userId") String userId, @RequestBody ProductUpdateDTO productDTO) {
//        Optional<Product> optionalProduct = productMapper.findByIdAndUserId(id, userId);
//        if (optionalProduct.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this product");
//        }
//        Product product = optionalProduct.get();
//        Product product = ProductConverter.toEntity(productDTO, userId);
//        productMapper.save(product);
//        return product;
//    }
}
