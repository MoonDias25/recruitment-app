package com.backend.backend.service;

import com.backend.backend.enums.PromRequestStatus;
import com.backend.backend.dto.AdminDecisionDTO;
import com.backend.backend.dto.NewPromotionRequestDTO;
import com.backend.backend.dto.PromotionRequestEventDTO;
import com.backend.backend.entity.PromotionRequest;
import com.backend.backend.repository.PromotionRequestRepository;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PromotionRequestService {

    private final PromotionRequestRepository promotionRequestRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PromotionRequestService(PromotionRequestRepository promotionRequestRepository,
                                   KafkaTemplate<String, Object> kafkaTemplate) {
        this.promotionRequestRepository = promotionRequestRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public void createPromotionRequest(NewPromotionRequestDTO dto, Authentication authentication) {
        String currentHrId = authentication.getName();

        PromotionRequest request = new PromotionRequest();
        request.setTargetUserId(dto.getTargetUserId());
        request.setHrId(currentHrId);
        request.setHrNotes(dto.getHrNotes());
        request.setCreatedAt(LocalDateTime.now());
        request.setStatus(PromRequestStatus.PENDING);

        promotionRequestRepository.save(request);

        PromotionRequestEventDTO event = new PromotionRequestEventDTO(
                request.getId(), request.getTargetUserId(), request.getHrNotes());

        kafkaTemplate.send("promotion-requests-topic", event);
    }

    @Transactional(readOnly = true)
    public long getPendingRequestsCount() {
        return promotionRequestRepository.countByStatus(PromRequestStatus.PENDING);
    }

    @Transactional
    public void updateRequestStatusFromKafka(AdminDecisionDTO decision) {
        PromotionRequest request = promotionRequestRepository.findById(decision.getRequestId())
                .orElseThrow(() -> new RuntimeException("Promotion request not found on Resource Server: " + decision.getRequestId()));

        request.setStatus(decision.getStatus());
        request.setAdminNotes(decision.getAdminNotes());
        request.setReviewedAt(LocalDateTime.now());

        promotionRequestRepository.save(request);

        System.out.println("Successfully updated request " + decision.getRequestId() + " to status " + decision.getStatus());
    }
}
