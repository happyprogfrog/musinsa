package com.musinsa.shop.style.service;

import com.musinsa.shop.domain.model.Category;
import com.musinsa.shop.style.service.persistence.LoadCategoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CategoryService {
    private final LoadCategoryPort loadCategoryPort;

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return loadCategoryPort.findAllCategories();
    }
}
