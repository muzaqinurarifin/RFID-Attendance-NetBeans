# Final Project — Ringkasan Fitur

## MK: Pemrograman Komputer 2 — Pertemuan 15: Demo Final Project

Tujuan: ringkas tema, fitur (fungsi & kode) dan lokasi kode yang relevan sehingga bisa dibaca/download untuk presentasi.

---

**Tema Aplikasi**

- Sistem Presensi RFID untuk proyek konstruksi (Bina Karya Attendance System).

---

**Fitur Aplikasi (Fungsi & Kode)**

1) CRUDs
- Implementasi DAO generik untuk MongoDB: [src/main/java/com/binakarya/absensi/dao/GenericDAO.java](src/main/java/com/binakarya/absensi/dao/GenericDAO.java#L15-L51)
- Interface CRUD: [src/main/java/com/binakarya/absensi/dao/BaseDAO.java](src/main/java/com/binakarya/absensi/dao/BaseDAO.java#L1-L13)
- Layanan Karyawan (tambah/ambil/update/hapus): [src/main/java/com/binakarya/absensi/service/KaryawanService.java](src/main/java/com/binakarya/absensi/service/KaryawanService.java#L12-L80)
- Layanan Admin (manajemen admin + autentikasi): [src/main/java/com/binakarya/absensi/service/AdminService.java](src/main/java/com/binakarya/absensi/service/AdminService.java#L10-L77)
- Layanan Log Absensi (tambah/ambil/cari + generator id): [src/main/java/com/binakarya/absensi/service/LogAbsensiService.java](src/main/java/com/binakarya/absensi/service/LogAbsensiService.java#L11-L88)

2) Cryptography
- AES encrypt/decrypt util: [src/main/java/com/binakarya/absensi/security/EncryptionUtils.java](src/main/java/com/binakarya/absensi/security/EncryptionUtils.java#L8-L26)
- SHA-256 hashing (UID & password hashing): [src/main/java/com/binakarya/absensi/security/SecurityUtils.java](src/main/java/com/binakarya/absensi/security/SecurityUtils.java#L14-L28)
- Penggunaan saat menyimpan log (hash + encrypt): `prosesTapRfid()` di [src/main/java/com/binakarya/absensi/view/PanelBeranda.java](src/main/java/com/binakarya/absensi/view/PanelBeranda.java#L226-L248)
- Penggunaan decrypt saat menampilkan log: `refreshData()` di [src/main/java/com/binakarya/absensi/view/PanelLog.java](src/main/java/com/binakarya/absensi/view/PanelLog.java#L61-L72)

3) Threads / Async (UI-safe threading)
- Timer untuk jam & update UI: [src/main/java/com/binakarya/absensi/view/PanelBeranda.java](src/main/java/com/binakarya/absensi/view/PanelBeranda.java#L80-L92)
- Penggunaan `SwingUtilities.invokeLater` saat menerima event serial agar eksekusi UI aman (EDT): [src/main/java/com/binakarya/absensi/view/PanelBeranda.java](src/main/java/com/binakarya/absensi/view/PanelBeranda.java#L212-L216)
- Animasi notifikasi non-blocking dengan `Timer`: [src/main/java/com/binakarya/absensi/view/DashboardAdmin.java](src/main/java/com/binakarya/absensi/view/DashboardAdmin.java#L138-L160)

4) Serial (1-way & 2-ways)
- Dependensi & import jSerialComm: terlihat di [src/main/java/com/binakarya/absensi/view/PanelBeranda.java](src/main/java/com/binakarya/absensi/view/PanelBeranda.java#L12-L14)
- Inisialisasi port serial & event-driven listener (one-way reading dari device → aplikasi): `initSerialRFID()` di [src/main/java/com/binakarya/absensi/view/PanelBeranda.java](src/main/java/com/binakarya/absensi/view/PanelBeranda.java#L181-L216)
- Catatan penting: tidak ditemukan implementasi pengiriman data ke perangkat serial (no `writeBytes`/output stream usage). Jadi saat ini hanya "one-way" (device → aplikasi). Jika diinginkan, fitur two-way (aplikasi → device) bisa ditambahkan.

5) i18n / l10n
- Service pengaturan locale & pengambilan teks: [src/main/java/com/binakarya/absensi/utils/I18nService.java](src/main/java/com/binakarya/absensi/utils/I18nService.java#L1-L20)
- Resource properties (pesan UI):
  - [src/main/resources/i18n/messages_en_US.properties](src/main/resources/i18n/messages_en_US.properties)
  - [src/main/resources/i18n/messages_id_ID.properties](src/main/resources/i18n/messages_id_ID.properties)
  - [src/main/resources/i18n/messages_ms_MY.properties](src/main/resources/i18n/messages_ms_MY.properties)
- Penerapan applyLanguage di tiap panel, dan tombol ganti bahasa di `DashboardAdmin`: [src/main/java/com/binakarya/absensi/view/DashboardAdmin.java](src/main/java/com/binakarya/absensi/view/DashboardAdmin.java#L60-L72)

---

**Catatan implementasi & rekomendasi singkat**
- Serial dua-arah belum diimplementasikan; untuk menambah, tambahkan panggilan `rfidPort.getOutputStream().write(...)` atau `rfidPort.writeBytes(...)` pada lokasi yang sesuai (mis. pada aksi tombol atau setelah autentikasi).
- Kriptografi: `EncryptionUtils` menggunakan AES/ECB dengan key statis — untuk produksi sebaiknya gunakan mode yang lebih aman (CBC/GCM) dan kelola key secara aman.

---

**Instruksi singkat menjalankan (dev)**
1. Pastikan MongoDB berjalan.
2. Build & run melalui IDE (NetBeans) atau `mvn package` lalu jalankan `MainApp` atau `TestKoneksi` untuk cek koneksi.

---

Jika Anda mau, saya bisa:
- Konversi file ini menjadi PDF dan tambahkan ke repo, atau
- Buat versi slide (Markdown -> reveal.js) untuk presentasi.

File ini dibuat otomatis untuk keperluan pembacaan/presentasi.
