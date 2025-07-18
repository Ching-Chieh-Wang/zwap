package com.zwap.product_read_service.controller;

import com.zwap.product_read_service.dto.ProductSearchDTO;
import com.zwap.product_read_service.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @GetMapping("/")
    public ProductVO getById(@RequestParam("id") String id) {
        return new ProductVO();
    }
}
