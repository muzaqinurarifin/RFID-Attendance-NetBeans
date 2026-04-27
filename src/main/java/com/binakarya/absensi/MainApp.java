package com.binakarya.absensi;

import com.binakarya.absensi.dao.GenericDAO;
import com.binakarya.absensi.model.Karyawan;
import com.binakarya.absensi.view.DashboardAdmin;
import javax.swing.SwingUtilities;

/**
 * Titik mulai (Entry Point) program.
 */
public class MainApp {
    public static void main(String[] args) {
        System.out.println("===============================================");
        System.out.println(" SISTEM PRESENSI BINA KARYA KONSTRUKSI AKTIF");
        System.out.println("===============================================");
        
        // 1. Tes Koneksi DAO (Berjalan di background/console)
        GenericDAO<Karyawan> pekerjaDAO = new GenericDAO<>(Karyawan.class);
        Karyawan pekerjaBaru = new Karyawan("A1:B2:C3:D4", "PKJ-001", "Budi Utomo", "Mandor");
        pekerjaDAO.save(pekerjaBaru);
        System.out.println("Total pekerja terdaftar di sistem: " + pekerjaDAO.findAll().size());
        
        // 2. Memanggil GUI Desktop (JFrame)
        // Menggunakan SwingUtilities agar GUI berjalan dengan lancar (Thread-Safe)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Membuat objek DashboardAdmin dan menampilkannya ke layar
                DashboardAdmin dashboard = new DashboardAdmin();
                dashboard.setVisible(true);
            }
        });
    }
}