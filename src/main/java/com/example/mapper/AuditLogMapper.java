package com.example.mapper;

import com.example.audit.AuditLog;
import com.example.dto.response.AuditLogResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {

    AuditLogResponse toResponse(AuditLog auditLog);
}
