package com.authserver.server.controllers;

import com.authserver.server.dto.RegisterRequest;
import com.authserver.server.services.RegisterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody RegisterRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));

            return ResponseEntity.badRequest().body(Map.of("message", "Validation error: " + errorMessages));
        }

        registerService.registerNewUser(request);

        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully! Please check your email for confirmation."));
    }

}
