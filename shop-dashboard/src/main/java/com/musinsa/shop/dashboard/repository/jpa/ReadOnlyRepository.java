package com.musinsa.shop.dashboard.repository.jpa;

import java.util.Iterator;
import java.util.Optional;

public interface ReadOnlyRepository <T, ID> {
    Optional<T> findById(ID id);
    Iterator<T> findAll();
    long count();
}
