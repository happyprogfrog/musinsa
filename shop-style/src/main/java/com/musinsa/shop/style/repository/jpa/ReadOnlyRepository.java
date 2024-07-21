package com.musinsa.shop.style.repository.jpa;

import java.util.List;
import java.util.Optional;

public interface ReadOnlyRepository <T, ID> {
    Optional<T> findById(ID id);

    List<T> findAll();

    long count();
}
