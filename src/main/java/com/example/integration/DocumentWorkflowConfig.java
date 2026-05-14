package com.example.integration;

import com.example.domain.entity.Approval;
import com.example.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableIntegration
@Slf4j
public class DocumentWorkflowConfig {

    @Bean
    public MessageChannel approvalNotificationChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow approvalNotificationFlow(NotificationService notificationService) {
        return IntegrationFlow
                .from(approvalNotificationChannel())
                .<Approval>handle((approval, headers) -> {
                    log.info("Integration flow triggered for approval id={}", approval.getId());
                    notificationService.sendApprovalRequest(approval);
                    return null;
                })
                .get();
    }
}