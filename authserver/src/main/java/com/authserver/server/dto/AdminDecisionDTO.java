package com.authserver.server.dto;

import com.authserver.server.PromRequestStatus;

public class AdminDecisionDTO {

    private String requestId;
    private PromRequestStatus status;
    private String adminNotes;

    public AdminDecisionDTO() {}

    public AdminDecisionDTO(String requestId, PromRequestStatus status, String adminNotes) {
        this.requestId = requestId;
        this.status = status;
        this.adminNotes = adminNotes;
    }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public PromRequestStatus getStatus() { return status; }
    public void setStatus(PromRequestStatus status) { this.status = status; }

    public String getAdminNotes() { return adminNotes; }
    public void setAdminNotes(String adminNotes) { this.adminNotes = adminNotes; }
}
