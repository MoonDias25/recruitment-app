package com.authserver.server.component;

import com.authserver.server.dto.PromotionRequestEventDTO;
import com.authserver.server.services.PromotionRequestService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PromotionRequestListener {

    private final PromotionRequestService promotionRequestService;

    public PromotionRequestListener(PromotionRequestService promotionRequestService) {
        this.promotionRequestService = promotionRequestService;
    }

    @KafkaListener(
            topics = "promotion-requests-topic",
            groupId = "auth-promotion-group",
            containerFactory = "promotionKafkaListenerContainerFactory"
    )
    public void consumePromotionRequest(PromotionRequestEventDTO event) {
        System.out.println("Received new promotion request from Kafka: " + event.getId());

        System.out.println("id: " + event.getId() + " userid: " + event.getTargetUserId() + " hrnotes: " + event.getHrNotes());

        promotionRequestService.saveIncomingRequestFromKafka(
                event.getId(),
                event.getTargetUserId(),
                event.getHrNotes()
        );
    }
}
