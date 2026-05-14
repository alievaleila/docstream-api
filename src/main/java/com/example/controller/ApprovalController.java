package com.example.controller;

import com.example.dto.request.RejectRequest;
import com.example.dto.response.ApprovalResponse;
import com.example.service.ApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Approvals", description = "Təsdiq əməliyyatları")
@SecurityRequirement(name = "bearerAuth")
public class ApprovalController {

    private final ApprovalService approvalService;

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('APPROVER')")
    @Operation(summary = "Sənədi təsdiqlə")
    public ResponseEntity<ApprovalResponse> approve(
            @PathVariable Long id,
            @AuthenticationPrincipal String approverEmail) {
        log.info("Approval request for approval id: {} by: {}", id, approverEmail);
        ApprovalResponse response = approvalService.approve(id, approverEmail);
        log.info("Document approved, approval id: {}", id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('APPROVER')")
    @Operation(summary = "Sənədi rədd et")
    public ResponseEntity<ApprovalResponse> reject(
            @PathVariable Long id,
            @Valid @RequestBody RejectRequest request,
            @AuthenticationPrincipal String approverEmail) {
        log.info("Rejection request for approval id: {} by: {}", id, approverEmail);
        ApprovalResponse response = approvalService.reject(id, request.comment(), approverEmail);
        log.info("Document rejected, approval id: {}", id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/document/{documentId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Sənədin bütün approval-ları")
    public ResponseEntity<List<ApprovalResponse>> getByDocument(@PathVariable Long documentId) {
        log.info("Fetching approvals for document id: {}", documentId);
        return ResponseEntity.ok(approvalService.getByDocument(documentId));
    }
}
