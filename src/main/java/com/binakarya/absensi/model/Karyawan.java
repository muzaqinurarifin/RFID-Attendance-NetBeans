package com.binakarya.absensi.model;

/**
 * Entity Class Karyawan Konstruksi
 * Sesuai Standar Pertemuan 3: Menerapkan Enkapsulasi data (private).
 */
public class Karyawan {
    private String uidRfid;     // Identifier unik dari kartu fisik RFID pekerja
    private String idKaryawan;  // Nomor Induk Pekerja (NIP)
    private String namaLengkap; // Nama Pekerja
    private String departemen;  // Bidang kerja (contoh: Mandor, Operator, Tukang)

    public Karyawan(String uidRfid, String idKaryawan, String namaLengkap, String departemen) {
        this.uidRfid = uidRfid;
        this.idKaryawan = idKaryawan;
        this.namaLengkap = namaLengkap;
        this.departemen = departemen;
    }

    // --- GETTER & SETTER ---
    public String getUidRfid() { return uidRfid; }
    public void setUidRfid(String uidRfid) { this.uidRfid = uidRfid; }

    public String getIdKaryawan() { return idKaryawan; }
    public void setIdKaryawan(String idKaryawan) { this.idKaryawan = idKaryawan; }

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getDepartemen() { return departemen; }
    public void setDepartemen(String departemen) { this.departemen = departemen; }
}