package com.backend.backend.controller;

import com.backend.backend.dto.UserProfileDTO;
import com.backend.backend.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/search-candidate")
    @PreAuthorize("hasAuthority('ROLE_HR')")
    public ResponseEntity<UserProfileDTO> searchCandidateForPromotion( @RequestParam String email,
            Authentication authentication) {

        UserProfileDTO candidate = userProfileService.getUserForPromotionByEmail(email, authentication);
        return ResponseEntity.ok(candidate);
    }
}
