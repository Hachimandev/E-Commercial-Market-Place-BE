package com.fit.ecommercialmarketplacebe.controller;

// Thêm các import DTO
import com.fit.ecommercialmarketplacebe.dto.response.ProductDetailGeneralDto;
import com.fit.ecommercialmarketplacebe.dto.response.ProductDetailVariantDto;
import com.fit.ecommercialmarketplacebe.entity.Product;
import com.fit.ecommercialmarketplacebe.entity.Seller;
import com.fit.ecommercialmarketplacebe.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/seller")
    public ResponseEntity<List<Product>> getSellerProducts(@AuthenticationPrincipal Seller seller) {
        List<Product> products = productService.getProductsBySeller(seller);
        return ResponseEntity.ok(products);
    }
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product,
                                                 @AuthenticationPrincipal Seller seller) {
        product.setSeller(seller);
        Product created = productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
                                                 @RequestBody Product productDetails,
                                                 @AuthenticationPrincipal Seller seller) {

        productDetails.setSeller(seller);
        Product updated = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
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

    @GetMapping("/recommended")
    public ResponseEntity<List<Product>> getRecommendedProducts() {
        List<Product> products = productService.getRecommendedProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam(required = false) String query,

            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String featureText,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "productId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir
    ) {

        Page<Product> products = productService.searchProducts(
                query, minPrice, maxPrice, minRating, categoryName, featureText,
                page, size, sortBy, sortDir
        );
        return ResponseEntity.ok(products);
    }
}