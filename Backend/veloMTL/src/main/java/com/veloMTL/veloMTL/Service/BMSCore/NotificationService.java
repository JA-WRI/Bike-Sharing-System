package com.veloMTL.veloMTL.Service.BMSCore;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyOperators(String message) {
        messagingTemplate.convertAndSend("/topic/operator", message);
    }
}
