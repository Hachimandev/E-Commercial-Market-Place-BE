package com.fit.ecommercialmarketplacebe.specification;

import com.fit.ecommercialmarketplacebe.entity.Category;
import com.fit.ecommercialmarketplacebe.entity.Product;
import com.fit.ecommercialmarketplacebe.entity.ProductFeature;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ProductSpecification {
    /**
     * Tìm kiếm theo tên hoặc mô tả
     */
    public static Specification<Product> hasNameOrDescription(String query) {
        return (Root<Product> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            if (!StringUtils.hasText(query)) {
                return cb.conjunction();
            }
            String likePattern = "%" + query.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), likePattern),
                    cb.like(cb.lower(root.get("description")), likePattern)
            );
        };
    }
    /**
     * Lọc theo khoảng giá
     */
    public static Specification<Product> hasPriceInRange(Double minPrice, Double maxPrice) {
        return (Root<Product> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            if (minPrice != null && maxPrice != null) {
                return cb.between(root.get("price"), minPrice, maxPrice);
            }
            if (minPrice != null) {
                return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            }
            if (maxPrice != null) {
                return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
            return cb.conjunction();
        };
    }
    /**
     * Lọc theo rating tối thiểu
     */
    public static Specification<Product> hasMinRating(Double minRating) {
        return (Root<Product> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            if (minRating != null && minRating > 0) {
                return cb.greaterThanOrEqualTo(root.get("rating"), minRating);
            }
            return cb.conjunction();
        };
    }

    /**
     * Lọc theo tên category
     */
    public static Specification<Product> hasCategory(String categoryName) {
        return (Root<Product> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            if (!StringUtils.hasText(categoryName)) {
                return cb.conjunction();
            }
            Join<Product, Category> categoryJoin = root.join("category", JoinType.LEFT);
            return cb.equal(cb.lower(categoryJoin.get("name")), categoryName.toLowerCase());
        };
    }

    /**
     * Lọc theo đặc tính sản phẩm (ví dụ: "30-day Free Return")
     */
    public static Specification<Product> hasFeature(String featureText) {
        return (Root<Product> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            if (!StringUtils.hasText(featureText)) {
                return cb.conjunction();
            }
            Join<Product, ProductFeature> featureJoin = root.join("features", JoinType.LEFT);
            return cb.equal(featureJoin.get("text"), featureText);
        };
    }
}