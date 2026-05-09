package com.example.mapper;

import com.example.domain.entity.Approval;
import com.example.dto.response.ApprovalResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DocumentMapper.class, UserMapper.class})
public interface ApprovalMapper {

    ApprovalResponse toResponse(Approval approval);
}
