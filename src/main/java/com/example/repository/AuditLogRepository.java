package com.example.repository;

import com.example.domain.entity.AuditLog;
import com.example.domain.enums.AuditAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findByActor(String actor, Pageable pageable);

    Page<AuditLog> findByAction(AuditAction action, Pageable pageable);
}
