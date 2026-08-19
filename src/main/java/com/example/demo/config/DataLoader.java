package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner initDatabase(ProductRepository repository){
        return args->{
            // Generate 50 dummy products
            for (int i=1; i<=50; i++){
                Product product = new Product("Test Product: "+i, 100.0+i);
                repository.save(product);
            }
        };
    }
}
