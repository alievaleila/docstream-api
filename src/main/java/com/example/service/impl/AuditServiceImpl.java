package com.example.service.impl;

import com.example.domain.entity.AuditLog;
import com.example.domain.enums.AuditAction;
import com.example.dto.response.AuditLogResponse;
import com.example.mapper.AuditLogMapper;
import com.example.repository.AuditLogRepository;
import com.example.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    public void log(AuditAction action, String actor, String details) {
        AuditLog entry = AuditLog.builder()
                .action(action)
                .actor(actor)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
        auditLogRepository.save(entry);
        log.info("[AUDIT] {} | {} | {}", action, actor, details);
    }

    @Override
    public Page<AuditLogResponse> getAll(Pageable pageable) {
        return auditLogRepository.findAll(pageable)
                .map(auditLogMapper::toResponse);
    }
}