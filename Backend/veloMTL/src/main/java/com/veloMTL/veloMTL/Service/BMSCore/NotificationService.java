package com.veloMTL.veloMTL.Service.BMSCore;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyOperators(String message) {
        messagingTemplate.convertAndSend("/topic/operator", message);
        System.out.println("\n\n\n\n\n\nMeesage\n\n\n\n\n\n\n");
    }
}
