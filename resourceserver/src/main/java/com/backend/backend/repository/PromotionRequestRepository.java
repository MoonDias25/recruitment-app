package com.backend.backend.repository;

import com.backend.backend.enums.PromRequestStatus;
import com.backend.backend.entity.PromotionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromotionRequestRepository extends JpaRepository<PromotionRequest, String> {

    long countByStatus(PromRequestStatus status);

    List<PromotionRequest> findByStatusOrderByCreatedAtDesc(PromRequestStatus status);

}
