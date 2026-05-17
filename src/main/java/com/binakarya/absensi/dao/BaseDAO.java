package com.binakarya.absensi.dao;

import org.bson.conversions.Bson;
import java.util.List;

public interface BaseDAO<T> {
    void save(T entity);                   // Create
    List<T> findAll();                     // Read All
    List<T> findMany(Bson filter);         // Search
    void update(Bson filter, T entity);    // Update
    void delete(Bson filter);              // Delete
}