package com.example.service;


import com.example.domain.entity.Approval;

public interface NotificationService {

    void sendApprovalRequest(Approval approval);
}