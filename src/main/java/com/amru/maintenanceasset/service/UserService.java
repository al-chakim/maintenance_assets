package com.amru.maintenanceasset.service;

import com.amru.maintenanceasset.dto.UserResponse;
import com.amru.maintenanceasset.dto.UserUpdateRequest;
import com.amru.maintenanceasset.entity.User;
import com.amru.maintenanceasset.exception.ResourceNotFoundException;
import com.amru.maintenanceasset.repository.UserRepository;
import com.amru.maintenanceasset.dto.UserRequest;
import com.amru.maintenanceasset.entity.Role;
import com.amru.maintenanceasset.exception.DuplicateRoleException;
import com.amru.maintenanceasset.repository.RoleRepository;
import com.amru.maintenanceasset.exception.DuplicateUserException;
import com.amru.maintenanceasset.exception.InactiveRoleException;
import com.amru.maintenanceasset.exception.BusinessRuleException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.ws.rs.Path;

import java.time.OffsetDateTime;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    RoleRepository roleRepository;

    // method get all data user
    @Transactional
    public List<UserResponse> getAllusers() {

        return userRepository.listAll()
                .stream().map(
                        this::mapToResponse
                ).toList();

    }

    // method get data user by id
    @Transactional
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id);

        if (user == null) {
            throw new ResourceNotFoundException(
                    "User with id " + id + " not found"
            );
        }

        return mapToResponse(user);
    }

    // method create new data user
    @Transactional
    public UserResponse createUser(UserRequest request) {

        // 1. cek email existing
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new DuplicateUserException(
                    "Email is already registered"
            );
        }

        // 2. search role by id
        Role role = roleRepository.findById(request.getRoleId());

        if (role == null) {
            throw new ResourceNotFoundException(
                    "Role with id " + request.getRoleId() + " not found"
            );
        }

        // 3. check role active
        if (!Boolean.TRUE.equals(role.getIsActive())) {
            throw new DuplicateUserException(
                    "Cannot assign an inactive role"
            );
        }

        // 4. add entity user
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(role);
        user.setIsActive(true);

        // 5. hash password using BCrypt
        String passwordHash = BCrypt.withDefaults()
                .hashToString(12, request.getPassword()
                        .toCharArray());

        user.setPassword(passwordHash);

        // 6. fill timestamp
        OffsetDateTime now = OffsetDateTime.now();

        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        // 7. save new data user to database
        userRepository.persist(user);

        // 8. return response without password
        return mapToResponse(user);

    }

    // method update data user
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {

        // 1. ensure the user to be updated exists
        User user = userRepository.findById(id);

        if (user == null) {
            throw new ResourceNotFoundException(
                    "Uaer with id " + id + " not found"
            );
        }

        // 2. check the email is already in use by another user
        User existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser != null && !existingUser.getId().equals(id)) {

            throw new DuplicateUserException(
                    "Email is already registered"
            );

        }

        // 3. ensure the target role available
        Role role = roleRepository.findById(request.getRoleId());

        if (role == null) {

            throw new ResourceNotFoundException(
                    "Role with id " + request.getRoleId() + " not found"
            );

        }

        // 4. Inactive rle should not be assigned
        if (!Boolean.TRUE.equals(role.getIsActive())) {

            throw new InactiveRoleException(
                    "Cannot assign an inactive role"
            );

        }

        // 5. update allowed fields
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(role);
        user.setUpdatedAt(OffsetDateTime.now());

        return mapToResponse(user);

    }

    // method deactivate user
    @Transactional
    public UserResponse deactivateUser(Long id) {

        // 1. find user by id
        User user = userRepository.findById(id);

        if (user == null) {

            throw new ResourceNotFoundException(
                    "User with id " + id + " not found"
            );

        }

        // 2. reject if the user is already inactive
        if (!Boolean.TRUE.equals(user.getIsActive())) {

            throw new BusinessRuleException(
                    "User is already inactive"
            );

        }

        // 3. prevent deactivation of the last active SUPER_ADMIN
        if (user.getRole() != null
                && "SUPER_ADMIN".equals(user.getRole().getName())
                && Boolean.TRUE.equals(user.getRole().getIsActive())) {

            long activeSuperAdminCount = userRepository.count(
                    "isActive = true and role.name = ?1 and role.isActive = true",
                    "SUPER_ADMIN"
            );

            if (activeSuperAdminCount <= 1) {
                throw new BusinessRuleException(
                        "Cannot deactivate the last active SUPER_ADMIN user"
                );
            }
        }

        // 4. Inactivate user
        user.setIsActive(false);
        user.setUpdatedAt(OffsetDateTime.now());

        return mapToResponse(user);

    }

    // methode reactivate user
    @Transactional
    public UserResponse activateUser(Long id) {

        // 1. find user by id
        User user = userRepository.findById(id);

        if (user == null) {
            throw new ResourceNotFoundException(
                    "User with id " + id + " not found"
            );
        }

        // 2. reject if the user is active
        if (Boolean.TRUE.equals(user.getIsActive())) {

            throw new BusinessRuleException(
                    "User is already active"
            );

        }

        // 3. ensure the role user is available and active
        Role role = user.getRole();

        if (role == null || !Boolean.TRUE.equals(role.getIsActive())) {

            throw new InactiveRoleException(
                    "Cannot activate user with an inactive role"
            );

        }

        // 4. reactivate user
        user.setIsActive(true);
        user.setUpdatedAt(OffsetDateTime.now());

        return mapToResponse(user);

    }

    // mapper
    private UserResponse mapToResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRoleId(user.getRole().getId());
        response.setRoleName(user.getRole().getName());
        response.setActive(user.getIsActive());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;

    }

}
