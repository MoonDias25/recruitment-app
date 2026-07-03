package com.authserver.server.Repos;

import com.authserver.server.PromRequestStatus;
import com.authserver.server.entity.PromotionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromotionRequestRepository extends JpaRepository<PromotionRequest, String> {

    List<PromotionRequest> findByStatus(PromRequestStatus status);
}
