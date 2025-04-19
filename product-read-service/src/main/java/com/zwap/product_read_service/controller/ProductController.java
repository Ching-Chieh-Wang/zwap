package com.zwap.product_read_service.controller;

import com.zwap.product_read_service.dto.ProductSearchDTO;
import com.zwap.product_read_service.dto.ProductSearchResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @GetMapping("/search")
    public ProductSearchResponseDTO searchProduct(@RequestBody ProductSearchDTO productSearchDTO) {
        
    }
}
