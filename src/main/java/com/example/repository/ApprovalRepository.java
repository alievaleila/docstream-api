package com.example.repository;

import com.example.domain.entity.Approval;
import com.example.domain.enums.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {

    List<Approval> findByDocumentId(Long documentId);
    List<Approval> findByApproverId(Long approverId);
    boolean existsByDocumentIdAndStatus(Long documentId, ApprovalStatus status);
}
