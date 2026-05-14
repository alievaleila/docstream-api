package com.example.service.impl;

import com.example.domain.entity.Approval;
import com.example.domain.entity.Document;
import com.example.domain.enums.ApprovalStatus;
import com.example.domain.enums.AuditAction;
import com.example.domain.enums.DocumentStatus;
import com.example.dto.response.ApprovalResponse;
import com.example.exception.BusinessException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.ApprovalMapper;
import com.example.repository.ApprovalRepository;
import com.example.repository.DocumentRepository;
import com.example.service.ApprovalService;
import com.example.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final DocumentRepository documentRepository;
    private final ApprovalMapper approvalMapper;
    private final AuditService auditService;

    @Override
    @Transactional
    public ApprovalResponse approve(Long approvalId, String approverEmail) {
        log.info("Processing approval id: {} by: {}", approvalId, approverEmail);
        Approval approval = findAndValidate(approvalId, approverEmail);

        approval.setStatus(ApprovalStatus.APPROVED);
        Document document = approval.getDocument();
        document.setStatus(DocumentStatus.APPROVED);
        documentRepository.save(document);

        Approval saved = approvalRepository.save(approval);
        log.info("Document '{}' approved by '{}'", document.getTitle(), approverEmail);

        auditService.log(AuditAction.DOCUMENT_APPROVED, approverEmail,
                "Document approved: " + document.getTitle());

        return approvalMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ApprovalResponse reject(Long approvalId, String comment, String approverEmail) {
        log.info("Processing rejection id: {} by: {}", approvalId, approverEmail);
        Approval approval = findAndValidate(approvalId, approverEmail);

        approval.setStatus(ApprovalStatus.REJECTED);
        approval.setComment(comment);
        Document document = approval.getDocument();
        document.setStatus(DocumentStatus.REJECTED);
        documentRepository.save(document);

        Approval saved = approvalRepository.save(approval);
        log.info("Document '{}' rejected by '{}'", document.getTitle(), approverEmail);

        auditService.log(AuditAction.DOCUMENT_REJECTED, approverEmail,
                "Document rejected: " + document.getTitle() + ". Reason: " + comment);

        return approvalMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApprovalResponse> getByDocument(Long documentId) {
        log.debug("Fetching approvals for document id: {}", documentId);
        return approvalRepository.findByDocumentId(documentId).stream()
                .map(approvalMapper::toResponse)
                .toList();
    }

    private Approval findAndValidate(Long approvalId, String approverEmail) {
        Approval approval = approvalRepository.findById(approvalId)
                .orElseThrow(() -> new ResourceNotFoundException("Approval", approvalId));

        if (!approval.getApprover().getEmail().equalsIgnoreCase(approverEmail)) {
            log.warn("Unauthorized approval attempt: approval id={}, requester={}", approvalId, approverEmail);
            throw new BusinessException("You are not authorized for this approval", HttpStatus.FORBIDDEN);
        }

        if (approval.getStatus() != ApprovalStatus.PENDING) {
            log.warn("Approval already processed: id={}, status={}", approvalId, approval.getStatus());
            throw new BusinessException("Approval already processed", HttpStatus.CONFLICT);
        }

        return approval;
    }
}
