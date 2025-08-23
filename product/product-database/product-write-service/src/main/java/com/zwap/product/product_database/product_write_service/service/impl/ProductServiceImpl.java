package com.zwap.product.product_database.product_write_service.service.impl;

import com.zwap.product.product_database.product_database_common.entity.ProductMongo;
import com.zwap.product.product_database.product_database_common.exception.ProductOwnershipException;
import com.zwap.product.product_database.product_database_common.mapper.IProductMapper;
import com.zwap.product.product_database.product_write_service.client.ProductReadGrpcClient;
import com.zwap.product.product_database.product_write_service.converter.ProductConverter;
import com.zwap.product.product_database.product_write_service.dto.ProductCreateQry;
import com.zwap.product.product_database.product_write_service.dto.ProductUpdateQry;
import com.zwap.product.product_database.product_write_service.service.IProductService;
import com.zwap.product.product_database.product_write_service.utils.UpdateBuilderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductMapper productMapper;

    @Autowired
    private ProductReadGrpcClient productReadGrpcClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UpdateBuilderUtils updateBuilderUtils;


    @Override
    public void create(String userId, ProductCreateQry productCreateQry) {
        ProductMongo product = ProductConverter.toMongo(productCreateQry, userId);
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
                ProductMongo.class
            );
        }
    }

    @Override
    public void delete(String userId, String id) {
        boolean isProductOwner = productReadGrpcClient.isProductOwner(id,userId);
        if (!isProductOwner) throw new ProductOwnershipException("You do not own this product");
        mongoTemplate.remove(
            Query.query(Criteria.where("_id").is(id)),
            ProductMongo.class
        );
    }
}
