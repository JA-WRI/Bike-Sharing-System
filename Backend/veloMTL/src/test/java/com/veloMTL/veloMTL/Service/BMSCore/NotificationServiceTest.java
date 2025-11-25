package com.veloMTL.veloMTL.Service.BMSCore;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    NotificationService notificationService;

    @Test
    void notify_operators_test(){
        String message = "Send message";
        notificationService.notifyOperators(message);
        verify(messagingTemplate).convertAndSend("/topic/operator", message);
    }

}
