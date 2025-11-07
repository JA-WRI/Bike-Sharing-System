package com.veloMTL.veloMTL.Service.BMSCore;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class TimerService {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    private final SimpMessagingTemplate messagingTemplate;

    public TimerService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

//    public void schedule(String userId, String message, long delayMs) {
//        scheduler.schedule(() -> {
//            messagingTemplate.convertAndSendToUser(userId, "/queue/notifications",
//                    message);
//        }, delayMs, TimeUnit.MILLISECONDS);
//    }

    public void scheduleReservationExpiry(String bikeId, String userId, long delayMs, Runnable onExpire) {
        scheduler.schedule(() -> {

            try {
                onExpire.run();
                messagingTemplate.convertAndSendToUser(
                        userId, "/queue/notifications",
                        "Reservation for bike " + bikeId + " expired."
                );
            } catch (Exception e) {
                System.out.println(e);
            }
        }, delayMs, TimeUnit.MILLISECONDS);
    }
}

