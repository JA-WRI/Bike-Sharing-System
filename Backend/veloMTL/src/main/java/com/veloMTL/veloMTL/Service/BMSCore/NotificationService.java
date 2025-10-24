package com.veloMTL.veloMTL.Service.BMSCore;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void notifyOperators(String message) {
        System.out.println("\n\n\n\n\n\n[NOTIFICATION TO OPERATORS] " + message+ "\n\n\n\n\n\n");
    }
}
