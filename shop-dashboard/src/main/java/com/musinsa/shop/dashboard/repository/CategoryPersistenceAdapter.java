package com.musinsa.shop.dashboard.repository;

import com.musinsa.shop.dashboard.repository.jpa.CategoryJpaRepository;
import com.musinsa.shop.domain.entity.CategoryJpaEntity;
import com.musinsa.shop.dashboard.service.persistence.LoadCategoryPort;
import com.musinsa.shop.domain.enums.CategoryCode;
import com.musinsa.shop.domain.model.Category;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CategoryPersistenceAdapter implements LoadCategoryPort {
    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Optional<Category> findCategoryByCode(CategoryCode categoryCode) {
        return categoryJpaRepository.findByCode(categoryCode)
                .map(CategoryJpaEntity::toDomain);
    }
}
