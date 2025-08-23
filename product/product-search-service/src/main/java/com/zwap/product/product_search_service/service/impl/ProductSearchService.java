package com.zwap.product.product_search_service.service.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.zwap.product.product_search_service.dto.ProductSearchQry;
import com.zwap.product.product_search_service.service.IProductService;
import com.zwap.product.product_search_service.vo.ProductSearchVOs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import com.zwap.product.product_search_service.entity.ProductEs;


@Service
public class ProductSearchService implements IProductService {

    @Autowired
    private ElasticsearchOperations ops;

    @Override
    public ProductSearchVOs search(ProductSearchQry q) {
        var bool = new co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery.Builder();

        if (q.getSearchParam() != null && !q.getSearchParam().isBlank()) {
            // match on "title"
            bool.must(QueryBuilders.match(m -> m
                    .field("title")
                    .query(q.getSearchParam())
            ));
        }

        // Add price filter if present
        Query priceFilter = buildPriceFilter(q);
        if (priceFilter != null) {
            bool.filter(priceFilter);
        }

        // Add geo distance filter if present
        Query distanceFilter = buildDistanceFilter(q);
        if (distanceFilter != null) {
            bool.filter(distanceFilter);
        }

        // Convert BoolQuery -> Query
        Query qb = new Query.Builder().bool(bool.build()).build();

        // Wrap in Spring Data's NativeQuery
        var nq = NativeQuery.builder()
                .withQuery(qb)
                .withPageable(PageRequest.of(0, 50))
                .build();

        // Execute
        var results = ops.search(nq, ProductEs.class)
                .map(SearchHit::getContent)
                .toList();
        ProductSearchVOs productSearchVOs = new ProductSearchVOs();
        productSearchVOs.setProducts(results);
        return productSearchVOs;
    }

    /**
     * Builds a price filter query for the given search parameters.
     * @param q The product search query DTO.
     * @return Query for price filter, or null if not applicable.
     */
    private Query buildPriceFilter(ProductSearchQry q) {
        if (q.getMinPrice() != null || q.getMaxPrice() != null) {
            var range = new RangeQuery.Builder()
                    .field("price");

            if (q.getMinPrice() != null) {
                range.gte(JsonData.of(q.getMinPrice()));
            }
            if (q.getMaxPrice() != null) {
                range.lte(JsonData.of(q.getMaxPrice()));
            }

            return new Query.Builder().range(range.build()).build();
        }
        return null;
    }

    /**
     * Builds a geo distance filter query for the given search parameters.
     * @param q The product search query DTO.
     * @return Query for geo distance filter, or null if not applicable.
     */
    private Query buildDistanceFilter(ProductSearchQry q) {
        if (q.getDistance() != null && q.getDistance() > 0) {
            var geoDistance = new co.elastic.clients.elasticsearch._types.query_dsl.GeoDistanceQuery.Builder()
                    .field("geoData")
                    .distance(q.getDistance() + "mi")
                    .location(l -> l
                            .latlon(latlon -> latlon
                                    .lat(q.getLocation().getLat())
                                    .lon(q.getLocation().getLon())
                            )
                    )
                    .build();
            return new Query.Builder().geoDistance(geoDistance).build();
        }
        return null;
    }
}
