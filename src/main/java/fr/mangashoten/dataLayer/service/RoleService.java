package fr.mangashoten.dataLayer.service;

import fr.mangashoten.dataLayer.model.Role;
import fr.mangashoten.dataLayer.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Iterable<Role> getRoles() {
        return roleRepository.findAll();
    }
}
