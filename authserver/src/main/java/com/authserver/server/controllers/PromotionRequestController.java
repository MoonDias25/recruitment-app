package com.authserver.server.controllers;

import com.authserver.server.enums.PromRequestStatus;
import com.authserver.server.dto.AdminDecisionDTO;
import com.authserver.server.dto.PromotionRequestDetailDTO;
import com.authserver.server.services.PromotionRequestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/promotions")
@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
public class PromotionRequestController {

    private final PromotionRequestService promotionRequestService;

    public PromotionRequestController(PromotionRequestService promotionRequestService) {
        this.promotionRequestService = promotionRequestService;
    }

    @GetMapping("/pending")
    public String showPendingRequests(Model model) {
        List<PromotionRequestDetailDTO> pendingRequests = promotionRequestService.getPendingRequestsForAdmin();
        model.addAttribute("requests", pendingRequests);
        model.addAttribute("decisionForm", new AdminDecisionDTO());
        return "admin/promotions";
    }

    @PostMapping("/decision")
    public String handleAdminDecision(
            @RequestParam("requestId") String requestId,
            @RequestParam("status") String status,
            @RequestParam(value = "adminNotes", required = false) String adminNotes) {

        AdminDecisionDTO decision = new AdminDecisionDTO();
        decision.setRequestId(requestId);
        decision.setStatus(PromRequestStatus.valueOf(status));
        decision.setAdminNotes(adminNotes != null ? adminNotes : "");

        promotionRequestService.processAdminDecision(decision);

        return "redirect:/admin/promotions/pending";
    }
}
