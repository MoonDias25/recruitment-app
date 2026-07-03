package com.authserver.server.entity;

import com.authserver.server.PromRequestStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="promotion_request")
public class PromotionRequest {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name= "hr_notes", length = 1000)
    private String hrNotes;

    @Column(name= "admin_notes", length = 1000)
    private String adminNotes;

    @Column(name= "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PromRequestStatus status;

    @Column(name= "processed_at")
    private LocalDateTime processedAt;

    public PromotionRequest() {
    }

    public PromotionRequest(String id, String userId, String hrNotes, PromRequestStatus status) {
        this.id = id;
        this.userId = userId;
        this.hrNotes = hrNotes;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHrNotes() {
        return hrNotes;
    }

    public void setHrNotes(String hrNotes) {
        this.hrNotes = hrNotes;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public PromRequestStatus getStatus() {
        return status;
    }

    public void setStatus(PromRequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
}
