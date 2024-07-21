package com.musinsa.shop.style.repository;

import com.musinsa.shop.domain.entity.CategoryJpaEntity;
import com.musinsa.shop.domain.model.Category;
import com.musinsa.shop.style.repository.jpa.CategoryJpaRepository;
import com.musinsa.shop.style.service.persistence.LoadCategoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CategoryPersistenceAdapter implements LoadCategoryPort {
    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public List<Category> findAllCategories() {
        return categoryJpaRepository.findAll().stream()
                .map(CategoryJpaEntity::toDomain)
                .toList();
    }
}