package com.binakarya.absensi;

import com.binakarya.absensi.view.LoginForm;
import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("===============================================");
        System.out.println(" SISTEM PRESENSI BINA KARYA KONSTRUKSI AKTIF");
        System.out.println("===============================================");
        
        // Memanggil Form Login sebagai pintu masuk pertama
        SwingUtilities.invokeLater(() -> {
            LoginForm login = new LoginForm();
            login.setVisible(true);
        });
    }
}