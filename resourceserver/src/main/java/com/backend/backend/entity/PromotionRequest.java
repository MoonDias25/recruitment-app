package com.backend.backend.entity;


import com.backend.backend.PromRequestStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name ="promotion_requests")
public class PromotionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private String id;

    @Column(name="target_user_id", nullable = false)
    private String targetUserId;

    @Column(name="hr_id", nullable = false)
    private String hrId;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="reviewed_at")
    private LocalDateTime reviewedAt;

    @Enumerated(EnumType.STRING)
    @Column(name="request_status", nullable = false)
    private PromRequestStatus status;

    @Column(name="hr_notes", length = 1000)
    private String hrNotes;

    @Column(name="admin_notes")
    private String adminNotes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getHrId() {
        return hrId;
    }

    public void setHrId(String hrId) {
        this.hrId = hrId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public PromRequestStatus getStatus() {
        return status;
    }

    public void setStatus(PromRequestStatus status) {
        this.status = status;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public String getHrNotes() {
        return hrNotes;
    }

    public void setHrNotes(String hrNotes) {
        this.hrNotes = hrNotes;
    }
}
