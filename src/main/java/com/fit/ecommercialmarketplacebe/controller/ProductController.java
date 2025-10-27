package com.fit.ecommercialmarketplacebe.controller;

// Thêm các import DTO
import com.fit.ecommercialmarketplacebe.dto.response.ProductDetailGeneralDto;
import com.fit.ecommercialmarketplacebe.dto.response.ProductDetailVariantDto;
import com.fit.ecommercialmarketplacebe.entity.Product;
import com.fit.ecommercialmarketplacebe.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*") // (Nên dùng cấu hình CORS toàn cục trong SecurityConfig)
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

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/general/{id}")
    public ResponseEntity<ProductDetailGeneralDto> getProductGeneral(@PathVariable Long id) {
        ProductDetailGeneralDto productDto = productService.getProductGeneralDetails(id);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping("/variant/{id}")
    public ResponseEntity<ProductDetailVariantDto> getProductVariant(@PathVariable Long id) {
        ProductDetailVariantDto productDto = productService.getProductVariantDetails(id);
        return ResponseEntity.ok(productDto);
    }
}