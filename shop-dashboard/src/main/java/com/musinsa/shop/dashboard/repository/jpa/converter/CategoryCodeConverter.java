package com.musinsa.shop.dashboard.repository.jpa.converter;

import com.musinsa.shop.domain.enums.CategoryCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CategoryCodeConverter implements AttributeConverter<CategoryCode, String> {

    @Override
    public String convertToDatabaseColumn(CategoryCode attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getKey();
    }

    @Override
    public CategoryCode convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return CategoryCode.fromKey(dbData);
    }
}