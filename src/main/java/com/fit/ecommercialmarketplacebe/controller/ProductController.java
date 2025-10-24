package com.fit.ecommercialmarketplacebe.controller;

import com.fit.ecommercialmarketplacebe.entity.Product;
import com.fit.ecommercialmarketplacebe.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*") // Cho phép gọi từ FE (React Native, React Web)
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/category/{name}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String name) {
        List<Product> products = productService.getProductsByCategory(name);
        return ResponseEntity.ok(products);
    }
}