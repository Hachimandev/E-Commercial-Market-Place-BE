package com.fit.ecommercialmarketplacebe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit.ecommercialmarketplacebe.dto.response.*;
import com.fit.ecommercialmarketplacebe.entity.OrderStatus;
import com.fit.ecommercialmarketplacebe.entity.Product;
import com.fit.ecommercialmarketplacebe.entity.ProductOption;
import com.fit.ecommercialmarketplacebe.entity.Seller;
import com.fit.ecommercialmarketplacebe.repository.ProductRepository;
import com.fit.ecommercialmarketplacebe.specification.ProductSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

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

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm có ID: " + id));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());
        product.setImageURL(productDetails.getImageURL());
        product.setRating(productDetails.getRating());
        product.setOffer(productDetails.getOffer());
        product.setCategory(productDetails.getCategory());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm có ID: " + id));
        productRepository.delete(product);
    }

    public List<Product> getProductsBySeller(Seller seller) {
        return productRepository.findBySeller(seller);
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public ProductDetailVariantDto getProductVariantDetails(Long id) {
        Product product = getProductById(id);

        String shortDesc = (product.getDescription() != null && product.getDescription().length() > 50)
                ? product.getDescription().substring(0, 50) + "..."
                : product.getDescription();

        List<ProductImageDto> imageDtos = product.getImages().stream()
                .map(img -> ProductImageDto.builder()
                        .id(img.getId().toString())
                        .imageURL(img.getImageURL())
                        .build())
                .collect(Collectors.toList());

        List<ProductColorDto> colorDtos = Collections.emptyList();
        List<String> sizeList = Collections.emptyList();

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

    public List<Product> getRecommendedProducts() {
        Pageable topTen = PageRequest.of(0, 10);
        return productRepository.findBestSellingProducts(OrderStatus.DELIVERED, topTen);
    }

    @Transactional(readOnly = true)
    public Page<Product> searchProducts(
            String query, Double minPrice, Double maxPrice,
            Double minRating, String categoryName, String featureText,
            int page, int size, String sortBy, String sortDir
    ) {
        Specification<Product> spec = ProductSpecification.hasNameOrDescription(query);
        spec = spec.and(ProductSpecification.hasPriceInRange(minPrice, maxPrice));
        spec = spec.and(ProductSpecification.hasMinRating(minRating));
        spec = spec.and(ProductSpecification.hasCategory(categoryName));
        spec = spec.and(ProductSpecification.hasFeature(featureText));

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.findAll(spec, pageable);
    }
}