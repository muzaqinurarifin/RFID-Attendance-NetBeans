/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.binakarya.absensi.service;

/**
 * Authentication service for admin login.
 */
public class AuthService {
    private final AdminService adminService;

    public AuthService() {
        this.adminService = new AdminService();
        this.adminService.siapkanAdminAwal();
    }

    public boolean login(String username, String password) {
        return adminService.cekLogin(username, password);
    }
}
