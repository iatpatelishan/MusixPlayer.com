package com.musixplayer.service;

import com.musixplayer.model.Role;
import com.musixplayer.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findById(Long role_id) {
        return roleRepository.findById(role_id).orElse(null);
    }

    public Role findByRoleName(String role_name) {
        System.out.println(role_name);
        return roleRepository.findByRoleName(role_name).orElse(null);
    }

}
