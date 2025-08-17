package com.zwap.product_search_service.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Document;
import com.zwap.product_common.entity.Product ;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(indexName = "mongo.product.products")
public class ProductEs extends Product {
}
