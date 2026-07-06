package com.binakarya.absensi.view;

import com.binakarya.absensi.service.AdminService;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.border.LineBorder;

public class LoginForm extends JFrame {

    private final AdminService adminService;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginForm() {
        this.adminService = new AdminService();
        this.adminService.siapkanAdminAwal();

        setTitle("Login - Sistem Presensi Bina Karya");
        setSize(900, 550); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new GridLayout(1, 2)); 

        //Panel Kiri
        JPanel leftPanel = createLeftImagePanel();
        
        //Panel Kanan
        JPanel rightPanel = createRightFormPanel();

        add(leftPanel);
        add(rightPanel);
    }

    //panel sebelah kiri
    private JPanel createLeftImagePanel() {
        // Kita menggunakan custom JPanel untuk menggambar (paint) background
        JPanel panel = new JPanel() {
            private Image bgImage;

            {
                try {
                    URL imgUrl = getClass().getResource("/images/images (17).jpeg");
                    if (imgUrl != null) {
                        bgImage = ImageIO.read(imgUrl);
                    }
                } catch (Exception e) {
                    System.out.println("Gambar background tidak ditemukan, menggunakan warna fallback.");
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                if (bgImage != null) {
                    g2d.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    GradientPaint gp = new GradientPaint(0, 0, Color.decode("#0f172a"), getWidth(), getHeight(), Color.decode("#1e293b"));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("SansSerif", Font.BOLD, 28));
                    g2d.drawString("BINA KARYA", 50, getHeight() / 2 - 20);
                    
                    g2d.setFont(new Font("SansSerif", Font.PLAIN, 16));
                    g2d.drawString("Sistem Manajemen Presensi", 50, getHeight() / 2 + 10);
                }
            }
        };
        return panel;
    }

    // panel sebelah kanan
    private JPanel createRightFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 10, 30); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // logo
        JLabel lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            // Memanggil gambar logo dari folder resources
            java.net.URL logoUrl = getClass().getResource("/images/Gemini_Generated_Image_min8btmin8btmin8.png");
            if (logoUrl != null) {
                ImageIcon originalIcon = new ImageIcon(logoUrl);
                Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(scaledImage));
            } else {
                lblLogo.setText("🏢"); 
                lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
            }
        } catch (Exception e) {
            lblLogo.setText("🏢");
            lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        }
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(10, 30, 5, 30);
        panel.add(lblLogo, gbc);
 
        JLabel lblTitle = new JLabel("Selamat Datang", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setForeground(Color.decode("#1f2937"));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 30, 5, 30);
        panel.add(lblTitle, gbc);

        JLabel lblSub = new JLabel("Silakan masuk ke akun admin Anda", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSub.setForeground(Color.decode("#6b7280"));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 30, 30, 30); // Jarak agak jauh sebelum masuk ke input
        panel.add(lblSub, gbc);

        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblUser.setForeground(Color.decode("#374151"));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 30, 5, 30);
        panel.add(lblUser, gbc);

        txtUsername = new JTextField(20);
        txtUsername.setPreferredSize(new Dimension(250, 40));
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.decode("#d1d5db"), 1, true),
                new EmptyBorder(5, 10, 5, 10)));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 30, 15, 30);
        panel.add(txtUsername, gbc);

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblPass.setForeground(Color.decode("#374151"));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 30, 5, 30);
        panel.add(lblPass, gbc);

        txtPassword = new JPasswordField(20);
        txtPassword.setPreferredSize(new Dimension(250, 40));
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.decode("#d1d5db"), 1, true),
                new EmptyBorder(5, 10, 5, 10)));
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 30, 25, 30);
        panel.add(txtPassword, gbc);

        // 8. Tombol Login
        btnLogin = new JButton("Masuk Sistem");
        btnLogin.setBackground(Color.decode("#3b82f6")); 
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(250, 45));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 30, 30, 30);
        panel.add(btnLogin, gbc);

        // Event Tombol Login
        btnLogin.addActionListener((java.awt.event.ActionEvent e) -> prosesLogin());
        txtPassword.addActionListener(e -> prosesLogin());

        return panel;
    }
    
    
    //Logika Validasi Login melalui Service
    private void prosesLogin() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        if (user.isBlank() || pass.isBlank()) {
            JOptionPane.showMessageDialog(this, "Username dan Password tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Cek login ke database MongoDB
        boolean isLolos = adminService.cekLogin(user, pass);

        if (isLolos) {

            new DashboardAdmin().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username atau Password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}