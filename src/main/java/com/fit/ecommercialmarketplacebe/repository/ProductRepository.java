package com.fit.ecommercialmarketplacebe.repository;

import com.fit.ecommercialmarketplacebe.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryNameIgnoreCase(String name);
    // findProductByProductId(Long id) đã được JpaRepository cung cấp (dưới tên findById)
    // Chúng ta sẽ dùng findById(id)
}