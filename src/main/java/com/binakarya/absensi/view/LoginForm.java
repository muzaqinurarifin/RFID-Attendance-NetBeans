package com.binakarya.absensi.view;

import com.binakarya.absensi.service.AdminService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private AdminService adminService;

    public LoginForm() {
        adminService = new AdminService();
        adminService.siapkanAdminAwal(); // Pastikan ada minimal 1 akun di database

        setTitle("Login - Bina Karya Konstruksi");
        // MEMBESARKAN UKURAN WINDOW JADI 600x450
        setSize(600, 450); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Agar tetap muncul di tengah layar
        setResizable(false);
        setLayout(new BorderLayout());

        inisialisasiUI();
    }

    private void inisialisasiUI() {
        // Panel Atas (Judul)
        JPanel panelJudul = new JPanel();
        panelJudul.setBackground(Color.decode("#1e293b"));
        // Padding atas-bawah dibesarkan
        panelJudul.setBorder(new EmptyBorder(30, 10, 30, 10)); 
        
        JLabel lblJudul = new JLabel("ADMIN LOGIN", SwingConstants.CENTER);
        lblJudul.setForeground(Color.WHITE);
        // Ukuran font judul dibesarkan jadi 36
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 36)); 
        panelJudul.add(lblJudul);
        add(panelJudul, BorderLayout.NORTH);

        // Panel Tengah (Form)
        // Jarak (gap) antar komponen dilebarkan
        JPanel panelForm = new JPanel(new GridLayout(2, 2, 20, 30)); 
        // Margin kiri-kanan dibesarkan
        panelForm.setBorder(new EmptyBorder(40, 60, 40, 60)); 
        panelForm.setBackground(Color.WHITE);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("SansSerif", Font.BOLD, 18)); // Font dibesarkan
        panelForm.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 18)); // Font teks dibesarkan
        panelForm.add(txtUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("SansSerif", Font.BOLD, 18));
        panelForm.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 18));
        panelForm.add(txtPassword);

        add(panelForm, BorderLayout.CENTER);

        // Panel Bawah (Tombol Login)
        JPanel panelBawah = new JPanel();
        panelBawah.setBackground(Color.WHITE);
        panelBawah.setBorder(new EmptyBorder(0, 60, 40, 60)); 
        
        btnLogin = new JButton("Login ke Dashboard");
        btnLogin.setBackground(Color.decode("#3b82f6"));
        btnLogin.setForeground(Color.WHITE);
        // Ukuran font tombol dibesarkan jadi 20
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 20)); 
        // Ukuran tombolnya sendiri dilebarkan
        btnLogin.setPreferredSize(new Dimension(480, 55)); 
        
        // --- LOGIKA TOMBOL LOGIN ---
        btnLogin.addActionListener(e -> {
            String uname = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());

            if (adminService.cekLogin(uname, pass)) {
                JOptionPane.showMessageDialog(this, "Login Berhasil! Selamat Datang, " + uname);
                
                // Buka Dashboard dan tutup jendela login
                new DashboardAdmin().setVisible(true);
                this.dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }
        });

        panelBawah.add(btnLogin);
        add(panelBawah, BorderLayout.SOUTH);
    }
}