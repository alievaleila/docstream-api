package com.example.event;

import com.example.domain.entity.Approval;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class DocumentSubmittedEvent extends ApplicationEvent {

    private final List<Approval> approvals;

    public DocumentSubmittedEvent(Object source, List<Approval> approvals) {
        super(source);
        this.approvals = approvals;
    }

    public List<Approval> getApprovals() {
        return approvals;
    }
}