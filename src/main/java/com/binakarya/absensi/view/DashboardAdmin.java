package com.binakarya.absensi.view;

import com.binakarya.absensi.utils.I18nService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Locale;

public class DashboardAdmin extends JFrame {
    
    //Layout & Panels
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private PanelBeranda pnlBeranda;
    private PanelKaryawan pnlKaryawan;
    private PanelAdmin pnlAdmin;
    private PanelLog pnlLog;

    //Sidebar Buttons
    private JButton btnMenu1, btnMenu2, btnMenu3, btnMenu4, btnLogout;

    public DashboardAdmin() {
        setTitle("Bina Karya Konstruksi - Dashboard Presensi");
        setSize(1300, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.decode("#d8e2ea"));

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setOpaque(false);

        // Inisialisasi Panel Anak
        pnlBeranda = new PanelBeranda(this);
        pnlKaryawan = new PanelKaryawan(this);
        pnlAdmin = new PanelAdmin(this);
        pnlLog = new PanelLog();

        buatSidebar();

        mainPanel.add(pnlBeranda, "BERANDA");
        mainPanel.add(pnlKaryawan, "KARYAWAN");
        mainPanel.add(pnlAdmin, "ADMIN");
        mainPanel.add(pnlLog, "LOG");

        add(mainPanel, BorderLayout.CENTER);
        
        applyLanguage();
        cardLayout.show(mainPanel, "BERANDA");
    }

    // Fungsi komunikasi antar panel
    public void refreshLogTable() {
        pnlLog.refreshData();
    }

    private void applyLanguage() {
        SwingUtilities.invokeLater(() -> {
            btnMenu1.setText(I18nService.get("ui.sidebar.dashboard"));
            btnMenu2.setText(I18nService.get("ui.sidebar.karyawan"));
            btnMenu3.setText(I18nService.get("ui.sidebar.admin"));
            btnMenu4.setText(I18nService.get("ui.sidebar.log"));
            btnLogout.setText(I18nService.get("ui.sidebar.logout"));

            pnlBeranda.applyLanguage();
            pnlKaryawan.applyLanguage();
            pnlAdmin.applyLanguage();
            pnlLog.applyLanguage();
        });
    }

    private void buatSidebar() {
        JPanel sidebar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(Color.decode("#1f2937"));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel lblLogo = new JLabel("BINA KARYA", SwingConstants.CENTER);
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblLogo.setPreferredSize(new Dimension(230, 60));
        sidebar.add(lblLogo);

        btnMenu1 = buatTombolMenu("");
        btnMenu2 = buatTombolMenu("");
        btnMenu3 = buatTombolMenu("");
        btnMenu4 = buatTombolMenu("");

        btnMenu1.addActionListener(e -> cardLayout.show(mainPanel, "BERANDA"));
        btnMenu2.addActionListener(e -> { cardLayout.show(mainPanel, "KARYAWAN"); pnlKaryawan.refreshData(); });
        btnMenu3.addActionListener(e -> { cardLayout.show(mainPanel, "ADMIN"); pnlAdmin.refreshData(); });
        btnMenu4.addActionListener(e -> { cardLayout.show(mainPanel, "LOG"); pnlLog.refreshData(); });

        sidebar.add(btnMenu1); sidebar.add(btnMenu2); sidebar.add(btnMenu3); sidebar.add(btnMenu4);
        
        // Tombol Switch Bahasa
        JPanel pnlBahasa = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pnlBahasa.setOpaque(false);
        JButton btnLangID = createButtonLang("ID", "#047857");
        JButton btnLangEN = createButtonLang("EN", "#1d4ed8");
        JButton btnLangMS = createButtonLang("MY", "#facc15");
        btnLangMS.setForeground(Color.BLACK); 
        
        btnLangID.addActionListener(e -> { I18nService.setLocale(new Locale("id", "ID")); applyLanguage(); });
        btnLangEN.addActionListener(e -> { I18nService.setLocale(new Locale("en", "US")); applyLanguage(); });
        btnLangMS.addActionListener(e -> { I18nService.setLocale(new Locale("ms", "MY")); applyLanguage(); });
        
        pnlBahasa.add(btnLangID); pnlBahasa.add(btnLangEN); pnlBahasa.add(btnLangMS);
        sidebar.add(pnlBahasa);

        btnLogout = buatTombolMenu("");
        btnLogout.setBackground(Color.decode("#ef4444"));
        btnLogout.addActionListener(e -> {
            new LoginForm().setVisible(true);
            this.dispose();
        });
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);
    }

    private JButton buatTombolMenu(String teks) {
        JButton btn = new JButton(teks); btn.setBackground(Color.decode("#374151")); btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 16)); btn.setFocusPainted(false); btn.setPreferredSize(new Dimension(230, 45)); return btn;
    }
    
    private JButton createButtonLang(String text, String hexColor) {
        JButton btn = new JButton(text); btn.setBackground(Color.decode(hexColor)); btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14)); btn.setFocusPainted(false); return btn;
    }

    // Fungsi notifikasi global yang bisa dipanggil oleh panel anak
    public void showSlidingNotification(String message, boolean isSuccess) {
        JLayeredPane layeredPane = getLayeredPane();
        JPanel toastPanel = new JPanel(new BorderLayout());
        toastPanel.setBackground(isSuccess ? Color.decode("#10b981") : Color.decode("#ef4444"));
        toastPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(isSuccess ? Color.decode("#047857") : Color.decode("#b91c1c"), 2), new EmptyBorder(15, 25, 15, 25)));
        
        JLabel lblMessage = new JLabel(message);
        lblMessage.setForeground(Color.WHITE); lblMessage.setFont(new Font("SansSerif", Font.BOLD, 16));
        toastPanel.add(lblMessage, BorderLayout.CENTER);
        
        toastPanel.setSize(toastPanel.getPreferredSize().width + 20, 60);
        int targetX = getWidth() - toastPanel.getWidth() - 30; int startX = getWidth(); int y = 80;
        
        toastPanel.setLocation(startX, y); layeredPane.add(toastPanel, JLayeredPane.POPUP_LAYER);
        Timer slideInTimer = new Timer(10, null); Timer slideOutTimer = new Timer(10, null);
        
        slideInTimer.addActionListener(e -> {
            if (toastPanel.getX() > targetX) { toastPanel.setLocation(toastPanel.getX() - 20, y); } else {
                toastPanel.setLocation(targetX, y); slideInTimer.stop();
                Timer pauseTimer = new Timer(2500, ev -> slideOutTimer.start());
                pauseTimer.setRepeats(false); pauseTimer.start();
            }
        });
        slideOutTimer.addActionListener(e -> {
            if (toastPanel.getX() < startX) { toastPanel.setLocation(toastPanel.getX() + 20, y); } else {
                slideOutTimer.stop(); layeredPane.remove(toastPanel); layeredPane.repaint();
            }
        });
        slideInTimer.start();
    }
}