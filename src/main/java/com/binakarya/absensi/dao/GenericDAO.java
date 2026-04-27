package com.binakarya.absensi.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * Sesuai Standar Pertemuan 4: Implementasi GenericDAO
 * Memisahkan logika Database agar tidak terjadi redundansi baris kode (DRY Principle).
 */
public class GenericDAO<T> implements BaseDAO<T> {
    
    private Class<T> type;
    
    // Database Sementara (Mock List) sebelum terhubung ke MongoDB asli (Pertemuan 5)
    private List<T> mockDatabase;

    public GenericDAO(Class<T> type) {
        this.type = type;
        this.mockDatabase = new ArrayList<>();
    }

    @Override
    public void save(T entity) {
        mockDatabase.add(entity);
        System.out.println("LOG SYSTEM: Entitas " + type.getSimpleName() + " berhasil disimpan ke memori lokal.");
    }

    @Override
    public T findById(String id) {
        // TODO: Implementasi query baca database asli
        return null; 
    }

    @Override
    public List<T> findAll() {
        return mockDatabase;
    }

    @Override
    public void update(T entity) {
        // TODO: Implementasi pembaruan data
    }

    @Override
    public void delete(String id) {
        // TODO: Implementasi penghapusan data
    }
}