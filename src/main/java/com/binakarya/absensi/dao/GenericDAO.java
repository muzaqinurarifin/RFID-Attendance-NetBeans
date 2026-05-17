package com.binakarya.absensi.dao;

import com.binakarya.absensi.utils.MongoManager;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class GenericDAO<T> implements BaseDAO<T> {
    
    private final MongoCollection<T> collection;

    // PERBAIKAN: Sekarang menerima nama koleksi secara manual agar tidak membuat koleksi baru otomatis
    public GenericDAO(String collectionName, Class<T> clazz) {
        MongoDatabase database = MongoManager.getDatabase();
        
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        // Menghubungkan langsung ke nama koleksi yang ditentukan
        this.collection = database.getCollection(collectionName, clazz).withCodecRegistry(pojoCodecRegistry);
    }

    @Override
    public void save(T entity) {
        collection.insertOne(entity);
    }

    @Override
    public List<T> findAll() {
        return collection.find().into(new ArrayList<>());
    }

    @Override
    public List<T> findMany(Bson filter) {
        return collection.find(filter).into(new ArrayList<>());
    }

    @Override
    public void update(Bson filter, T entity) {
        collection.replaceOne(filter, entity);
    }

    @Override
    public void delete(Bson filter) {
        collection.deleteOne(filter);
    }
}