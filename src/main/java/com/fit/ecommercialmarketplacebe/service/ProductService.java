package com.fit.ecommercialmarketplacebe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit.ecommercialmarketplacebe.dto.response.*;
import com.fit.ecommercialmarketplacebe.entity.Product;
import com.fit.ecommercialmarketplacebe.entity.ProductOption;
import com.fit.ecommercialmarketplacebe.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- Import quan trọng

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper; // Dùng để đọc JSON metadata của màu sắc

    // Cập nhật constructor
    public ProductService(ProductRepository productRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(String name) {
        return productRepository.findByCategoryNameIgnoreCase(name);
    }

    // Phương thức cũ (vẫn giữ lại nếu cần)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    // ==========================================================
    // ✅ PHƯƠNG THỨC MỚI CHO ProductDetailGeneralScreen
    // ==========================================================
    @Transactional(readOnly = true) // Cần thiết để lazy-loading
    public ProductDetailGeneralDto getProductGeneralDetails(Long id) {
        Product product = getProductById(id); // Gọi lại hàm trên để lấy product

        List<FeatureDto> featureDtos = product.getFeatures().stream()
                .map(feature -> FeatureDto.builder()
                        .id(feature.getId().toString())
                        .icon(feature.getIcon())
                        .text(feature.getText())
                        .build())
                .collect(Collectors.toList());

        List<ReviewDto> reviewDtos = product.getReviews().stream()
                .map(review -> ReviewDto.builder()
                        .id(review.getReviewId().toString())
                        .user(review.getBuyer().getFullName())
                        .comment(review.getComment())
                        .userImage(review.getBuyer().getAvatarURL())
                        // TODO: Xử lý `date` thành định dạng "X days ago"
                        .date(review.getDate().toString())
                        .build())
                .collect(Collectors.toList());

        List<RelevantProductDto> relevantDtos = product.getRelevantProducts().stream()
                .map(relProduct -> RelevantProductDto.builder()
                        .id(relProduct.getProductId().toString())
                        .name(relProduct.getName())
                        .rating(relProduct.getRating())
                        .price(relProduct.getPrice())
                        .image(relProduct.getImageURL())
                        .build())
                .collect(Collectors.toList());

        return ProductDetailGeneralDto.builder()
                .id(product.getProductId().toString())
                .name(product.getName())
                .price(product.getPrice())
                .rating(product.getRating())
                .reviewCount(product.getReviews() != null ? product.getReviews().size() : 0)
                .description(product.getDescription())
                .imageURL(product.getImageURL())
                .features(featureDtos)
                .reviews(reviewDtos)
                .relevantProducts(relevantDtos)
                .build();
    }

    // ==========================================================
    // ✅ PHƯƠNG THỨC MỚI CHO ProductDetailVariantScreen
    // ==========================================================
    @Transactional(readOnly = true) // Cần thiết để lazy-loading
    public ProductDetailVariantDto getProductVariantDetails(Long id) {
        Product product = getProductById(id);

        String shortDesc = (product.getDescription() != null && product.getDescription().length() > 50)
                ? product.getDescription().substring(0, 50) + "..."
                : product.getDescription();

        // Lấy ảnh phụ
        List<ProductImageDto> imageDtos = product.getImages().stream()
                .map(img -> ProductImageDto.builder()
                        .id(img.getId().toString())
                        .imageURL(img.getImageURL())
                        .build())
                .collect(Collectors.toList());

        List<ProductColorDto> colorDtos = Collections.emptyList();
        List<String> sizeList = Collections.emptyList();

        // Xử lý Options
        if (product.getOptions() != null) {
            for (ProductOption option : product.getOptions()) {
                if ("Color".equalsIgnoreCase(option.getName())) {
                    colorDtos = option.getValues().stream()
                            .map(val -> ProductColorDto.builder()
                                    .id(val.getId().toString())
                                    .code(ProductColorDto.parseColorCode(val.getMetadata(), objectMapper)) // Đọc JSON
                                    .build())
                            .collect(Collectors.toList());
                } else if ("Size".equalsIgnoreCase(option.getName())) {
                    sizeList = option.getValues().stream()
                            .map(val -> val.getValue())
                            .collect(Collectors.toList());
                }
            }
        }

        return ProductDetailVariantDto.builder()
                .id(product.getProductId().toString())
                .name(product.getName())
                .shortDescription(shortDesc)
                .price(product.getPrice())
                .rating(product.getRating())
                .offer(product.getOffer())
                .mainImageURL(product.getImageURL())
                .images(imageDtos)
                .colors(colorDtos)
                .sizes(sizeList)
                .build();
    }
}