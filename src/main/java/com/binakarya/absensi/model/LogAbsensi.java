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
    public String getUidRfid() { return uidRfid; }
    public LocalDateTime getWaktuTap() { return waktuTap; }
    public String getStatus() { return status; }
}