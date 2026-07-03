package com.backend.backend.service;

import com.backend.backend.dto.UserProfileDTO;
import com.backend.backend.entity.UserProfile;
import com.backend.backend.mapper.UserProfileMapper;
import com.backend.backend.repository.CandidateApplicationRepository;
import com.backend.backend.repository.UserProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final CandidateApplicationRepository candidateApplicationRepository;
    private final UserProfileMapper userProfileMapper;

    UserProfileService(UserProfileRepository userProfileRepository,
                       CandidateApplicationRepository candidateApplicationRepository, UserProfileMapper userProfileMapper){

        this.userProfileRepository = userProfileRepository;
        this.candidateApplicationRepository = candidateApplicationRepository;
        this.userProfileMapper = userProfileMapper;
    }

    @Transactional(readOnly = true)
    public UserProfileDTO getUserForPromotionByEmail(String email, Authentication authentication) {
        String currentHrId = authentication.getName();

        UserProfile user = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this email not found."));

        boolean hasAppliedToCurrentHr = candidateApplicationRepository
                .existsByUserIdAndReviewedBy(user.getId(), currentHrId);

        if (!hasAppliedToCurrentHr) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You can only request promotion for candidates who applied to your job offers.");
        }

        return userProfileMapper.toDto(user);
    }
}
