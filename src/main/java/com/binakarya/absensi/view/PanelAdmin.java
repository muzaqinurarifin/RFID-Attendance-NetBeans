package com.binakarya.absensi.view;

import com.binakarya.absensi.model.AdminUser;
import com.binakarya.absensi.service.AdminService;
import com.binakarya.absensi.utils.I18nService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class PanelAdmin extends JPanel {
    private final AdminService adminService;
    private final DashboardAdmin parentFrame;

    private JLabel lblAdminUser, lblAdminPass;
    private JTextField txtUsernameAdmin;
    private JPasswordField txtPasswordAdmin;
    private JButton btnSimpanA, btnUpdateA, btnResetA;
    private JPanel panelKartuAdmin;
    private String usernameAdminTerpilih = "";

    public PanelAdmin(DashboardAdmin parentFrame) {
        this.parentFrame = parentFrame;
        this.adminService = new AdminService();

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.decode("#d8e2ea"));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initUI();
    }

    private void initUI() {
        JPanel panelAtas = new JPanel(new BorderLayout(5, 10));
        panelAtas.setOpaque(false);

        JPanel panelForm = new JPanel(new GridLayout(2, 2, 10, 10));
        panelForm.setBackground(Color.decode("#e6eff5"));
        panelForm.setBorder(new EmptyBorder(15, 15, 15, 15));

        lblAdminUser = createLabelHitam("", 14);
        txtUsernameAdmin = new JTextField();
        panelForm.add(lblAdminUser); panelForm.add(txtUsernameAdmin);

        lblAdminPass = createLabelHitam("", 14);
        txtPasswordAdmin = new JPasswordField();
        panelForm.add(lblAdminPass); panelForm.add(txtPasswordAdmin);

        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelAksi.setOpaque(false);
        btnSimpanA = createButton("", "#3b82f6"); 
        btnUpdateA = createButton("", "#f59e0b"); btnUpdateA.setEnabled(false);
        btnResetA = createButton("", "#22c55e");
        panelAksi.add(btnSimpanA); panelAksi.add(btnUpdateA); panelAksi.add(btnResetA);

        panelAtas.add(panelForm, BorderLayout.NORTH);
        panelAtas.add(panelAksi, BorderLayout.SOUTH);

        panelKartuAdmin = new JPanel(new GridLayout(0, 3, 15, 15));
        panelKartuAdmin.setBackground(Color.decode("#d8e2ea"));
        JPanel panelBungkus = new JPanel(new BorderLayout());
        panelBungkus.setBackground(Color.decode("#d8e2ea"));
        panelBungkus.add(panelKartuAdmin, BorderLayout.NORTH);

        add(panelAtas, BorderLayout.NORTH);
        add(new JScrollPane(panelBungkus), BorderLayout.CENTER);

        btnSimpanA.addActionListener(e -> {
            adminService.tambahAdmin(txtUsernameAdmin.getText(), new String(txtPasswordAdmin.getPassword()));
            resetForm(); refreshData();
        });
        
        btnUpdateA.addActionListener(e -> {
            adminService.updateAdmin(usernameAdminTerpilih, txtUsernameAdmin.getText(), new String(txtPasswordAdmin.getPassword()));
            resetForm(); refreshData();
        });
        
        btnResetA.addActionListener(e -> resetForm());
    }

    public void refreshData() {
        panelKartuAdmin.removeAll();
        for (AdminUser a : adminService.ambilSemuaAdmin()) {
            JPanel kartu = new JPanel(new GridLayout(2, 1, 0, 8));
            kartu.setBackground(Color.decode("#c59b6d"));
            kartu.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.decode("#b45309"), 2), new EmptyBorder(15, 15, 15, 15)));
            kartu.setPreferredSize(new Dimension(300, 100));

            kartu.add(createLabelCard(I18nService.get("ui.admin.user") + " " + a.getUsername()));

            JPanel pnlAksi = new JPanel(new GridLayout(1, 2, 10, 0));
            pnlAksi.setOpaque(false);
            JButton btnEdit = createButton(I18nService.get("ui.btn.edit"), "#facc15"); btnEdit.setForeground(Color.BLACK);
            JButton btnHapus = createButton(I18nService.get("ui.btn.delete"), "#991b1b");

            btnEdit.addActionListener(e -> {
                usernameAdminTerpilih = a.getUsername();
                txtUsernameAdmin.setText(usernameAdminTerpilih); txtPasswordAdmin.setText("");
                btnSimpanA.setEnabled(false); btnUpdateA.setEnabled(true);
            });

            btnHapus.addActionListener(e -> {
                if(!a.getUsername().equals("admin")) { adminService.hapusAdmin(a.getUsername()); refreshData(); }
            });

            pnlAksi.add(btnEdit); pnlAksi.add(btnHapus);
            kartu.add(pnlAksi);
            panelKartuAdmin.add(kartu);
        }
        panelKartuAdmin.revalidate();
        panelKartuAdmin.repaint();
    }

    private void resetForm() {
        txtUsernameAdmin.setText(""); txtPasswordAdmin.setText("");
        usernameAdminTerpilih = "";
        btnSimpanA.setEnabled(true); btnUpdateA.setEnabled(false);
    }

    public void applyLanguage() {
        lblAdminUser.setText(I18nService.get("ui.admin.user"));
        lblAdminPass.setText(I18nService.get("ui.admin.pass"));
        btnSimpanA.setText(I18nService.get("ui.btn.save"));
        btnUpdateA.setText(I18nService.get("ui.btn.update"));
        btnResetA.setText(I18nService.get("ui.btn.reset"));
        refreshData();
    }

    private JLabel createLabelHitam(String text, int size) {
        JLabel lbl = new JLabel(text); lbl.setForeground(Color.BLACK); lbl.setFont(new Font("SansSerif", Font.BOLD, size)); return lbl;
    }
    private JLabel createLabelCard(String text) {
        JLabel lbl = new JLabel(text); lbl.setForeground(Color.WHITE); lbl.setFont(new Font("SansSerif", Font.BOLD, 14)); return lbl;
    }
    private JButton createButton(String text, String hexColor) {
        JButton btn = new JButton(text); btn.setBackground(Color.decode(hexColor)); btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14)); btn.setFocusPainted(false); return btn;
    }
}