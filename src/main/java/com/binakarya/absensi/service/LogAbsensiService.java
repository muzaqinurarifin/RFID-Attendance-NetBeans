package com.binakarya.absensi.service;

import com.binakarya.absensi.dao.GenericDAO;
import com.binakarya.absensi.model.LogAbsensi;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class LogAbsensiService {
    private final GenericDAO<LogAbsensi> dao;
    private final KaryawanService karyawanService;

    public LogAbsensiService() {
        this.dao = new GenericDAO<>("absensi_logs", LogAbsensi.class);
        this.karyawanService = new KaryawanService();
    }

    public void tambahLog(LogAbsensi log) {
        dao.save(log);
    }

    public List<LogAbsensi> ambilSemuaLog() {
        return dao.findAll();
    }

    public List<LogAbsensi> cariLog(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return ambilSemuaLog();
        }

        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.regex("idLog", keyword, "i"));
        filters.add(Filters.regex("uidRfid", keyword, "i"));
        filters.add(Filters.regex("status", keyword, "i"));

        return dao.findMany(Filters.or(filters));
    }

    public String generateNewId() {
        List<LogAbsensi> semuaLogs = dao.findAll();
        if (semuaLogs == null || semuaLogs.isEmpty()) {
            return "LOG-001";
        }

        int maxId = 0;
        for (LogAbsensi log : semuaLogs) {
            String id = log.getIdLog();
            if (id != null && id.startsWith("LOG-")) {
                try {
                    int num = Integer.parseInt(id.substring(4));
                    if (num > maxId) {
                        maxId = num;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return String.format("LOG-%03d", maxId + 1);
    }

    public String cariNamaKaryawanByUid(String uidRfid) {
        if (uidRfid == null || uidRfid.isBlank()) {
            return "-";
        }
        var karyawan = karyawanService.cariKaryawanByUid(uidRfid);
        return karyawan == null ? "UID Tidak Dikenal" : karyawan.getNamaLengkap();
    }

    public LogAbsensi getLogTerakhir(String uidRfid) {
        if (uidRfid == null || uidRfid.isBlank()) {
            return null;
        }
        List<LogAbsensi> logs = dao.findMany(Filters.eq("uidRfid", uidRfid));
        if (logs == null || logs.isEmpty()) {
            return null;
        }
        LogAbsensi terakhir = logs.get(0);
        for (LogAbsensi log : logs) {
            if (log.getWaktuTap().isAfter(terakhir.getWaktuTap())) {
                terakhir = log;
            }
        }
        return terakhir;
    }

    public String tentukanStatus(String uidRfid) {
        LogAbsensi terakhir = getLogTerakhir(uidRfid);
        if (terakhir == null || "Keluar".equalsIgnoreCase(terakhir.getStatus())) {
            return "Masuk";
        }
        return "Keluar";
    }
}