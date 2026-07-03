package com.backend.backend.controller;

import com.backend.backend.dto.NewPromotionRequestDTO;
import com.backend.backend.response.ApiResponse;
import com.backend.backend.service.PromotionRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/promotion")
public class PromotionRequestController {

    private final PromotionRequestService promotionRequestService;

    public PromotionRequestController(PromotionRequestService promotionRequestService) {
        this.promotionRequestService = promotionRequestService;
    }

    @PostMapping("/submit")
    @PreAuthorize("hasAuthority('ROLE_HR')")
    public ResponseEntity<ApiResponse> submitPromotionRequest(
            @RequestBody NewPromotionRequestDTO dto,
            Authentication authentication){

        promotionRequestService.createPromotionRequest(dto, authentication);
        ApiResponse response = new ApiResponse("Promotion request submitted successfully!", HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending-count")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Long>> getPendingRequestsCount() {
        long count = promotionRequestService.getPendingRequestsCount();

        Map<String, Long> response = new HashMap<>();
        response.put("count", count);

        return ResponseEntity.ok(response);
    }
}
