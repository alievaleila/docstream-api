package com.example.repository;

import com.example.audit.AuditAction;
import com.example.audit.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogRepository {

    Page<AuditLog> findByActor(String actor, Pageable pageable);

    Page<AuditLog> findByAction(AuditAction action, Pageable pageable);
}
