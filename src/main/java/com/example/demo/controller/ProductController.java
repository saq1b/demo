package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository repository;
    public ProductController(ProductRepository repository){
        this.repository = repository;
    }
    @GetMapping
    public Page<Product> getProducts(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
}
