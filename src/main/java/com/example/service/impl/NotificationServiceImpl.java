package com.example.service.impl;

import com.example.domain.entity.Approval;
import com.example.domain.enums.AuditAction;
import com.example.service.AuditService;
import com.example.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;
    private final AuditService auditService;

    @Override
    @Retryable(
            retryFor = RuntimeException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void sendApprovalRequest(Approval approval) {
        String approverEmail = approval.getApprover().getEmail();
        String docTitle = approval.getDocument().getTitle();
        Long approvalId = approval.getId();

        log.info("Sending approval email to {} for '{}'", approverEmail, docTitle);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(approverEmail);
        msg.setSubject("Təsdiq Tələbi: " + docTitle);
        msg.setText("""
                Salam,
                
                Aşağıdakı sənəd sizin təsdiqlənməyinizi gözləyir:
                
                  Başlıq : %s
                  Müəllif: %s
                
                Qərar vermək üçün:
                  Təsdiqlə → POST /api/approvals/%d/approve
                  Rədd et  → POST /api/approvals/%d/reject
                
                Hörmətlə,
                DocumentFlow sistemi
                """.formatted(
                docTitle,
                approval.getDocument().getOwner().getEmail(),
                approvalId,
                approvalId
        ));

        mailSender.send(msg);

        auditService.log(
                AuditAction.EMAIL_SENT,
                approverEmail,
                "Approval email sent for document: " + docTitle
        );
    }

    @Recover
    public void recover(RuntimeException ex, Approval approval) {
        log.error("All retry attempts failed for approval id={}: {}",
                approval.getId(), ex.getMessage());

        auditService.log(
                AuditAction.EMAIL_FAILED,
                approval.getApprover().getEmail(),
                "Email delivery permanently failed for document: " + approval.getDocument().getTitle()
        );
    }
}