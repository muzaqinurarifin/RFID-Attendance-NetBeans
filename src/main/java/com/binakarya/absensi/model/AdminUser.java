package com.binakarya.absensi.model;

import org.bson.types.ObjectId;

public class AdminUser {
    private ObjectId id;
    private String username;
    private String passwordHash; // Sesuai dengan desain database

    // SYARAT WAJIB MONGODB: Constructor Kosong
    public AdminUser() {}

    public AdminUser(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    // --- GETTER & SETTER ---
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}