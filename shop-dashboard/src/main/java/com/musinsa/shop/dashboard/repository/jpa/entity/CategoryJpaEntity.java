package com.musinsa.shop.dashboard.repository.jpa.entity;

import com.musinsa.shop.dashboard.repository.jpa.converter.CategoryCodeConverter;
import com.musinsa.shop.domain.enums.CategoryCode;
import com.musinsa.shop.domain.model.Category;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "category")
@Entity
public class CategoryJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = CategoryCodeConverter.class)
    @Column(name = "code", nullable = false, unique = true)
    private CategoryCode code;

    protected CategoryJpaEntity() {
    }

    public CategoryJpaEntity(CategoryCode code) {
        this.code = code;
    }

    public CategoryJpaEntity(Long id, CategoryCode code) {
        this.id = id;
        this.code = code;
    }

    public Category toDomain() {
        return Category.builder()
                .id(this.id)
                .code(this.code)
                .build();
    }

    public static CategoryJpaEntity fromDomain(Category category) {
        return new CategoryJpaEntity(
                category.getId(),
                category.getCode()
        );
    }
}
