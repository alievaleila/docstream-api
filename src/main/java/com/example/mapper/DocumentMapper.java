package com.example.mapper;

import com.example.domain.entity.Document;
import com.example.dto.response.DocumentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface DocumentMapper {

    DocumentResponse toResponse(Document document);
}
