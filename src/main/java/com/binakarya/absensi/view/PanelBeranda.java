package com.binakarya.absensi.view;

import com.binakarya.absensi.model.Karyawan;
import com.binakarya.absensi.model.LogAbsensi;
import com.binakarya.absensi.security.EncryptionUtils;
import com.binakarya.absensi.security.SecurityUtils;
import com.binakarya.absensi.service.KaryawanService;
import com.binakarya.absensi.service.LogAbsensiService;
import com.binakarya.absensi.utils.I18nService;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PanelBeranda extends JPanel {
    private final KaryawanService karyawanService;
    private final LogAbsensiService logAbsensiService;
    private final DashboardAdmin parentFrame;

    private JLabel lblWelcome, lblTap;
    private JTextField txtUidTap;
    private JButton btnTap;
    
    private JToggleButton btnMasuk;
    private JToggleButton btnKeluar;

    public PanelBeranda(DashboardAdmin parentFrame) {
        this.parentFrame = parentFrame;
        this.karyawanService = new KaryawanService();
        this.logAbsensiService = new LogAbsensiService();

        setLayout(new BorderLayout(20, 20));
        setBackground(Color.decode("#d8e2ea"));
        setBorder(new EmptyBorder(40, 40, 40, 40));

        initUI();
    }

    private void initUI() {
        //Area Jam & Welcome
        JPanel panelTengah = new JPanel(new GridLayout(2, 1));
        panelTengah.setOpaque(false);
        
        lblWelcome = new JLabel("", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblWelcome.setForeground(Color.decode("#1f2937"));

        JLabel lblJam = new JLabel("", SwingConstants.CENTER);
        lblJam.setFont(new Font("SansSerif", Font.BOLD, 60)); // Ukuran sedikit disesuaikan agar muat
        lblJam.setForeground(Color.decode("#3b82f6")); 

        //Timer Jam Terintegrasi dengan i18n dan Format Khusus English
        Timer timer = new Timer(1000, e -> {
            // Ambil bahasa yang sedang aktif
            Locale activeLocale = I18nService.getCurrentLocale();
            Date now = new Date();
            String formattedTime;
            
            if (activeLocale != null && activeLocale.getLanguage().equals("en")) {
                // Logika khusus untuk format English: EEEE, MMMM d[st/nd/rd/th], yyyy
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(now);
                int day = cal.get(java.util.Calendar.DAY_OF_MONTH);
                String suffix = getDayOfMonthSuffix(day);
                
                // Menambahkan akhiran suffix (st/nd/rd/th) langsung ke dalam pola SimpleDateFormat
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d'" + suffix + "', yyyy | HH:mm:ss", activeLocale);
                formattedTime = sdf.format(now);
            } else {
                // Format standar untuk Indonesia (ID) dan Malaysia (MS)
                Locale useLocale = (activeLocale != null) ? activeLocale : new Locale("id", "ID");
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy | HH:mm:ss", useLocale);
                formattedTime = sdf.format(now);
            }
            
            lblJam.setText(formattedTime);
        });
        timer.start();

        panelTengah.add(lblWelcome);
        panelTengah.add(lblJam);

        // Area Simulasi Tap RFID & Toggle
        JPanel pnlToggle = new JPanel(new GridLayout(1, 2, 0, 0));
        pnlToggle.setPreferredSize(new Dimension(300, 45));
        pnlToggle.setBorder(new LineBorder(Color.decode("#cbd5e1"), 2, true));
        
        btnMasuk = new JToggleButton();
        btnKeluar = new JToggleButton();
        
        Font toggleFont = new Font("SansSerif", Font.BOLD, 16);
        btnMasuk.setFont(toggleFont); btnKeluar.setFont(toggleFont);
        btnMasuk.setFocusPainted(false); btnKeluar.setFocusPainted(false);
        btnMasuk.setCursor(new Cursor(Cursor.HAND_CURSOR)); btnKeluar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(btnMasuk); bg.add(btnKeluar);
        
        btnMasuk.addActionListener(e -> updateToggleStyle());
        btnKeluar.addActionListener(e -> updateToggleStyle());
        
        pnlToggle.add(btnMasuk); pnlToggle.add(btnKeluar);
        
        btnMasuk.setSelected(true);
        updateToggleStyle();

        JPanel panelTap = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        panelTap.setBackground(Color.decode("#ffffff"));
        panelTap.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.decode("#cbd5e1"), 2, true),
                new EmptyBorder(10, 20, 10, 20)
        ));
        
        lblTap = new JLabel("");
        lblTap.setFont(new Font("SansSerif", Font.BOLD, 18));
        txtUidTap = new JTextField(20);
        txtUidTap.setFont(new Font("SansSerif", Font.BOLD, 18));
        
        btnTap = new JButton("");
        btnTap.setBackground(Color.decode("#3b82f6"));
        btnTap.setForeground(Color.WHITE);
        btnTap.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnTap.setPreferredSize(new Dimension(200, 40));
        btnTap.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelTap.add(lblTap);
        panelTap.add(txtUidTap);
        panelTap.add(btnTap);

        // --- INTEGRASI HARDWARE ASLI ---
        // 1. Aksi Tombol Manual (Simulasi / Manual Input)
        btnTap.addActionListener(e -> prosesTapRfid());
        
        // 2. Dukungan RFID Scanner USB (Keyboard Emulator) - Otomatis terproses saat scanner mengirim 'Enter'
        txtUidTap.addActionListener(e -> prosesTapRfid());
        
        // 3. Dukungan Hardware Serial (COM Port / Arduino)
        initSerialRFID();

        JPanel pnlTapWrapper = new JPanel(new BorderLayout(10, 15));
        pnlTapWrapper.setOpaque(false);
        
        JPanel pnlToggleCenter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlToggleCenter.setOpaque(false);
        pnlToggleCenter.add(pnlToggle);
        
        pnlTapWrapper.add(pnlToggleCenter, BorderLayout.NORTH);
        pnlTapWrapper.add(panelTap, BorderLayout.CENTER);

        add(panelTengah, BorderLayout.CENTER);
        add(pnlTapWrapper, BorderLayout.SOUTH);
    }

    private void updateToggleStyle() {
        if (btnMasuk.isSelected()) {
            btnMasuk.setBackground(Color.decode("#10b981"));
            btnMasuk.setForeground(Color.WHITE);
            btnKeluar.setBackground(Color.decode("#f1f5f9"));
            btnKeluar.setForeground(Color.decode("#64748b"));
        } else {
            btnKeluar.setBackground(Color.decode("#ef4444"));
            btnKeluar.setForeground(Color.WHITE);
            btnMasuk.setBackground(Color.decode("#f1f5f9"));
            btnMasuk.setForeground(Color.decode("#64748b"));
        }
    }

    /**
     * Integrasi Alat RFID via Komunikasi Serial (Library jSerialComm)
     */
    private void initSerialRFID() {
        try {
            SerialPort[] ports = SerialPort.getCommPorts();
            if (ports.length == 0) {
                System.out.println("LOG HARDWARE: Tidak ada port serial RFID (COM) yang terdeteksi.");
                return;
            }
            
            // Otomatis memilih COM Port pertama yang terdeteksi (Bisa disesuaikan misal ports[1])
            SerialPort rfidPort = ports[0];
            rfidPort.setBaudRate(9600);
            
            if (rfidPort.openPort()) {
                System.out.println("LOG HARDWARE: Mesin RFID terhubung sukses pada " + rfidPort.getSystemPortName());
                
                // Mendaftarkan Event Listener agar sistem tidak Freeze (Asinkron)
                rfidPort.addDataListener(new SerialPortDataListener() {
                    @Override
                    public int getListeningEvents() {
                        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                    }

                    @Override
                    public void serialEvent(SerialPortEvent event) {
                        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) return;
                        
                        byte[] newData = new byte[rfidPort.bytesAvailable()];
                        int numRead = rfidPort.readBytes(newData, newData.length);
                        String uidRfidRaw = new String(newData, 0, numRead).trim();
                        
                        if (!uidRfidRaw.isEmpty()) {
                            // Lempar eksekusi ke Thread UI (EDT) menggunakan invokeLater
                            SwingUtilities.invokeLater(() -> {
                                txtUidTap.setText(uidRfidRaw);
                                prosesTapRfid(); // Otomatis proses tap!
                            });
                        }
                    }
                });
            }
        } catch (NoClassDefFoundError | Exception e) {
            System.err.println("LOG HARDWARE: Library jSerialComm belum terpasang. Abaikan jika menggunakan RFID model USB.");
        }
    }

    private void prosesTapRfid() {
        String uidInput = txtUidTap.getText().trim();
        if (uidInput.isEmpty()) { 
            parentFrame.showSlidingNotification("UID Kosong!", false); 
            return; 
        }

        String hashedUID = SecurityUtils.hashPassword(uidInput);
        Karyawan k = karyawanService.cariKaryawanByUid(hashedUID);
        
        if (k == null) k = karyawanService.cariKaryawanByUid(uidInput);
        
        if (k == null) {
            parentFrame.showSlidingNotification("Akses ditolak, kartu tidak terdaftar", false);
            txtUidTap.setText(""); 
            return;
        }

        String statusDb = btnMasuk.isSelected() ? "Masuk" : "Keluar";
        String statusTranslated = btnMasuk.isSelected() ? I18nService.get("ui.toggle.masuk") : I18nService.get("ui.toggle.keluar");

        try {
            String encryptedUID = EncryptionUtils.encrypt(uidInput);
            String idLogBaru = logAbsensiService.generateNewId();
            logAbsensiService.tambahLog(new LogAbsensi(idLogBaru, encryptedUID, statusDb));
            
            parentFrame.showSlidingNotification(k.getNamaLengkap() + " (" + statusTranslated + ")", true);
        } catch (Exception ex) {
            parentFrame.showSlidingNotification("Sistem Error Kriptografi!", false);
        }
        
        txtUidTap.setText(""); 
        parentFrame.refreshLogTable();
    }

    public void applyLanguage() {
        lblWelcome.setText(I18nService.get("ui.title.welcome"));
        lblTap.setText(I18nService.get("ui.label.tap"));
        btnTap.setText(I18nService.get("ui.btn.simulasikan"));
        
        btnMasuk.setText(I18nService.get("ui.toggle.masuk"));
        btnKeluar.setText(I18nService.get("ui.toggle.keluar"));
    }

    /**
     * Fungsi utilitas untuk menentukan akhiran hari (Ordinal Suffix) dalam Bahasa Inggris
     */
    private String getDayOfMonthSuffix(int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        return switch (n % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }
}