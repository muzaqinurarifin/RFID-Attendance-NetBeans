package com.binakarya.absensi.view;

import com.binakarya.absensi.model.AdminUser;
import com.binakarya.absensi.model.Karyawan;
import com.binakarya.absensi.model.LogAbsensi;
import com.binakarya.absensi.security.SecurityUtils;
import com.binakarya.absensi.service.AdminService;
import com.binakarya.absensi.service.KaryawanService;
import com.binakarya.absensi.service.LogAbsensiService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardAdmin extends JFrame {
    
    // Lapisan Service
    private final KaryawanService karyawanService;
    private final LogAbsensiService logAbsensiService;
    private final AdminService adminService;
    
    // Komponen Layout Utama
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Komponen Karyawan
    private JTextField txtUID, txtID, txtNama, txtCariKaryawan;
    private JComboBox<String> cbDept;
    private JButton btnSimpanK, btnUpdateK, btnResetK, btnCariK;
    private JPanel panelKartuKaryawan;
    private String uidKaryawanTerpilih = ""; // Tracker untuk menampung Hash UID lama

    // Komponen Admin
    private JTextField txtUsernameAdmin, txtCariAdmin;
    private JPasswordField txtPasswordAdmin;
    private JButton btnSimpanA, btnUpdateA, btnResetA, btnCariA;
    private JPanel panelKartuAdmin;
    private String usernameAdminTerpilih = ""; // Tracker untuk edit admin

    // Komponen Simulasi Tap RFID (di Beranda)
    private JTextField txtUidTap;
    
    // Komponen Log Presensi (Tabel)
    private JTable tableLog;
    private DefaultTableModel modelLog;
    private JTextField txtCariLog;

    public DashboardAdmin() {
        karyawanService = new KaryawanService();
        logAbsensiService = new LogAbsensiService();
        adminService = new AdminService();
        
        setTitle("Bina Karya Konstruksi - Dashboard Presensi");
        setSize(1300, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.decode("#d8e2ea")); // Latar belakang utama (light blue-grey)

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setOpaque(false);

        buatSidebar();

        // Mendaftarkan Halaman ke dalam CardLayout
        mainPanel.add(buatHalamanBeranda(), "BERANDA");
        mainPanel.add(buatHalamanKaryawan(), "KARYAWAN");
        mainPanel.add(buatHalamanAdmin(), "ADMIN");
        mainPanel.add(buatHalamanLog(), "LOG");

        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "BERANDA");
    }

    // ==========================================
    // 1. SIDEBAR NAVIGATION
    // ==========================================
    private void buatSidebar() {
        JPanel sidebar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(Color.decode("#1f2937")); // Dark Navy
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel lblLogo = new JLabel("BINA KARYA", SwingConstants.CENTER);
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblLogo.setPreferredSize(new Dimension(230, 60));
        sidebar.add(lblLogo);

        JButton btnMenu1 = buatTombolMenu("Dashboard Utama");
        JButton btnMenu2 = buatTombolMenu("Data Karyawan");
        JButton btnMenu3 = buatTombolMenu("Manajemen Admin");
        JButton btnMenu4 = buatTombolMenu("Log Presensi");

        btnMenu1.addActionListener(e -> cardLayout.show(mainPanel, "BERANDA"));
        btnMenu2.addActionListener(e -> {
            cardLayout.show(mainPanel, "KARYAWAN");
            refreshDataKaryawan();
        });
        btnMenu3.addActionListener(e -> {
            cardLayout.show(mainPanel, "ADMIN");
            refreshDataAdmin();
        });
        btnMenu4.addActionListener(e -> {
            cardLayout.show(mainPanel, "LOG");
            refreshDataLog();
        });

        sidebar.add(btnMenu1);
        sidebar.add(btnMenu2);
        sidebar.add(btnMenu3);
        sidebar.add(btnMenu4);
        
        JButton btnLogout = buatTombolMenu("Logout");
        btnLogout.setBackground(Color.decode("#ef4444")); // Red Logout
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginForm().setVisible(true);
                this.dispose();
            }
        });
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);
    }

    // ==========================================
    // 2. HALAMAN BERANDA (DENGAN SIMULASI TAP RFID)
    // ==========================================
    private JPanel buatHalamanBeranda() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.decode("#d8e2ea"));
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Area Jam & Welcome
        JPanel panelTengah = new JPanel(new GridLayout(2, 1));
        panelTengah.setOpaque(false);
        
        JLabel lblWelcome = new JLabel("Sistem Presensi Bina Karya", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblWelcome.setForeground(Color.decode("#1f2937"));

        JLabel lblJam = new JLabel("", SwingConstants.CENTER);
        lblJam.setFont(new Font("SansSerif", Font.BOLD, 72));
        lblJam.setForeground(Color.decode("#3b82f6")); 

        Timer timer = new Timer(1000, e -> {
            lblJam.setText(new SimpleDateFormat("dd MMMM yyyy | HH:mm:ss").format(new Date()));
        });
        timer.start();

        panelTengah.add(lblWelcome);
        panelTengah.add(lblJam);

        // Area Simulasi Tap RFID
        JPanel panelTap = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        panelTap.setBackground(Color.decode("#ffffff"));
        panelTap.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.decode("#cbd5e1"), 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblTap = new JLabel("Simulasi Tap Kartu RFID (UID):");
        lblTap.setFont(new Font("SansSerif", Font.BOLD, 18));
        
        txtUidTap = new JTextField(20);
        txtUidTap.setFont(new Font("SansSerif", Font.BOLD, 18));
        
        JButton btnTap = createButton("Simulasikan Tap", "#3b82f6");
        btnTap.setPreferredSize(new Dimension(200, 40));

        panelTap.add(lblTap);
        panelTap.add(txtUidTap);
        panelTap.add(btnTap);

        // --- LOGIKA TAP RFID (KEAMANAN HASH) ---
        btnTap.addActionListener(e -> {
            String uidInput = txtUidTap.getText().trim();
            if (uidInput.isEmpty()) {
                showSlidingNotification("Masukkan UID Kartu terlebih dahulu!", false);
                return;
            }

            // HASHING INPUT: Mengonversi UID mentah menjadi Hash 64 Karakter sebelum dicek ke DB
            String hashedUID = SecurityUtils.hashPassword(uidInput);
            Karyawan k = karyawanService.cariKaryawanByUid(hashedUID);
            
            if (k == null) {
                showSlidingNotification("Akses Ditolak! Kartu tidak terdaftar.", false);
                txtUidTap.setText("");
                return;
            }

            String namaPekerja = k.getNamaLengkap();
            String status = logAbsensiService.tentukanStatus(hashedUID);
            String idLogBaru = logAbsensiService.generateNewId();
            
            // Simpan log menggunakan Hash UID demi keamanan data
            logAbsensiService.tambahLog(new LogAbsensi(idLogBaru, hashedUID, status));
            
            showSlidingNotification("Berhasil! " + namaPekerja + " (" + status + ")", true);
            txtUidTap.setText(""); 
            refreshDataLog();
        });

        panel.add(panelTengah, BorderLayout.CENTER);
        panel.add(panelTap, BorderLayout.SOUTH);
        
        return panel;
    }

    // ==========================================
    // 3. HALAMAN DATA KARYAWAN (CARD LAYOUT)
    // ==========================================
    private JPanel buatHalamanKaryawan() {
        JPanel panelUtama = new JPanel(new BorderLayout(10, 10));
        panelUtama.setBackground(Color.decode("#d8e2ea"));
        panelUtama.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- Panel Atas (Form Input & Pencarian) ---
        JPanel panelAtas = new JPanel(new BorderLayout(5, 10));
        panelAtas.setOpaque(false);

        // Form Grid
        JPanel panelForm = new JPanel(new GridLayout(2, 4, 10, 10));
        panelForm.setBackground(Color.decode("#e6eff5"));
        panelForm.setBorder(new EmptyBorder(15, 15, 15, 15));

        panelForm.add(createLabelHitam("ID Karyawan (Auto):", 14));
        txtID = new JTextField(); txtID.setEnabled(false); panelForm.add(txtID);

        panelForm.add(createLabelHitam("UID RFID (Kosongkan saat Edit jika sama):", 13));
        txtUID = new JTextField(); panelForm.add(txtUID);

        panelForm.add(createLabelHitam("Nama Lengkap:", 14));
        txtNama = new JTextField(); panelForm.add(txtNama);

        panelForm.add(createLabelHitam("Departemen:", 14));
        String[] depts = {"Operasional Proyek", "Teknologi & Informasi", "Manajemen Risiko", "Logistik & Gudang"};
        cbDept = new JComboBox<>(depts); panelForm.add(cbDept);

        // Aksi Bawah Form
        JPanel panelAksi = new JPanel(new BorderLayout());
        panelAksi.setOpaque(false);
        
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelTombol.setOpaque(false);
        btnSimpanK = createButton("Save", "#3b82f6"); 
        btnUpdateK = createButton("Update", "#f59e0b"); btnUpdateK.setEnabled(false);
        btnResetK = createButton("Reset Form", "#22c55e");
        panelTombol.add(btnSimpanK); panelTombol.add(btnUpdateK); panelTombol.add(btnResetK);

        JPanel panelPencarian = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelPencarian.setOpaque(false);
        txtCariKaryawan = new JTextField(15);
        btnCariK = createButton("Cari", "#e2e8f0"); 
        btnCariK.setForeground(Color.BLACK); btnCariK.setBorder(new LineBorder(Color.GRAY));
        panelPencarian.add(txtCariKaryawan); panelPencarian.add(btnCariK);

        panelAksi.add(panelTombol, BorderLayout.WEST);
        panelAksi.add(panelPencarian, BorderLayout.EAST);

        panelAtas.add(panelForm, BorderLayout.NORTH);
        panelAtas.add(panelAksi, BorderLayout.SOUTH);

        // --- Panel Bawah (Grid Card Layout) ---
        panelKartuKaryawan = new JPanel(new GridLayout(0, 3, 15, 15));
        panelKartuKaryawan.setBackground(Color.decode("#d8e2ea"));
        
        // Wrapper trik agar kartu tidak melar
        JPanel panelBungkus = new JPanel(new BorderLayout());
        panelBungkus.setBackground(Color.decode("#d8e2ea"));
        panelBungkus.add(panelKartuKaryawan, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(panelBungkus);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panelUtama.add(panelAtas, BorderLayout.NORTH);
        panelUtama.add(scrollPane, BorderLayout.CENTER);

        // --- Event Karyawan ---
        btnSimpanK.addActionListener(e -> {
            String rawUid = txtUID.getText().trim();
            if (rawUid.isEmpty() || txtNama.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "UID Kartu dan Nama Lengkap wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Terapkan Hashing pada UID Karyawan Baru
            String hashedUid = SecurityUtils.hashPassword(rawUid);
            karyawanService.tambahKaryawan(new Karyawan(hashedUid, txtID.getText(), txtNama.getText(), cbDept.getSelectedItem().toString()));
            JOptionPane.showMessageDialog(this, "Data Karyawan Tersimpan!"); 
            resetFormKaryawan(); refreshDataKaryawan();
        });
        
        btnUpdateK.addActionListener(e -> {
            if (txtNama.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama Lengkap wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String rawUid = txtUID.getText().trim();
            // Jika UID tidak diketik ulang, gunakan UID lama (hash lama). Jika diisi baru, Hash lagi!
            String finalUid = rawUid.isEmpty() ? uidKaryawanTerpilih : SecurityUtils.hashPassword(rawUid);
            
            karyawanService.updateKaryawan(new Karyawan(finalUid, txtID.getText(), txtNama.getText(), cbDept.getSelectedItem().toString()));
            JOptionPane.showMessageDialog(this, "Data Karyawan Diperbarui!"); 
            resetFormKaryawan(); refreshDataKaryawan();
        });
        
        btnResetK.addActionListener(e -> resetFormKaryawan());
        
        btnCariK.addActionListener(e -> {
            refreshDataKaryawan(); 
        });

        resetFormKaryawan();
        return panelUtama;
    }

    private void refreshDataKaryawan() {
        panelKartuKaryawan.removeAll();
        for (Karyawan k : karyawanService.ambilSemuaKaryawan()) {
            panelKartuKaryawan.add(buatKartuKaryawan(k));
        }
        panelKartuKaryawan.revalidate();
        panelKartuKaryawan.repaint();
    }

    private JPanel buatKartuKaryawan(Karyawan k) {
        JPanel kartu = new JPanel(new GridLayout(4, 1, 0, 8));
        kartu.setBackground(Color.decode("#c59b6d")); // Cokelat terang
        kartu.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.decode("#b45309"), 2), new EmptyBorder(15, 15, 15, 15)
        ));
        kartu.setPreferredSize(new Dimension(300, 180));

        kartu.add(createLabelCard("Nama: " + k.getNamaLengkap()));
        kartu.add(createLabelCard("ID Karyawan: " + k.getIdKaryawan()));
        kartu.add(createLabelCard("Departemen: " + k.getDepartemen()));

        JPanel pnlAksi = new JPanel(new GridLayout(1, 2, 10, 0));
        pnlAksi.setOpaque(false);
        JButton btnEdit = createButton("Edit", "#facc15"); btnEdit.setForeground(Color.BLACK);
        JButton btnHapus = createButton("Delete", "#991b1b");

        btnEdit.addActionListener(e -> {
            uidKaryawanTerpilih = k.getUidRfid(); // Simpan memori hash lama
            txtID.setText(k.getIdKaryawan()); 
            txtUID.setText(""); // Dikosongkan agar hash lama tidak muncul dan di-hash ulang!
            txtNama.setText(k.getNamaLengkap()); 
            cbDept.setSelectedItem(k.getDepartemen());
            btnSimpanK.setEnabled(false); btnUpdateK.setEnabled(true);
        });

        btnHapus.addActionListener(e -> {
            int ch = JOptionPane.showConfirmDialog(this, "Hapus " + k.getNamaLengkap() + "?", "Hapus", JOptionPane.YES_NO_OPTION);
            if (ch == JOptionPane.YES_OPTION) {
                karyawanService.hapusKaryawan(k.getIdKaryawan());
                refreshDataKaryawan();
            }
        });

        pnlAksi.add(btnEdit); pnlAksi.add(btnHapus);
        kartu.add(pnlAksi);
        return kartu;
    }

    private void resetFormKaryawan() {
        txtID.setText(karyawanService.generateNewId());
        txtUID.setText(""); txtNama.setText(""); cbDept.setSelectedIndex(0);
        uidKaryawanTerpilih = "";
        btnSimpanK.setEnabled(true); btnUpdateK.setEnabled(false);
    }

    // ==========================================
    // 4. HALAMAN MANAJEMEN ADMIN (CARD LAYOUT)
    // ==========================================
    private JPanel buatHalamanAdmin() {
        JPanel panelUtama = new JPanel(new BorderLayout(10, 10));
        panelUtama.setBackground(Color.decode("#d8e2ea"));
        panelUtama.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- Panel Atas (Form Input) ---
        JPanel panelAtas = new JPanel(new BorderLayout(5, 10));
        panelAtas.setOpaque(false);

        JPanel panelForm = new JPanel(new GridLayout(2, 2, 10, 10));
        panelForm.setBackground(Color.decode("#e6eff5"));
        panelForm.setBorder(new EmptyBorder(15, 15, 15, 15));

        panelForm.add(createLabelHitam("Username Admin:", 14));
        txtUsernameAdmin = new JTextField(); panelForm.add(txtUsernameAdmin);

        panelForm.add(createLabelHitam("Password (Kosongkan bila tidak diubah):", 14));
        txtPasswordAdmin = new JPasswordField(); panelForm.add(txtPasswordAdmin);

        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelAksi.setOpaque(false);
        btnSimpanA = createButton("Save", "#3b82f6"); 
        btnUpdateA = createButton("Update", "#f59e0b"); btnUpdateA.setEnabled(false);
        btnResetA = createButton("Reset Form", "#22c55e");
        panelAksi.add(btnSimpanA); panelAksi.add(btnUpdateA); panelAksi.add(btnResetA);

        panelAtas.add(panelForm, BorderLayout.NORTH);
        panelAtas.add(panelAksi, BorderLayout.SOUTH);

        // --- Panel Bawah (Grid Card Layout) ---
        panelKartuAdmin = new JPanel(new GridLayout(0, 3, 15, 15));
        panelKartuAdmin.setBackground(Color.decode("#d8e2ea"));
        
        JPanel panelBungkus = new JPanel(new BorderLayout());
        panelBungkus.setBackground(Color.decode("#d8e2ea"));
        panelBungkus.add(panelKartuAdmin, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(panelBungkus);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panelUtama.add(panelAtas, BorderLayout.NORTH);
        panelUtama.add(scrollPane, BorderLayout.CENTER);

        // --- Event Admin ---
        btnSimpanA.addActionListener(e -> {
            String u = txtUsernameAdmin.getText(); String p = new String(txtPasswordAdmin.getPassword());
            if(u.isBlank() || p.isBlank()) { JOptionPane.showMessageDialog(this, "Isi username & password!"); return; }
            adminService.tambahAdmin(u, p);
            JOptionPane.showMessageDialog(this, "Admin Tersimpan!"); resetFormAdmin(); refreshDataAdmin();
        });
        btnUpdateA.addActionListener(e -> {
            String uBaru = txtUsernameAdmin.getText(); String pBaru = new String(txtPasswordAdmin.getPassword());
            adminService.updateAdmin(usernameAdminTerpilih, uBaru, pBaru);
            JOptionPane.showMessageDialog(this, "Admin Diperbarui!"); resetFormAdmin(); refreshDataAdmin();
        });
        btnResetA.addActionListener(e -> resetFormAdmin());

        return panelUtama;
    }

    private void refreshDataAdmin() {
        panelKartuAdmin.removeAll();
        for (AdminUser a : adminService.ambilSemuaAdmin()) {
            panelKartuAdmin.add(buatKartuAdmin(a));
        }
        panelKartuAdmin.revalidate();
        panelKartuAdmin.repaint();
    }

    private JPanel buatKartuAdmin(AdminUser a) {
        JPanel kartu = new JPanel(new GridLayout(3, 1, 0, 8));
        kartu.setBackground(Color.decode("#c59b6d"));
        kartu.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.decode("#b45309"), 2), new EmptyBorder(15, 15, 15, 15)
        ));
        kartu.setPreferredSize(new Dimension(300, 150));

        kartu.add(createLabelCard("Username: " + a.getUsername()));
        
        String hash = a.getPasswordHash();
        if(hash != null && hash.length() > 20) hash = hash.substring(0, 20) + "...";
        kartu.add(createLabelCard("Pass Hash: " + hash));

        JPanel pnlAksi = new JPanel(new GridLayout(1, 2, 10, 0));
        pnlAksi.setOpaque(false);
        JButton btnEdit = createButton("Edit", "#facc15"); btnEdit.setForeground(Color.BLACK);
        JButton btnHapus = createButton("Delete", "#991b1b");

        btnEdit.addActionListener(e -> {
            usernameAdminTerpilih = a.getUsername();
            txtUsernameAdmin.setText(usernameAdminTerpilih); txtPasswordAdmin.setText("");
            btnSimpanA.setEnabled(false); btnUpdateA.setEnabled(true);
        });

        btnHapus.addActionListener(e -> {
            if(a.getUsername().equals("admin")) { JOptionPane.showMessageDialog(this, "Admin utama tak bisa dihapus!"); return; }
            int ch = JOptionPane.showConfirmDialog(this, "Hapus admin " + a.getUsername() + "?", "Hapus", JOptionPane.YES_NO_OPTION);
            if (ch == JOptionPane.YES_OPTION) {
                adminService.hapusAdmin(a.getUsername());
                refreshDataAdmin();
            }
        });

        pnlAksi.add(btnEdit); pnlAksi.add(btnHapus);
        kartu.add(pnlAksi);
        return kartu;
    }

    private void resetFormAdmin() {
        txtUsernameAdmin.setText(""); txtPasswordAdmin.setText("");
        usernameAdminTerpilih = "";
        btnSimpanA.setEnabled(true); btnUpdateA.setEnabled(false);
    }

    // ==========================================
    // 5. HALAMAN LOG PRESENSI (TABEL)
    // ==========================================
    private JPanel buatHalamanLog() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.decode("#d8e2ea"));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        JButton btnRefreshLog = createButton("Refresh Data", "#22c55e");
        btnRefreshLog.addActionListener(e -> refreshDataLog());
        topPanel.add(btnRefreshLog);

        modelLog = new DefaultTableModel(new Object[]{"ID Log", "Hash UID", "Nama Pekerja", "Waktu Tap", "Status"}, 0);
        tableLog = new JTable(modelLog);
        tableLog.setRowHeight(30);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(tableLog), BorderLayout.CENTER);

        return panel;
    }

    private void refreshDataLog() {
        modelLog.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        for (LogAbsensi log : logAbsensiService.ambilSemuaLog()) {
            String nama = logAbsensiService.cariNamaKaryawanByUid(log.getUidRfid());
            String waktu = log.getWaktuTap() != null ? sdf.format(java.sql.Timestamp.valueOf(log.getWaktuTap())) : "-";
            
            // Masking UID Hash yang terlalu panjang agar tabel tetap rapi
            String displayUid = log.getUidRfid();
            if (displayUid != null && displayUid.length() > 15) {
                displayUid = displayUid.substring(0, 15) + "...";
            }
            
            modelLog.addRow(new Object[]{log.getIdLog(), displayUid, nama, waktu, log.getStatus()});
        }
    }

    // ==========================================
    // UTILITY METHODS & UI TRICKS
    // ==========================================
    private JLabel createLabelHitam(String text, int size) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.BLACK);
        lbl.setFont(new Font("SansSerif", Font.BOLD, size));
        return lbl;
    }
    
    private JLabel createLabelCard(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        return lbl;
    }

    private JButton buatTombolMenu(String teks) {
        JButton btn = new JButton(teks);
        btn.setBackground(Color.decode("#374151")); 
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 16)); 
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(230, 45));
        return btn;
    }

    private JButton createButton(String text, String hexColor) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.decode(hexColor)); 
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14)); 
        btn.setFocusPainted(false);
        return btn;
    }

    /**
     * Animasi Notifikasi Geser (Sliding Toast/Snackbar) Anti-Freeze
     */
    private void showSlidingNotification(String message, boolean isSuccess) {
        JLayeredPane layeredPane = getLayeredPane();
        
        JPanel toastPanel = new JPanel(new BorderLayout());
        toastPanel.setBackground(isSuccess ? Color.decode("#10b981") : Color.decode("#ef4444")); // Hijau atau Merah
        toastPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(isSuccess ? Color.decode("#047857") : Color.decode("#b91c1c"), 2),
                new EmptyBorder(15, 25, 15, 25)
        ));
        
        JLabel lblMessage = new JLabel(message);
        lblMessage.setForeground(Color.WHITE);
        lblMessage.setFont(new Font("SansSerif", Font.BOLD, 16));
        toastPanel.add(lblMessage, BorderLayout.CENTER);
        
        // Kalkulasi ukuran dan posisi awal
        toastPanel.setSize(toastPanel.getPreferredSize().width + 20, 60);
        int targetX = getWidth() - toastPanel.getWidth() - 30; // Berhenti dengan jarak 30px dari kanan
        int startX = getWidth(); // Mulai dari luar layar sebelah kanan
        int y = 80; // Ketinggian 80px dari atas
        
        toastPanel.setLocation(startX, y);
        layeredPane.add(toastPanel, JLayeredPane.POPUP_LAYER);
        
        // Animasi menggunakan javax.swing.Timer agar mulus tanpa freeze (EDT Compliant)
        int delay = 10; // milidetik per frame
        int step = 20;  // kecepatan geser (pixel per frame)
        
        Timer slideInTimer = new Timer(delay, null);
        Timer slideOutTimer = new Timer(delay, null);
        
        slideInTimer.addActionListener(e -> {
            int currentX = toastPanel.getX();
            if (currentX > targetX) {
                toastPanel.setLocation(currentX - step, y); // Geser masuk ke kiri
            } else {
                toastPanel.setLocation(targetX, y);
                slideInTimer.stop();
                
                // Tahan di layar selama 2.5 detik, lalu mulai geser keluar
                Timer pauseTimer = new Timer(2500, pauseEvent -> {
                    slideOutTimer.start();
                });
                pauseTimer.setRepeats(false);
                pauseTimer.start();
            }
        });
        
        slideOutTimer.addActionListener(e -> {
            int currentX = toastPanel.getX();
            if (currentX < startX) {
                toastPanel.setLocation(currentX + step, y); // Geser keluar ke kanan
            } else {
                slideOutTimer.stop();
                layeredPane.remove(toastPanel);
                layeredPane.repaint();
            }
        });
        
        slideInTimer.start();
    }
}