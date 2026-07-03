package com.backend.backend.service;

import com.backend.backend.dto.UserDuplicationDTO;
import com.backend.backend.entity.UserProfile;
import com.backend.backend.repository.UserProfileRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserRegistrationService {

    private final UserProfileRepository userProfileRepository;

    public UserRegistrationService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @KafkaListener(topics = "user-registrations", groupId = "business-profile-group")
    public void consumeUserRegistration(UserDuplicationDTO dto) {
        System.out.println("Received event for user ID: " + dto.getUserId());

        try {
            Optional<UserProfile> existingProfile = userProfileRepository.findById(dto.getUserId());

            if (existingProfile.isPresent()) {
                System.out.println("UserProfile already exists for ID: " + dto.getUserId() + ". Skipping execution.");
                return;
            }

            UserProfile profile = new UserProfile();
            profile.setId(dto.getUserId());
            profile.setFirstName(dto.getFirstName());
            profile.setLastName(dto.getLastName());
            profile.setEmail(dto.getEmail());
            profile.setPhoneNumber(dto.getPhoneNumber());
            profile.setBirthDate(dto.getBirthDate());

            userProfileRepository.save(profile);
            System.out.println("Successfully created UserProfile for ID: " + dto.getUserId());

        } catch (Exception e) {
            System.err.println("Error creating UserProfile via Kafka (Handled pașnic): " + e.getMessage());
        }
    }
}
