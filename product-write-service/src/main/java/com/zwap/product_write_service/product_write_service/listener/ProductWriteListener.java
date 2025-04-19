package com.zwap.product_write_service.product_write_service.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProductWriteListener {

    @KafkaListener(topics = "dbz.productdb.products", groupId = "product-write-consumer")
    public void listenWriteSuccess(ConsumerRecord<String, String> record) {
        System.out.println("Received CDC event: " + record.value());
    }
}
