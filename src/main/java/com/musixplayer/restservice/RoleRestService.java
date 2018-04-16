package com.musixplayer.restservice;

import com.musixplayer.model.Role;
import com.musixplayer.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class RoleRestService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleRestService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @GetMapping("/api/role")
    public List<Role> findAllUsers() {
        return (List<Role>) roleRepository.findAll();
    }

    @GetMapping("/api/role/{role_name}")
    public Optional<Role> findAllUsers(@PathVariable("role_name") String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

}
