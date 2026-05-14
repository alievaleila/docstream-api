package com.example.service;

import com.example.dto.response.ApprovalResponse;

import java.util.List;

public interface ApprovalService {

    ApprovalResponse approve(Long approvalId, String approverEmail);

    ApprovalResponse reject(Long approvalId, String comment, String approverEmail);

    List<ApprovalResponse> getByDocument(Long documentId);
}