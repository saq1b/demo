package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductBatchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BatchController {

    private final ProductBatchService service;

    public BatchController(ProductBatchService service) {
        this.service = service;
    }

    @GetMapping("/test-batch")
    public String runTest() {
        List products = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            products.add(new Product("Batch Item", 50.0));
        }

        long jpaTime = service.insertWithJpa(products);
        long jdbcTime = service.insertWithJdbc(products);

        return "Inserted 10,000 records. JPA took: " + jpaTime + "ms | JdbcTemplate took: " + jdbcTime + "ms";
    }
}