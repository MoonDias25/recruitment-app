package com.backend.backend.listener;

import com.backend.backend.dto.AdminDecisionDTO;
import com.backend.backend.service.PromotionRequestService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PromotionDecisionListener {

    private final PromotionRequestService promotionRequestService;

    public PromotionDecisionListener(PromotionRequestService promotionRequestService) {
        this.promotionRequestService = promotionRequestService;
    }

    @KafkaListener(
            topics = "promotion_decisions-topic",
            groupId = "resource-promotion-group",
            containerFactory = "adminDecisionKafkaListenerContainerFactory"
    )
    public void consumeAdminDecision(AdminDecisionDTO decision) {
        System.out.println("Received Admin Decision from Kafka for request: " + decision.getRequestId());

        promotionRequestService.updateRequestStatusFromKafka(decision);
    }
}
