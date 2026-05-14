package com.example.service.impl;

import com.example.domain.entity.Approval;
import com.example.domain.entity.Document;
import com.example.domain.entity.User;
import com.example.domain.enums.ApprovalStatus;
import com.example.domain.enums.AuditAction;
import com.example.domain.enums.DocumentStatus;
import com.example.domain.enums.Role;
import com.example.dto.request.DocumentRequest;
import com.example.dto.response.DocumentResponse;
import com.example.event.DocumentSubmittedEvent;
import com.example.exception.BusinessException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.DocumentMapper;
import com.example.repository.ApprovalRepository;
import com.example.repository.DocumentRepository;
import com.example.repository.UserRepository;
import com.example.service.AuditService;
import com.example.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final ApprovalRepository approvalRepository;
    private final DocumentMapper documentMapper;
    private final AuditService auditService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public DocumentResponse submit(DocumentRequest request, String ownerEmail) {
        log.info("Submitting document '{}' by '{}'", request.title(), ownerEmail);

        User owner = userRepository.findByEmailIgnoreCase(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", -1L));

        List<User> approvers = userRepository.findByRole(Role.APPROVER);
        if (approvers.isEmpty()) {
            log.warn("No approvers found in the system");
            throw new BusinessException("No approvers found in the system", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Document document = Document.builder()
                .title(request.title())
                .content(request.content())
                .owner(owner)
                .status(DocumentStatus.PENDING_APPROVAL)
                .build();

        Document saved = documentRepository.save(document);
        log.info("Document saved with id: {}", saved.getId());

        List<Approval> approvals = approvers.stream()
                .map(approver -> Approval.builder()
                        .document(saved)
                        .approver(approver)
                        .status(ApprovalStatus.PENDING)
                        .build())
                .toList();

        approvalRepository.saveAll(approvals);
        log.info("Created {} approval record(s) for document id: {}", approvals.size(), saved.getId());

        auditService.log(AuditAction.DOCUMENT_SUBMITTED, ownerEmail,
                "Document submitted: " + saved.getTitle());

        eventPublisher.publishEvent(new DocumentSubmittedEvent(this, approvals));

        return documentMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentResponse getById(Long id) {
        log.debug("Fetching document by id: {}", id);
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document", id));
        return documentMapper.toResponse(doc);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentResponse> getAll() {
        log.debug("Fetching all documents");
        return documentRepository.findAll().stream()
                .map(documentMapper::toResponse)
                .toList();
    }
}