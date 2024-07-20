package com.musinsa.shop.dashboard.repository.jpa.entity;

import com.musinsa.shop.domain.model.Product;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "product")
@Entity
public class ProductJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandJpaEntity brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_code", referencedColumnName = "code", nullable = false)
    private CategoryJpaEntity category;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Long price;

    @CreatedDate
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    protected ProductJpaEntity() {
    }

    public ProductJpaEntity(Long id,
                            BrandJpaEntity brand,
                            CategoryJpaEntity category,
                            String name,
                            Long price) {
        this.id = id;
        this.brand = brand;
        this.category = category;
        this.name = name;
        this.price = price;
    }

    public ProductJpaEntity(BrandJpaEntity brand,
                            CategoryJpaEntity category,
                            String name,
                            Long price) {
        this.brand = brand;
        this.category = category;
        this.name = name;
        this.price = price;
    }

    public Product toDomain() {
        return Product.builder()
                .id(this.id)
                .brand(this.brand.toDomain())
                .category(this.category.toDomain())
                .name(this.name)
                .price(this.price)
                .build();
    }

    public static ProductJpaEntity fromDomain(Product product) {
        return new ProductJpaEntity(
                product.getId(),
                BrandJpaEntity.fromDomain(product.getBrand()),
                CategoryJpaEntity.fromDomain(product.getCategory()),
                product.getName(),
                product.getPrice()
        );
    }
}
