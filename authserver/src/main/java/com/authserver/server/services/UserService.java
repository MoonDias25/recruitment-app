package com.authserver.server.services;

import com.authserver.server.Exception;
import com.authserver.server.Repos.AuthorityRepository;
import com.authserver.server.Repos.UserRepository;
import com.authserver.server.dto.UserEditDto;
import com.authserver.server.entity.Authority;
import com.authserver.server.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @Transactional(readOnly = true)
    public User findUserById(String id) {

        return userRepository.findUserById(id).orElseThrow(
                ()-> new Exception.ResourceNotFoundException("Could not find user with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<User> getAllEmployees() {
        Authority userAuthority = authorityRepository.findByAuthorityName("ROLE_USER").orElseThrow(
                () -> new Exception.ResourceNotFoundException("Authority not found"));

        Authority hrAuthority = authorityRepository.findByAuthorityName("ROLE_HR").orElseThrow(
                () -> new Exception.ResourceNotFoundException("Authority not found"));

        List<String> authorityNames = Arrays.asList(userAuthority.getAuthorityName(), hrAuthority.getAuthorityName());

        return userRepository.findAllByAuthorityAuthorityNameIn(authorityNames);
    }

    @Transactional(readOnly = true)
    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(
                () -> new Exception.ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public void updateEmployee(String id, UserEditDto dto) {
        User existingUser = userRepository.findById(id).orElseThrow(
                () -> new Exception.ResourceNotFoundException("User not found with id: " + id));

        existingUser.setFirstName(dto.getFirstName());
        existingUser.setLastName(dto.getLastName());
        existingUser.setEmail(dto.getEmail());
        existingUser.setPhoneNumber(dto.getPhoneNumber());
        existingUser.setBirthDate(dto.getBirthDate());
        existingUser.setActive(dto.isActive());

        String dbAuthorityName = "ROLE_" + dto.getRoleName();

        Authority managedAuth = authorityRepository.findByAuthorityName(dbAuthorityName).orElseThrow(
                () -> new Exception.ResourceNotFoundException("Authority not found in database: " + dbAuthorityName));

        existingUser.setAuthority(managedAuth);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<String> getAllAvailableRoles() {
        return Arrays.asList("USER", "HR", "ADMIN", "SUPER_ADMIN");
    }

    @Transactional(readOnly = true)
    public Page<User> getUsersPaginatedAndFiltered(
            boolean isSuperAdmin, String email, int page, int size, String sortOption) {

        String sortBy = "lastName";
        Sort.Direction direction = Sort.Direction.ASC;

        if (sortOption != null && sortOption.contains("-")) {
            String[] parts = sortOption.split("-");
            sortBy = parts[0];
            direction = parts[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        }

        Sort sortObj = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        String emailFilter = (email != null && !email.trim().isEmpty()) ? email.trim() : null;

        if (isSuperAdmin) {
            List<String> allRoles = Arrays.asList("ROLE_USER", "ROLE_HR", "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
            return userRepository.findEmployeesWithEmailFilter(allRoles, emailFilter, pageable);
        } else {
            List<String> allowedRoles = Arrays.asList("ROLE_USER", "ROLE_HR");
            return userRepository.findEmployeesWithEmailFilter(allowedRoles, emailFilter, pageable);
        }
    }
}
