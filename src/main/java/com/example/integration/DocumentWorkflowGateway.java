package com.example.integration;

import com.example.domain.entity.Approval;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface DocumentWorkflowGateway {

    @Gateway(requestChannel = "approvalNotificationChannel")
    void sendForApproval(Approval approval);
}
