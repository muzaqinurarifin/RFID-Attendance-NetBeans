package com.binakarya.absensi.model;

import java.time.LocalDateTime;

/**
 * Entity Class LogAbsensi
 * Sesuai Standar Pertemuan 3: Menggunakan LocalDateTime untuk catatan runtun-waktu.
 */
public class LogAbsensi {
    private String idLog;
    private String uidRfid;         // Foreign Key ke tabel Pekerja
    private LocalDateTime waktuTap; // Waktu kedatangan/pulang presisi tinggi
    private String status;          // Status kehadiran ("IN", "OUT", "LATE")

    public LogAbsensi(String idLog, String uidRfid, String status) {
        this.idLog = idLog;
        this.uidRfid = uidRfid;
        this.waktuTap = LocalDateTime.now(); // Dibuat instan saat kartu ditap
        this.status = status;
    }

    public String getIdLog() { return idLog; }
    public String getUidRfid() { return uidRfid; }
    public LocalDateTime getWaktuTap() { return waktuTap; }
    public String getStatus() { return status; }
}