package com.binakarya.absensi.view;

import com.binakarya.absensi.model.Karyawan;
import com.binakarya.absensi.service.KaryawanService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DashboardAdmin extends JFrame {

    private final KaryawanService karyawanService;

    // Komponen CardLayout & Sidebar
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Komponen Form Data Karyawan
    private JTextField txtUID, txtNama, txtID, txtCari;
    private JComboBox<String> cbDept;
    private JButton btnSimpan, btnUpdate, btnRefresh, btnCari;
    private JPanel panelKartu;

    public DashboardAdmin() {
        karyawanService = new KaryawanService();

        setTitle("Bina Karya Konstruksi - Dashboard Presensi");
        setSize(1300, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        buatSidebar();

        mainPanel.add(buatHalamanBeranda(), "BERANDA");
        mainPanel.add(buatHalamanKaryawan(), "KARYAWAN");
        mainPanel.add(buatHalamanLog(), "LOG");

        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "BERANDA");
    }

    private void buatSidebar() {
        JPanel sidebar = new JPanel(new GridLayout(10, 1, 0, 10));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(Color.decode("#1e293b")); 
        sidebar.setBorder(new EmptyBorder(30, 15, 30, 15));

        JLabel lblLogo = new JLabel("BINA KARYA", SwingConstants.CENTER);
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 24));
        sidebar.add(lblLogo);

        JButton btnMenu1 = buatTombolMenu("Dashboard Utama");
        JButton btnMenu2 = buatTombolMenu("Data Karyawan");
        JButton btnMenu3 = buatTombolMenu("Log Presensi");

        btnMenu1.addActionListener(e -> cardLayout.show(mainPanel, "BERANDA"));
        btnMenu2.addActionListener(e -> {
            cardLayout.show(mainPanel, "KARYAWAN");
            resetForm(); 
            refreshData(); // Pastikan data langsung ter-load saat menu diklik
        });
        btnMenu3.addActionListener(e -> cardLayout.show(mainPanel, "LOG"));

        sidebar.add(btnMenu1);
        sidebar.add(btnMenu2);
        sidebar.add(btnMenu3);
        
        JButton btnLogout = buatTombolMenu("Logout");
        btnLogout.setBackground(Color.decode("#ef4444")); // Warna merah agar kontras
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Apakah Anda yakin ingin logout?", "Konfirmasi Logout", 
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginForm().setVisible(true); // Buka kembali form login
                this.dispose(); // Tutup dashboard admin
            }
        });
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);
    }

    private JButton buatTombolMenu(String teks) {
        JButton btn = new JButton(teks);
        btn.setBackground(Color.decode("#334155"));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btn.setFocusPainted(false);
        return btn;
    }

    private JPanel buatHalamanBeranda() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.decode("#f8fafc"));

        JLabel lblWelcome = new JLabel("Selamat Datang di Sistem Presensi", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 36));

        JLabel lblJam = new JLabel("", SwingConstants.CENTER);
        lblJam.setFont(new Font("SansSerif", Font.BOLD, 80));
        lblJam.setForeground(Color.decode("#14b8a6")); 

        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy | HH:mm:ss");
            lblJam.setText(sdf.format(new Date()));
        });
        timer.start();

        JPanel panelTengah = new JPanel(new GridLayout(2, 1));
        panelTengah.setOpaque(false);
        panelTengah.add(lblWelcome);
        panelTengah.add(lblJam);

        panel.add(panelTengah, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buatHalamanKaryawan() {
        JPanel panelUtama = new JPanel(new BorderLayout());

        // --- Panel Atas (Form) ---
        JPanel panelAtas = new JPanel(new BorderLayout(10, 10));
        panelAtas.setBackground(Color.decode("#e0f2fe"));
        panelAtas.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel panelForm = new JPanel(new GridLayout(2, 4, 10, 10));
        panelForm.setOpaque(false);

        panelForm.add(new JLabel("ID Karyawan (Auto):"));
        txtID = new JTextField();
        txtID.setEnabled(false); 
        panelForm.add(txtID);

        panelForm.add(new JLabel("UID Kartu RFID:"));
        txtUID = new JTextField();
        panelForm.add(txtUID);

        panelForm.add(new JLabel("Nama Lengkap:"));
        txtNama = new JTextField();
        panelForm.add(txtNama);

        panelForm.add(new JLabel("Departemen:"));
        String[] depts = {"Operasional Proyek", "Teknologi & Informasi", "Manajemen Risiko", "Logistik & Gudang"};
        cbDept = new JComboBox<>(depts);
        panelForm.add(cbDept);

        // Panel Tombol
        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAksi.setOpaque(false);
        btnSimpan = new JButton("Save"); btnSimpan.setBackground(Color.decode("#3b82f6")); btnSimpan.setForeground(Color.WHITE);
        btnUpdate = new JButton("Update"); btnUpdate.setBackground(Color.decode("#f59e0b")); btnUpdate.setForeground(Color.WHITE);
        btnRefresh = new JButton("Reset Form"); btnRefresh.setBackground(Color.decode("#22c55e")); btnRefresh.setForeground(Color.WHITE);
        
        btnUpdate.setEnabled(false);
        panelAksi.add(btnSimpan); panelAksi.add(btnUpdate); panelAksi.add(btnRefresh);

        // Panel Pencarian
        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelCari.setOpaque(false);
        txtCari = new JTextField(15);
        btnCari = new JButton("Cari");
        panelCari.add(txtCari); panelCari.add(btnCari);

        JPanel panelTengahAtas = new JPanel(new BorderLayout());
        panelTengahAtas.setOpaque(false);
        panelTengahAtas.add(panelAksi, BorderLayout.WEST);
        panelTengahAtas.add(panelCari, BorderLayout.EAST);

        panelAtas.add(panelForm, BorderLayout.NORTH);
        panelAtas.add(panelTengahAtas, BorderLayout.SOUTH);

        // --- Panel Bawah (Grid Kartu dengan Wrapper Ajaib) ---
        panelKartu = new JPanel(new GridLayout(0, 3, 15, 15)); // 0 baris, 3 kolom
        panelKartu.setBackground(Color.decode("#cbd5e1"));
        panelKartu.setBorder(new EmptyBorder(15, 15, 15, 15));

        // INI DIA TRIKNYA: Wrapper Panel
        JPanel panelBungkus = new JPanel(new BorderLayout());
        panelBungkus.setBackground(Color.decode("#cbd5e1"));
        // Memaku grid kartu ke arah NORTH agar tingginya pas dan tidak melar
        panelBungkus.add(panelKartu, BorderLayout.NORTH); 

        JScrollPane scrollPane = new JScrollPane(panelBungkus); // Yang dimasukkan ke scroll adalah bungkusnya
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        panelUtama.add(panelAtas, BorderLayout.NORTH);
        panelUtama.add(scrollPane, BorderLayout.CENTER);

        setupEventKaryawan(); 
        return panelUtama;
    }

    private void setupEventKaryawan() {
        btnSimpan.addActionListener(e -> {
            Karyawan k = new Karyawan(txtUID.getText(), txtID.getText(), txtNama.getText(), cbDept.getSelectedItem().toString());
            karyawanService.tambahKaryawan(k);
            JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan!");
            resetForm();
            refreshData();
        });

        btnUpdate.addActionListener(e -> {
            Karyawan k = new Karyawan(txtUID.getText(), txtID.getText(), txtNama.getText(), cbDept.getSelectedItem().toString());
            karyawanService.updateKaryawan(k);
            JOptionPane.showMessageDialog(this, "Data Berhasil Diperbarui!");
            resetForm();
            refreshData();
        });

        btnCari.addActionListener(e -> renderKartu(karyawanService.cariKaryawan(txtCari.getText())));
        btnRefresh.addActionListener(e -> { resetForm(); refreshData(); });
    }

    private JPanel buatHalamanLog() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel("Halaman Log Presensi (Dalam Pengembangan...)", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 24));
        panel.add(lbl, BorderLayout.CENTER);
        return panel;
    }

    private void refreshData() {
        renderKartu(karyawanService.ambilSemuaKaryawan());
    }

    private void renderKartu(List<Karyawan> list) {
        panelKartu.removeAll();
        for (Karyawan k : list) {
            panelKartu.add(buatKartu(k));
        }
        
        // Refresh panel pembungkus agar scroll mendeteksi perubahan ukuran
        panelKartu.revalidate();
        panelKartu.repaint();
    }

    private JPanel buatKartu(Karyawan k) {
        JPanel kartu = new JPanel(new GridLayout(4, 1, 0, 5));
        kartu.setBackground(Color.decode("#c59b6d"));
        kartu.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.decode("#b45309"), 2), new EmptyBorder(10, 10, 10, 10)));
        
        // Membatasi tinggi maksimum kartu agar tidak pernah lebih dari ukuran ini
        kartu.setPreferredSize(new Dimension(300, 160)); 

        JLabel lblNama = new JLabel("Nama: " + k.getNamaLengkap());
        lblNama.setForeground(Color.WHITE); lblNama.setFont(new Font("SansSerif", Font.BOLD, 14));
        JLabel lblID = new JLabel("ID Karyawan: " + k.getIdKaryawan());
        lblID.setForeground(Color.WHITE);
        JLabel lblDept = new JLabel("Departemen: " + k.getDepartemen());
        lblDept.setForeground(Color.WHITE);

        JPanel panelTombol = new JPanel(new GridLayout(1, 2, 10, 0));
        panelTombol.setOpaque(false);

        JButton btnEdit = new JButton("Edit"); btnEdit.setBackground(Color.decode("#facc15"));
        JButton btnHapus = new JButton("Delete"); btnHapus.setBackground(Color.decode("#991b1b")); btnHapus.setForeground(Color.WHITE);

        btnEdit.addActionListener(e -> {
            txtUID.setText(k.getUidRfid()); txtNama.setText(k.getNamaLengkap());
            txtID.setText(k.getIdKaryawan()); cbDept.setSelectedItem(k.getDepartemen());
            btnSimpan.setEnabled(false); btnUpdate.setEnabled(true);
        });

        btnHapus.addActionListener(e -> {
            int choice = JOptionPane.showOptionDialog(this,
                    "Hapus " + k.getNamaLengkap() + "?", "Konfirmasi",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,
                    new String[]{"Ya, Hapus", "Batal"}, "Batal");
            if (choice == JOptionPane.YES_OPTION) {
                karyawanService.hapusKaryawan(k.getIdKaryawan());
                refreshData();
            }
        });

        panelTombol.add(btnEdit); panelTombol.add(btnHapus);
        kartu.add(lblNama); kartu.add(lblID); kartu.add(lblDept); kartu.add(panelTombol);

        return kartu;
    }

    private void resetForm() {
        txtID.setText(karyawanService.generateNewId()); 
        txtUID.setText("");
        txtNama.setText("");
        txtCari.setText("");
        cbDept.setSelectedIndex(0);

        btnSimpan.setEnabled(true);
        btnUpdate.setEnabled(false);
    }
}