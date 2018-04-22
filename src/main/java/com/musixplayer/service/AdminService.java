package com.musixplayer.service;

import com.musixplayer.model.Admin;
import com.musixplayer.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Optional<Admin> findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public Admin findByConfirmationToken(String confirmationToken) {
        return adminRepository.findByConfirmationToken(confirmationToken);
    }

    public Admin create(Admin admin){
        return adminRepository.save(admin);
    }

    public List<Admin> findAll(){
        return adminRepository.findAll();
    }


}
