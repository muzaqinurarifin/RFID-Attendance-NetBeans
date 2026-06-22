package com.binakarya.absensi.model;

import java.time.LocalDateTime;

public class LogAbsensi {
    private String idLog;
    private String uidRfid;
    private LocalDateTime waktuTap;
    private String status;

    public LogAbsensi(String idLog, String uidRfid, String status) {
        this.idLog = idLog;
        this.uidRfid = uidRfid;
        this.waktuTap = LocalDateTime.now();
        this.status = status;
    }

    public LogAbsensi() {}

    public String getIdLog() { return idLog; }
    public void setIdLog(String idLog) { this.idLog = idLog; }

    public String getUidRfid() { return uidRfid; }
    public void setUidRfid(String uidRfid) { this.uidRfid = uidRfid; }

    public LocalDateTime getWaktuTap() { return waktuTap; }
    public void setWaktuTap(LocalDateTime waktuTap) { this.waktuTap = waktuTap; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}