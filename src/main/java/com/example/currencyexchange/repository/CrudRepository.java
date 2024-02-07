package com.example.currencyexchange.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    Optional<T> findById(Integer id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(Integer id);
}
