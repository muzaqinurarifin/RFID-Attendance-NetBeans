package com.binakarya.absensi.service;

import com.binakarya.absensi.dao.GenericDAO;
import com.binakarya.absensi.model.Karyawan;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class KaryawanService {
    private final GenericDAO<Karyawan> dao;

    public KaryawanService() {
        this.dao = new GenericDAO<>("karyawan", Karyawan.class);
    }

    public void tambahKaryawan(Karyawan k) {
        dao.save(k);
    }

    public List<Karyawan> ambilSemuaKaryawan() {
        return dao.findAll();
    }

    public String generateNewId() {
        List<Karyawan> semuaKaryawan = dao.findAll();
        if (semuaKaryawan == null || semuaKaryawan.isEmpty()) return "RFID-01";

        int maxId = 0;
        for (Karyawan k : semuaKaryawan) {
            String id = k.getIdKaryawan();
            if (id != null && id.startsWith("RFID-")) {
                try {
                    int num = Integer.parseInt(id.substring(5));
                    if (num > maxId) maxId = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("RFID-%02d", (maxId + 1));
    }

    public List<Karyawan> cariKaryawan(String key) {
        List<Bson> filters = new ArrayList<>();
        for (Field field : Karyawan.class.getDeclaredFields()) {
            // Lewati pencarian pada id MongoDB dan uidRfid
            if (field.getName().equals("uidRfid") || field.getName().equals("id")) continue;
            filters.add(Filters.regex(field.getName(), key, "i"));
        }
        return dao.findMany(Filters.or(filters));
    }

    public void updateKaryawan(Karyawan newK) {
        Bson filter = Filters.eq("idKaryawan", newK.getIdKaryawan());
        
        // Ambil data lama dari database untuk mempertahankan ObjectId (Cegah immutable error)
        List<Karyawan> dataLama = dao.findMany(filter);
        if (!dataLama.isEmpty()) {
            newK.setId(dataLama.get(0).getId()); // Copy ID lama ke objek baru
            dao.update(filter, newK); 
        }
    }

    public void hapusKaryawan(String idK) {
        Bson filter = Filters.eq("idKaryawan", idK);
        dao.delete(filter);
    }
}