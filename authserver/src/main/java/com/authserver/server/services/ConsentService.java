package com.authserver.server.services;

import com.authserver.server.repos.UserRepository;
import com.authserver.server.dto.UserDuplicationDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

public class ConsentService implements OAuth2AuthorizationConsentService {

    private final OAuth2AuthorizationConsentService delegate;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ConsentService(JdbcTemplate jdbcTemplate,
                          RegisteredClientRepository clientRepository,
                          UserRepository userRepository,
                          KafkaTemplate<String, Object> kafkaTemplate) {
        this.delegate = new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, clientRepository);
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void save(OAuth2AuthorizationConsent consent) {
        try {
            this.delegate.save(consent);
            System.out.println("[Consent] Saved successfully in oauth2_authorization_consent table.");
        } catch (Exception e) {
            System.err.println("CRITICAL: Failed to save consent in DB: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        try {
            String email = consent.getPrincipalName();
            System.out.println("[Consent] Processing Kafka sync for principal: " + email);

            userRepository.findByEmail(email).ifPresentOrElse(user -> {

                String userIdStr = (user.getId() != null) ? user.getId() : "";

                UserDuplicationDTO event = new UserDuplicationDTO(
                        userIdStr,
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getBirthDate()
                );

                kafkaTemplate.send("user-registrations", userIdStr, event)
                        .whenComplete((result, ex) -> {
                            if (ex == null) {
                                System.out.println("-> Kafka: Successfully replicated user ID: " + userIdStr);
                            } else {
                                System.err.println("-> Kafka ERROR: Message failed to send: " + ex.getMessage());
                            }
                        });
            }, () -> {
                System.err.println("-> ERROR: Consent given, but user with email " + email + " was not found in UserRepository!");
            });

        } catch (Exception e) {
            System.err.println("-> ERROR during Kafka replication process (Consent is still valid): " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void remove(OAuth2AuthorizationConsent consent) {
        this.delegate.remove(consent);
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        return this.delegate.findById(registeredClientId, principalName);
    }
}


