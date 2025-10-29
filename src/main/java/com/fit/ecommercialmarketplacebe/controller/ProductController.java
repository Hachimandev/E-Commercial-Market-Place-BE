package com.fit.ecommercialmarketplacebe.controller;

// Thêm các import DTO
import com.fit.ecommercialmarketplacebe.dto.response.ProductDetailGeneralDto;
import com.fit.ecommercialmarketplacebe.dto.response.ProductDetailVariantDto;
import com.fit.ecommercialmarketplacebe.entity.Product;
import com.fit.ecommercialmarketplacebe.entity.Seller;
import com.fit.ecommercialmarketplacebe.service.ProductService;
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
        Product existing = productService.getProductById(id);


        if (existing.getSeller() == null ||
                !existing.getSeller().getUserId().equals(seller.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        productDetails.setSeller(seller);
        Product updated = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id,
                                              @AuthenticationPrincipal Seller seller) {
        Product product = productService.getProductById(id);

        if (!product.getSeller().getUserId().equals(seller.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
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
}