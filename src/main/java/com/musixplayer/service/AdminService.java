package com.musixplayer.service;

import com.musixplayer.model.Admin;
import com.musixplayer.model.Person;
import com.musixplayer.repository.AdminRepository;
import com.musixplayer.repository.PersonRepository;
import com.musixplayer.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository, RoleRepository roleRepository) {
        this.adminRepository = adminRepository;
        this.roleRepository = roleRepository;
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

    public Collection<Person> findAllPerson(){
        return adminRepository.findAllPerson();
    }


}
