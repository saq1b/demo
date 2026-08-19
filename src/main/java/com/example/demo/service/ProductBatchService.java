package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductBatchService {
    private final ProductRepository repository;
    private final JdbcTemplate jdbcTemplate; // Spring auto-injects this!

    public ProductBatchService(ProductRepository repository, JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public long insertWithJpa(List<Product> products) {
        long start = System.currentTimeMillis();
        repository.saveAll(products);
        return System.currentTimeMillis() - start;
    }

    @Transactional
    public long insertWithJdbc(List<Product> products) {
        long start = System.currentTimeMillis();
        
        // Raw SQL query
        String sql = "INSERT INTO product (name, price) VALUES (?, ?)";
        
        // Map our Product objects to the ? parameters
        List batchArgs = new ArrayList<>();
        for (Product p : products) {
            batchArgs.add(new Object[]{p.getName(), p.getPrice()});
        }
        
        jdbcTemplate.batchUpdate(sql, batchArgs);
        return System.currentTimeMillis() - start;
    }
}
