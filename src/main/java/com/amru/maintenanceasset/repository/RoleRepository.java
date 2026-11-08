package com.amru.maintenanceasset.repository;

import com.amru.maintenanceasset.entity.Role;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class RoleRepository implements PanacheRepository<Role> {

    public Role findByName(String name) {

        return find("name", name).firstResult();

    }

    public List<Role> findActiveRoles() {

        return list("isActive", true);

    }

}
