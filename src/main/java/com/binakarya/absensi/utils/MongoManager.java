package com.binakarya.absensi.utils;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class MongoManager {
    private static MongoClient mongoClient;
    private static final String DATABASE_NAME = "binakarya_absensi";

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
            );

            mongoClient = MongoClients.create("mongodb://localhost:27017");

            return mongoClient.getDatabase(DATABASE_NAME).withCodecRegistry(pojoCodecRegistry);
        }
        return mongoClient.getDatabase(DATABASE_NAME);
    }
}