package com.binakarya.absensi.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18nService {
    // 1. Variabel statis untuk menyimpan status bahasa yang sedang digunakan
    private static Locale currentLocale = new Locale("id", "ID");

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
    }

    // Fungsi untuk mengambil teks dari file .properties berdasarkan locale saat ini
    public static String get(String key) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", currentLocale);
        return bundle.getString(key);
    }
}