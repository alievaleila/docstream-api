package com.example.service;


import com.example.dto.request.DocumentRequest;
import com.example.dto.response.DocumentResponse;

import java.util.List;

public interface DocumentService {

    DocumentResponse submit(DocumentRequest request, String ownerEmail);

    DocumentResponse getById(Long id);

    List<DocumentResponse> getAll();
}
