package com.binakarya.absensi.service;

import com.binakarya.absensi.dao.GenericDAO;
import com.binakarya.absensi.model.AdminUser;
import com.binakarya.absensi.security.SecurityUtils;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import java.util.List;

public class AdminService {
    private final GenericDAO<AdminUser> dao;

    public AdminService() {
        // Mengarah ke koleksi 'admin_users' sesuai gambar MongoDB-mu
        this.dao = new GenericDAO<>("admin_users", AdminUser.class);
    }

    // ==========================================
    // FUNGSI INIT & AUTENTIKASI
    // ==========================================
    public void siapkanAdminAwal() {
        List<AdminUser> daftarAdmin = dao.findAll();
        if (daftarAdmin == null || daftarAdmin.isEmpty()) {
            String passwordHash = SecurityUtils.hashPassword("admin123");
            dao.save(new AdminUser("admin", passwordHash));
            System.out.println("LOG: Akun admin default berhasil dibuat di MongoDB!");
        }
    }

    public boolean cekLogin(String username, String password) {
        String hash = SecurityUtils.hashPassword(password);
        Bson filter = Filters.and(
                Filters.eq("username", username),
                Filters.or(
                        Filters.eq("passwordHash", hash),
                        Filters.eq("passwordHash", password) // Backward compatibility
                )
        );
        List<AdminUser> hasil = dao.findMany(filter);
        return !hasil.isEmpty();
    }

    // ==========================================
    // FUNGSI CRUD MANAJEMEN ADMIN
    // ==========================================
    
    // CREATE
    public void tambahAdmin(String username, String passwordPlain) {
        String passwordHash = SecurityUtils.hashPassword(passwordPlain);
        dao.save(new AdminUser(username, passwordHash));
    }

    // READ
    public List<AdminUser> ambilSemuaAdmin() {
        return dao.findAll();
    }

    // UPDATE
    public void updateAdmin(String usernameLama, String usernameBaru, String passwordPlainBaru) {
        Bson filter = Filters.eq("username", usernameLama);
        List<AdminUser> dataLama = dao.findMany(filter);
        
        if (!dataLama.isEmpty()) {
            AdminUser adminUpdate = dataLama.get(0);
            adminUpdate.setUsername(usernameBaru);
            
            // Hanya update password jika form password diisi (tidak kosong)
            if (passwordPlainBaru != null && !passwordPlainBaru.isBlank()) {
                adminUpdate.setPasswordHash(SecurityUtils.hashPassword(passwordPlainBaru));
            }
            
            dao.update(filter, adminUpdate);
        }
    }

    // DELETE
    public void hapusAdmin(String username) {
        Bson filter = Filters.eq("username", username);
        dao.delete(filter);
    }
}