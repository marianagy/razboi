package com.razboi.razboi.persistence.user;

import java.util.List;

/**
 * Typical DAO class, deals with database related operations.
 * @param <T> - Entity to use.
 */
public interface DAO<T> {



    void create(T entity);

    void delete(Integer id);

    void update(T entity);

    Integer count();

    List<T> readAll();

    T readOne(Integer id);



}