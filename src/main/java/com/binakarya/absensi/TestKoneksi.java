package com.binakarya.absensi;

import com.binakarya.absensi.utils.MongoManager; 
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class TestKoneksi {
    public static void main(String[] args) {
        System.out.println("=== MEMULAI HANDSHAKE TEST MONGODB ===");
        
        try {
            // Singleton MongoManager
            MongoDatabase db = MongoManager.getDatabase();
            
            db.runCommand(new Document("ping", 1));
            
            System.out.println("\n[✔] TEST KONEKSI BERHASIL (CONNECT SUCCESS)!");
            System.out.println("[✔] Terhubung ke Database : " + db.getName());
            
        } catch (Exception e) {
 
            System.err.println("\n[X] KONEKSI GAGAL!");
            System.err.println("Pastikan service MongoDB sudah berjalan di komputermu.");
            e.printStackTrace();
        }
    }
}