package com.zwap.product_write_service.service.impl;

import com.zwap.product_common.entity.Product;
import com.zwap.product_common.exception.ProductOwnershipException;
import com.zwap.product_write_service.client.ProductReadGrpcClient;
import com.zwap.product_write_service.converter.ProductConverter;
import com.zwap.product_write_service.dto.ProductCreateQry;
import com.zwap.product_write_service.dto.ProductUpdateQry;
import com.zwap.product_write_service.service.ProductService;
import com.zwap.product_common.mapper.ProductMapper;
import com.zwap.product_write_service.utils.UpdateBuilderUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private ProductReadGrpcClient productReadGrpcClient;

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private UpdateBuilderUtils updateBuilderUtils;


    @Override
    public void create(String userId, ProductCreateQry productCreateQry) {
        Product product = ProductConverter.toEntity(productCreateQry, userId);
        productMapper.save(product);
    }

    @Override
    public void update(String userId, String id, ProductUpdateQry productUpdateQry) {
        boolean isProductOwner = productReadGrpcClient.isProductOwner(id,userId);
        if (!isProductOwner) throw new ProductOwnershipException("You do not own this product");
        Update u = updateBuilderUtils.buildUpdateFrom(productUpdateQry);

        if (!u.getUpdateObject().isEmpty()) {
            mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(id)),
                u,
                Product.class
            );
        }
    }

    @Override
    public void delete(String userId, String id) {
        boolean isProductOwner = productReadGrpcClient.isProductOwner(id,userId);
        if (!isProductOwner) throw new ProductOwnershipException("You do not own this product");
        mongoTemplate.remove(
            Query.query(Criteria.where("_id").is(id)),
            Product.class
        );
    }
}
