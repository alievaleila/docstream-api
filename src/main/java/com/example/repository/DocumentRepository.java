package com.example.repository;

import com.example.domain.entity.Document;
import com.example.domain.enums.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByStatus(DocumentStatus status);
    List<Document> findByOwnerId(Long ownerId);
}
