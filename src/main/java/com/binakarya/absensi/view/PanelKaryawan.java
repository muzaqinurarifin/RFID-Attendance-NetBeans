package com.binakarya.absensi.view;

import com.binakarya.absensi.model.Karyawan;
import com.binakarya.absensi.security.SecurityUtils;
import com.binakarya.absensi.service.KaryawanService;
import com.binakarya.absensi.utils.I18nService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class PanelKaryawan extends JPanel {
    private final KaryawanService karyawanService;
    private final DashboardAdmin parentFrame;

    private JLabel lblKaryawanId, lblKaryawanUid, lblKaryawanNama, lblKaryawanDept;
    private JTextField txtUID, txtID, txtNama, txtCariKaryawan;
    private JComboBox<String> cbDept;
    private JButton btnSimpanK, btnUpdateK, btnResetK, btnCariK;
    private JPanel panelKartuKaryawan;
    private String uidKaryawanTerpilih = "";

    public PanelKaryawan(DashboardAdmin parentFrame) {
        this.parentFrame = parentFrame;
        this.karyawanService = new KaryawanService();

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.decode("#d8e2ea"));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initUI();
    }

    private void initUI() {
        JPanel panelAtas = new JPanel(new BorderLayout(5, 10));
        panelAtas.setOpaque(false);

        JPanel panelForm = new JPanel(new GridLayout(2, 4, 10, 10));
        panelForm.setBackground(Color.decode("#e6eff5"));
        panelForm.setBorder(new EmptyBorder(15, 15, 15, 15));

        lblKaryawanId = createLabelHitam("", 14); txtID = new JTextField(); txtID.setEnabled(false);
        lblKaryawanUid = createLabelHitam("", 13); txtUID = new JTextField();
        lblKaryawanNama = createLabelHitam("", 14); txtNama = new JTextField();
        lblKaryawanDept = createLabelHitam("", 14);
        cbDept = new JComboBox<>(new String[]{"Operasional Proyek", "Teknologi & Informasi", "Manajemen Risiko", "Logistik & Gudang"});

        panelForm.add(lblKaryawanId); panelForm.add(txtID);
        panelForm.add(lblKaryawanUid); panelForm.add(txtUID);
        panelForm.add(lblKaryawanNama); panelForm.add(txtNama);
        panelForm.add(lblKaryawanDept); panelForm.add(cbDept);

        JPanel panelAksi = new JPanel(new BorderLayout());
        panelAksi.setOpaque(false);
        
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelTombol.setOpaque(false);
        btnSimpanK = createButton("", "#3b82f6"); 
        btnUpdateK = createButton("", "#f59e0b"); btnUpdateK.setEnabled(false);
        btnResetK = createButton("", "#22c55e");
        panelTombol.add(btnSimpanK); panelTombol.add(btnUpdateK); panelTombol.add(btnResetK);

        JPanel panelPencarian = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelPencarian.setOpaque(false);
        txtCariKaryawan = new JTextField(15);
        btnCariK = createButton("", "#e2e8f0"); btnCariK.setForeground(Color.BLACK);
        panelPencarian.add(txtCariKaryawan); panelPencarian.add(btnCariK);

        panelAksi.add(panelTombol, BorderLayout.WEST);
        panelAksi.add(panelPencarian, BorderLayout.EAST);
        panelAtas.add(panelForm, BorderLayout.NORTH);
        panelAtas.add(panelAksi, BorderLayout.SOUTH);

        panelKartuKaryawan = new JPanel(new GridLayout(0, 3, 15, 15));
        panelKartuKaryawan.setBackground(Color.decode("#d8e2ea"));
        JPanel panelBungkus = new JPanel(new BorderLayout());
        panelBungkus.setBackground(Color.decode("#d8e2ea"));
        panelBungkus.add(panelKartuKaryawan, BorderLayout.NORTH);

        add(panelAtas, BorderLayout.NORTH);
        add(new JScrollPane(panelBungkus), BorderLayout.CENTER);

        btnSimpanK.addActionListener(e -> {
            if (txtUID.getText().trim().isEmpty() || txtNama.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "UID dan Nama wajib diisi!");
                return;
            }
            karyawanService.tambahKaryawan(new Karyawan(SecurityUtils.hashPassword(txtUID.getText().trim()), txtID.getText(), txtNama.getText(), cbDept.getSelectedItem().toString()));
            resetForm(); 
            refreshData();
        });
        
        btnUpdateK.addActionListener(e -> {
            String finalUid = txtUID.getText().trim().isEmpty() ? uidKaryawanTerpilih : SecurityUtils.hashPassword(txtUID.getText().trim());
            karyawanService.updateKaryawan(new Karyawan(finalUid, txtID.getText(), txtNama.getText(), cbDept.getSelectedItem().toString()));
            resetForm(); 
            refreshData();
        });
        
        btnResetK.addActionListener(e -> resetForm());
        btnCariK.addActionListener(e -> refreshData());

        resetForm();
        refreshData();
    }

    public void refreshData() {
        // Jika masih terasa lambat, logika database di sini bisa dibungkus SwingWorker
        panelKartuKaryawan.removeAll();
        for (Karyawan k : karyawanService.ambilSemuaKaryawan()) {
            JPanel kartu = new JPanel(new GridLayout(4, 1, 0, 8));
            kartu.setBackground(Color.decode("#c59b6d"));
            kartu.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.decode("#b45309"), 2), new EmptyBorder(15, 15, 15, 15)));
            
            kartu.add(createLabelCard("Nama: " + k.getNamaLengkap()));
            kartu.add(createLabelCard("ID: " + k.getIdKaryawan()));
            kartu.add(createLabelCard("Dept: " + k.getDepartemen()));

            JPanel pnlAksi = new JPanel(new GridLayout(1, 2, 10, 0));
            pnlAksi.setOpaque(false);
            JButton btnEdit = createButton("Edit", "#facc15");
            JButton btnHapus = createButton("Hapus", "#991b1b");

            btnEdit.addActionListener(e -> {
                uidKaryawanTerpilih = k.getUidRfid();
                txtID.setText(k.getIdKaryawan());
                txtNama.setText(k.getNamaLengkap());
                cbDept.setSelectedItem(k.getDepartemen());
                btnSimpanK.setEnabled(false); btnUpdateK.setEnabled(true);
            });

            btnHapus.addActionListener(e -> {
                if (JOptionPane.showConfirmDialog(this, "Yakin hapus?") == JOptionPane.YES_OPTION) {
                    karyawanService.hapusKaryawan(k.getIdKaryawan());
                    refreshData();
                }
            });

            pnlAksi.add(btnEdit); pnlAksi.add(btnHapus);
            kartu.add(pnlAksi);
            panelKartuKaryawan.add(kartu);
        }
        panelKartuKaryawan.revalidate();
        panelKartuKaryawan.repaint();
    }

    private void resetForm() {
        txtID.setText(karyawanService.generateNewId());
        txtUID.setText(""); txtNama.setText(""); cbDept.setSelectedIndex(0);
        uidKaryawanTerpilih = "";
        btnSimpanK.setEnabled(true); btnUpdateK.setEnabled(false);
    }

    public void applyLanguage() {
        lblKaryawanId.setText(I18nService.get("ui.label.empid"));
        lblKaryawanUid.setText(I18nService.get("ui.label.uid"));
        lblKaryawanNama.setText(I18nService.get("ui.label.fullname"));
        lblKaryawanDept.setText(I18nService.get("ui.label.dept"));
        btnSimpanK.setText(I18nService.get("ui.btn.save"));
        btnUpdateK.setText(I18nService.get("ui.btn.update"));
        btnResetK.setText(I18nService.get("ui.btn.reset"));
        btnCariK.setText(I18nService.get("ui.btn.search"));
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