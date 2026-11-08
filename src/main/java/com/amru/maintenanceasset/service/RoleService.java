package com.amru.maintenanceasset.service;

import com.amru.maintenanceasset.dto.RoleResponse;
import com.amru.maintenanceasset.entity.Role;
import com.amru.maintenanceasset.exception.DuplicateRoleException;
import com.amru.maintenanceasset.exception.InvalidIdExceptionMapper;
import com.amru.maintenanceasset.exception.ResourceNotFoundException;
import com.amru.maintenanceasset.exception.BusinessRuleException;
import com.amru.maintenanceasset.repository.RoleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import com.amru.maintenanceasset.dto.RoleRequest;
import jakarta.transaction.Transactional;

import io.quarkus.narayana.jta.QuarkusTransaction;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
//@Transactional
public class RoleService {

    @Inject
    RoleRepository roleRepository;

    // get all role
    public List<RoleResponse> getAllRoles() {

        List<Role> roles = roleRepository.listAll();

        return roles.stream()
                .map(this::mapToResponse)
                .toList();

    }

    // get role by id
    public RoleResponse getRoleById(Long id) {

        Role role = roleRepository.findById(id);

        if (role == null) {
            throw new ResourceNotFoundException(
                    "Role with id " + id + " not found"
            );
        }

        return mapToResponse(role);

    }

    // store new data role
    @Transactional
    public RoleResponse createRole(RoleRequest request) {

        Role existingRole = roleRepository.findByName(request.getName());

        if (existingRole != null) {
            throw new DuplicateRoleException(
                    "Role name `" + request.getName() + "` already exists"
            );
        }

        Role role = new Role();

        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setIsActive(true);

        OffsetDateTime now = OffsetDateTime.now();

        role.setCreatedAt(now);
        role.setUpdatedAt(now);

        roleRepository.persist(role);

        return mapToResponse(role);
    }

    // update data role
    @Transactional
    public RoleResponse updateRole(Long id, RoleRequest request) {

        Role role = roleRepository.findById(id);

        if (role == null) {
            throw new ResourceNotFoundException(
                    "Role with id " + id +  " not found"
            );
        }

        Role existingRole = roleRepository.findByName(request.getName());

        if (existingRole != null && !existingRole.getId().equals(id)) {
            throw new DuplicateRoleException(
                    "Role name `" + request.getName() + "` already exists"
            );
        }

        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setUpdatedAt(OffsetDateTime.now());

        return mapToResponse(role);

    }

    // deactivate role
    @Transactional
    public RoleResponse deactivateRole(Long id) {

        Role role = roleRepository.findById(id);

        if (role == null) {

            throw new ResourceNotFoundException(
                    "Role with id " + id + " not found"
             );

        }

        if (!role.getIsActive()) {

            throw new BusinessRuleException(
                    "Role with id " + id + " is already inactive"
            );

        }

        if ("SUPER_ADMIN".equals(role.getName())) {

            long activeSuperAdminCount = roleRepository.count(
                    "name = ?1 and isActive = true",
                    "SUPER_ADMIN"
            );

            if (activeSuperAdminCount <= 1) {
                throw new BusinessRuleException(
                        "Cannot deactivate the only active SUPER_ADMIN "
                );
            }

        }

        role.setIsActive(false);
        role.setUpdatedAt(OffsetDateTime.now());

        return mapToResponse(role);

    }

    // reactivate role
    @Transactional
    public RoleResponse activateRole(Long id) {

        Role role = roleRepository.findById(id);

        if (role ==  null) {
            throw new ResourceNotFoundException(
                    "Role with id " + id + " not found"
            );
        }

        if (Boolean.TRUE.equals(role.getIsActive())) {
            throw new BusinessRuleException(
                    "Role with id " + id + " is already active"
            );
        }

        role.setIsActive(true);
        role.setUpdatedAt(OffsetDateTime.now());

        return mapToResponse(role);

    }

    // get role active only
    public List<RoleResponse> getActiveRoles() {

        return roleRepository.findActiveRoles()
                .stream().map(
                        this::mapToResponse
                ).toList();

    }

    // mapping data role
    private RoleResponse mapToResponse(Role role) {

        RoleResponse response = new RoleResponse();

        response.setId(role.getId());
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        response.setIsActive(role.getIsActive());
        response.setCreatedAt(role.getCreatedAt());
        response.setUpdatedAt(role.getUpdatedAt());

        return response;

    }

//    public RoleResponse createRole(RoleRequest request) {
//        return QuarkusTransaction.requiringNew().call(() -> {
//            Role role = new Role();
//            role.setName(request.getName());
//            role.setDescription(request.getDescription());
//            role.setIsActive(true);
//
//            roleRepository.persist(role);
//            return mapToResponse(role);
//        });
//    }

}
