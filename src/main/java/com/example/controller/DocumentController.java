package com.example.controller;

import com.example.dto.request.DocumentRequest;
import com.example.dto.response.DocumentResponse;
import com.example.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Documents", description = "Sənəd əməliyyatları")
@SecurityRequirement(name = "bearerAuth")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Yeni sənəd göndər")
    public ResponseEntity<DocumentResponse> submit(
            @Valid @RequestBody DocumentRequest request,
            @AuthenticationPrincipal String ownerEmail) {
        log.info("Document submission by user: {}, title: {}", ownerEmail, request.title());
        DocumentResponse response = documentService.submit(request, ownerEmail);
        log.info("Document submitted successfully, id: {}", response.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "ID ilə sənəd gətir")
    public ResponseEntity<DocumentResponse> getById(@PathVariable Long id) {
        log.info("Fetching document id: {}", id);
        return ResponseEntity.ok(documentService.getById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Bütün sənədlər (Yalnız Admin)")
    public ResponseEntity<List<DocumentResponse>> getAll() {
        log.info("Admin fetching all documents");
        return ResponseEntity.ok(documentService.getAll());
    }
}

