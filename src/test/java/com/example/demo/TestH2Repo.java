package com.example.demo;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestH2Repo extends JpaRepository<Product, Integer> {
}
