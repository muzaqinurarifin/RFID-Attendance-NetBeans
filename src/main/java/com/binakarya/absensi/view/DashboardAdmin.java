package com.binakarya.absensi.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * GUI Dasbor Admin menggunakan Java Swing (JFrame)
 * Menggantikan versi HTML web untuk dijalankan langsung di Desktop via NetBeans.
 */
public class DashboardAdmin extends JFrame {

    public DashboardAdmin() {
        // Konfigurasi Window (Frame)
        setTitle("Dasbor Admin Proyek - Bina Karya Konstruksi");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Menengahkan window di layar
        setLayout(new BorderLayout());

        // --- WARNA TEMA (Dark Mode ala Konstruksi) ---
        Color bgColor = Color.decode("#0f172a"); // Slate 900
        Color panelColor = Color.decode("#1e293b"); // Slate 800
        Color textColor = Color.decode("#f8fafc"); // Slate 50
        Color accentColor = Color.decode("#14b8a6"); // Teal 500

        // ==========================================
        // 1. PANEL SIDEBAR (KIRI)
        // ==========================================
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(panelColor);
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        JLabel titleLabel = new JLabel("<html><div style='text-align: center; color: white;'>"
                + "<h2>Bina Karya</h2><p>Manajemen Presensi</p></div></html>");
        sidebar.add(titleLabel);

        // Tombol Menu
        JButton btnDasbor = createMenuButton("Dasbor Analitik", accentColor, bgColor);
        JButton btnPekerja = createMenuButton("Data Pekerja", panelColor, textColor);
        JButton btnLog = createMenuButton("Log Kehadiran", panelColor, textColor);
        
        sidebar.add(btnDasbor);
        sidebar.add(btnPekerja);
        sidebar.add(btnLog);

        // ==========================================
        // 2. PANEL UTAMA (KANAN)
        // ==========================================
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(bgColor);
        JLabel headerTitle = new JLabel("Log Taps Terbaru di Lapangan");
        headerTitle.setForeground(textColor);
        headerTitle.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(headerTitle, BorderLayout.WEST);

        // --- Panel Metrik (Kartu Total Kehadiran) ---
        JPanel metricPanel = new JPanel();
        metricPanel.setBackground(accentColor);
        metricPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel metricLabel = new JLabel("Total Kehadiran Hari Ini: 145 Pekerja");
        metricLabel.setForeground(Color.WHITE);
        metricLabel.setFont(new Font("Arial", Font.BOLD, 16));
        metricPanel.add(metricLabel);
        headerPanel.add(metricPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- Tabel Log Absensi ---
        String[] kolom = {"Waktu Tap", "UID Kartu", "Nama Pekerja / Jabatan", "Status"};
        Object[][] data = {
            {"07:45:12 WIB", "A1:B2:C3:D4", "Budi Utomo (Mandor)", "MASUK"},
            {"07:50:05 WIB", "E5:F6:G7:H8", "Hasanudin (Tukang Besi)", "MASUK"},
            {"08:15:30 WIB", "J9:K0:L1:M2", "Sutejo (Operator Crane)", "TERLAMBAT"}
        };

        DefaultTableModel model = new DefaultTableModel(data, kolom);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setBackground(panelColor);
        table.setForeground(textColor);
        table.setGridColor(bgColor);
        
        // Styling Header Tabel
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(Color.decode("#334155"));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(bgColor);
        scrollPane.setBorder(BorderFactory.createLineBorder(panelColor, 2));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- Tombol Aksi Bawah ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(bgColor);
        JButton btnTambah = new JButton("Tambah Manual");
        btnTambah.setBackground(accentColor);
        btnTambah.setForeground(Color.WHITE);
        JButton btnExport = new JButton("Export Excel");
        
        actionPanel.add(btnTambah);
        actionPanel.add(btnExport);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        // Tambahkan ke Frame Utama
        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    // Method bantuan untuk membuat tombol menu yang seragam
    private JButton createMenuButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder());
        return btn;
    }
}