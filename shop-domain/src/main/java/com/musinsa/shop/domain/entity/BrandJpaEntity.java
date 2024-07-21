package com.musinsa.shop.domain.entity;

import com.musinsa.shop.domain.model.Brand;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "brand")
@Entity
public class BrandJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alias", length = 50, nullable = false)
    private String alias;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @CreatedDate
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    protected BrandJpaEntity() {
    }

    public BrandJpaEntity(Long id, String alias, String name) {
        this.id = id;
        this.alias = alias;
        this.name = name;
    }

    public BrandJpaEntity(String alias, String name) {
        this.alias = alias;
        this.name = name;
    }

    public Brand toDomain() {
        return Brand.builder()
                .id(this.id)
                .alias(this.alias)
                .name(this.name)
                .build();
    }

    public static BrandJpaEntity fromDomain(Brand brand) {
        return new BrandJpaEntity(
                brand.getId(),
                brand.getAlias(),
                brand.getName()
        );
    }
}
