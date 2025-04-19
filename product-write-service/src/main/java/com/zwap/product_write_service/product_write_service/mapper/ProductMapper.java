package com.zwap.product_write_service.product_write_service.mapper;

import com.zwap.common.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMapper extends JpaRepository<Product, Long> {
}
