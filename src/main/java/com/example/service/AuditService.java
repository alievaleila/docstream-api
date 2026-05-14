package com.example.service;

import com.example.domain.enums.AuditAction;
import com.example.dto.response.AuditLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditService {

    void log(AuditAction action, String actor, String details);

    Page<AuditLogResponse> getAll(Pageable pageable);
}
