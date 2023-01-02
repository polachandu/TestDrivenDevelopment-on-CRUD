package com.example.demo.controller;


import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public Product addProduct(@RequestBody Product product){
        return productService.saveProduct(product);
    }

    @GetMapping
    public List<Product> findAllProducts(){
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable int id){
        return productService.getProductById(id);
    }

    @PutMapping("/update/{id}")
    public Product updateProduct(@RequestBody Product product, @PathVariable int id){
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id){
        return productService.deleteProduct(id);
    }
}
