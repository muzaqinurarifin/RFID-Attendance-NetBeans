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

     //database admin masih kosong, otomatis buatkan akun default
    public void siapkanAdminAwal() {
        List<AdminUser> daftarAdmin = dao.findAll();
        if (daftarAdmin == null || daftarAdmin.isEmpty()) {
            dao.save(new AdminUser("admin", "admin123"));
            System.out.println("Akun admin default berhasil dibuat di MongoDB!");
        }
    }

    // fungsi untuk mengecek kecocokan login
    public boolean cekLogin(String username, String password) {
        Bson filter = Filters.and(
                Filters.eq("username", username),
                Filters.eq("passwordHash", password)
        );
        
        List<AdminUser> hasil = dao.findMany(filter);
        return !hasil.isEmpty();
    }
}