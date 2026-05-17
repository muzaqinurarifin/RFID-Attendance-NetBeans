package com.binakarya.absensi.service;

import com.binakarya.absensi.dao.GenericDAO;
import com.binakarya.absensi.model.AdminUser;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import java.util.List;

public class AdminService {
    private final GenericDAO<AdminUser> dao;

    public AdminService() {
        this.dao = new GenericDAO<>("admin_users", AdminUser.class);
    }

    // Fungsi cerdas: Jika database admin masih kosong, otomatis buatkan akun default
    public void siapkanAdminAwal() {
        List<AdminUser> daftarAdmin = dao.findAll();
        if (daftarAdmin == null || daftarAdmin.isEmpty()) {
            // Membuat admin default: Username "admin", Password "admin123"
            dao.save(new AdminUser("admin", "admin123"));
            System.out.println("Akun admin default berhasil dibuat di MongoDB!");
        }
    }

    // Fungsi untuk mengecek kecocokan login
    public boolean cekLogin(String username, String password) {
        // Mencari dokumen yang username DAN password-nya cocok
        Bson filter = Filters.and(
                Filters.eq("username", username),
                Filters.eq("passwordHash", password)
        );
        
        List<AdminUser> hasil = dao.findMany(filter);
        return !hasil.isEmpty(); // Jika hasilnya tidak kosong, berarti login berhasil (true)
    }
}