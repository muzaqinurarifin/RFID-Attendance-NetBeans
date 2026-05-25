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
        adminService.siapkanAdminAwal();

        setTitle("Login - Bina Karya Konstruksi");
        setSize(600, 450); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        inisialisasiUI();
    }

    private void inisialisasiUI() {
        JPanel panelJudul = new JPanel();
        panelJudul.setBackground(Color.decode("#1e293b"));
        panelJudul.setBorder(new EmptyBorder(30, 10, 30, 10)); 
        
        JLabel lblJudul = new JLabel("ADMIN LOGIN", SwingConstants.CENTER);
        lblJudul.setForeground(Color.WHITE);
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 36)); 
        panelJudul.add(lblJudul);
        add(panelJudul, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(2, 2, 20, 30)); 
        panelForm.setBorder(new EmptyBorder(40, 60, 40, 60)); 
        panelForm.setBackground(Color.WHITE);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("SansSerif", Font.BOLD, 18));
        panelForm.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 18));
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
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 20)); 
        btnLogin.setPreferredSize(new Dimension(480, 55)); 
        
        // --- LOGIKA TOMBOL LOGIN ---
        btnLogin.addActionListener(e -> {
            String uname = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());

            if (adminService.cekLogin(uname, pass)) {
                JOptionPane.showMessageDialog(this, "Login Berhasil! Selamat Datang, " + uname);
                
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