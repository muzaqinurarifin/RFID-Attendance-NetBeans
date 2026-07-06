package com.binakarya.absensi.view;

import com.binakarya.absensi.model.LogAbsensi;
import com.binakarya.absensi.security.EncryptionUtils;
import com.binakarya.absensi.security.SecurityUtils;
import com.binakarya.absensi.service.LogAbsensiService;
import com.binakarya.absensi.utils.I18nService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;

public class PanelLog extends JPanel {
    private final LogAbsensiService logAbsensiService;
    
    private JButton btnRefreshLog;
    private JTable tableLog;
    private DefaultTableModel modelLog;

    public PanelLog() {
        this.logAbsensiService = new LogAbsensiService();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.decode("#d8e2ea"));

        initUI();
    }

    private void initUI() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        
        btnRefreshLog = new JButton("");
        btnRefreshLog.setBackground(Color.decode("#22c55e"));
        btnRefreshLog.setForeground(Color.WHITE);
        btnRefreshLog.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRefreshLog.setFocusPainted(false);
        
        btnRefreshLog.addActionListener(e -> refreshData());
        topPanel.add(btnRefreshLog);

        modelLog = new DefaultTableModel(new Object[]{"", "", "", "", ""}, 0);
        tableLog = new JTable(modelLog);
        tableLog.setRowHeight(30);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(tableLog), BorderLayout.CENTER);
    }

    public void refreshData() {
        modelLog.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        for (LogAbsensi log : logAbsensiService.ambilSemuaLog()) {
            String dbUid = log.getUidRfid();
            String originalUid = dbUid;
            
            try { 
                originalUid = EncryptionUtils.decrypt(dbUid); 
            } catch (Exception ex) { 
                // Biarkan wujud asli untuk data lama (backward compatibility)
            }
            
            String hashedUidForSearch = SecurityUtils.hashPassword(originalUid);
            String nama = logAbsensiService.cariNamaKaryawanByUid(hashedUidForSearch);
            
            if (nama.equals("Tidak Dikenal")) {
                nama = logAbsensiService.cariNamaKaryawanByUid(originalUid);
            }

            String waktu = log.getWaktuTap() != null ? sdf.format(java.sql.Timestamp.valueOf(log.getWaktuTap())) : "-";
            modelLog.addRow(new Object[]{log.getIdLog(), originalUid, nama, waktu, log.getStatus()});
        }
    }

    public void applyLanguage() {
        btnRefreshLog.setText(I18nService.get("ui.btn.refresh"));
        modelLog.setColumnIdentifiers(new Object[]{
            I18nService.get("ui.table.idlog"),
            I18nService.get("ui.table.uid"),
            I18nService.get("ui.table.nama"),
            I18nService.get("ui.table.waktu"),
            I18nService.get("ui.table.status")
        });
        refreshData();
    }
}