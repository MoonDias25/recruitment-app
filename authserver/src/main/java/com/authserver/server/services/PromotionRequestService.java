package com.authserver.server.services;

import com.authserver.server.enums.PromRequestStatus;
import com.authserver.server.repos.AuthorityRepository;
import com.authserver.server.repos.PromotionRequestRepository;
import com.authserver.server.repos.UserRepository;
import com.authserver.server.dto.AdminDecisionDTO;
import com.authserver.server.dto.PromotionRequestDetailDTO;
import com.authserver.server.entity.Authority;
import com.authserver.server.entity.PromotionRequest;
import com.authserver.server.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionRequestService {

    private final PromotionRequestRepository promotionRequestRepository;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PromotionRequestService(PromotionRequestRepository promotionRequestRepository, UserRepository userRepository, AuthorityRepository authorityRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.promotionRequestRepository = promotionRequestRepository;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional(readOnly = true)
    public List<PromotionRequestDetailDTO> getPendingRequestsForAdmin() {
        List<PromotionRequest> requests = promotionRequestRepository.findByStatus(PromRequestStatus.PENDING);
        List<PromotionRequestDetailDTO> dtos = new ArrayList<>();

        for (PromotionRequest request : requests) {
            User user = userRepository.findById(request.getUserId()).orElse(null);

            if (user != null) {
                PromotionRequestDetailDTO dto = new PromotionRequestDetailDTO(
                        request.getId(),
                        request.getUserId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getAuthority().getAuthorityName(),
                        request.getHrNotes(),
                        request.getAdminNotes(),
                        request.getStatus(),
                        request.getProcessedAt()
                );
                dtos.add(dto);
            }
        }
        return dtos;
    }

    @Transactional
    public void saveIncomingRequestFromKafka(String id, String userId, String hrNotes) {

        System.out.println("Request id" + id);
        System.out.println("UserId: " + userId);
        System.out.println("HRNotes: " + hrNotes);
        PromotionRequest newRequest = new PromotionRequest(id, userId, hrNotes, PromRequestStatus.PENDING);
        promotionRequestRepository.save(newRequest);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void processAdminDecision(AdminDecisionDTO decision) {

        PromotionRequest request = promotionRequestRepository.findById(decision.getRequestId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Promotion request not found"));

        if (request.getStatus() != PromRequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This request has already been processed");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (decision.getStatus() == PromRequestStatus.APPROVED) {

            String currentRole = user.getAuthority().getAuthorityName();

            if (!"ROLE_USER".equals(currentRole)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Promotion denied: Target user is already " + currentRole + " and cannot be promoted.");
            }

            Authority hrAuthority = authorityRepository.findByAuthorityName("ROLE_HR")
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "ROLE_HR authority not found in database"));

            user.setAuthority(hrAuthority);
            userRepository.save(user);
        }

        request.setStatus(decision.getStatus());
        request.setAdminNotes(decision.getAdminNotes());
        request.setProcessedAt(LocalDateTime.now());
        promotionRequestRepository.save(request);

        this.kafkaTemplate.send("promotion_decisions-topic", decision);
    }
}
