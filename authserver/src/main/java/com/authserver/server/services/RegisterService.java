package com.authserver.server.services;

import com.authserver.server.repos.AuthorityRepository;
import com.authserver.server.repos.UserRepository;
import com.authserver.server.dto.RegisterRequest;
import com.authserver.server.entity.Authority;
import com.authserver.server.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public RegisterService(UserRepository userRepository,
                           AuthorityRepository authorityRepository,
                           PasswordEncoder passwordEncoder,
                           EmailService emailService) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public void registerNewUser(RegisterRequest request) {
        User savedUser = saveUserInDatabase(request);

        try {
            emailService.sendConfirmationEmail(savedUser.getEmail(), savedUser.getFirstName());
        } catch (Exception e) {
            System.err.println("Error at mail sending: " + e.getMessage());
        }
    }

    private User saveUserInDatabase(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Error: Mail already in use!");
        }

        Authority defaultAuthority = authorityRepository.findByAuthorityName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: This authority doesn't exist!"));

        User newUser = new User();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setActive(true);
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setCreationDate(LocalDateTime.now());
        newUser.setAuthority(defaultAuthority);
        newUser.setBirthDate(request.getBirthDate());

        System.out.println(newUser.getPassword());
        return userRepository.save(newUser);
    }
}
