package com.zwap.product_search_service.repository;

import com.zwap.product_search_service.entity.ProductEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface ProductEsRepository extends ElasticsearchRepository<ProductEs, String> {
    List<ProductEs> findByTitleContaining(String title);
}