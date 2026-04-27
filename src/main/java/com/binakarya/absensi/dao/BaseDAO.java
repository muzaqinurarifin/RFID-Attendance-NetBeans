package com.binakarya.absensi.dao;

import java.util.List;

/**
 * Sesuai Standar Pertemuan 4: Blueprint Generic Programming (BaseDAO)
 * Interface ini menetapkan kontrak wajib untuk manipulasi data (CRUD).
 */
public interface BaseDAO<T> {
    void save(T entity);
    T findById(String id);
    List<T> findAll();
    void update(T entity);
    void delete(String id);
}