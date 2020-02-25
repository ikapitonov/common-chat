package ru.chat.websocket.utils;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public class IsValid {

    public static boolean check (SimpMessageHeaderAccessor headerAccessor) {
        if (headerAccessor.getSessionAttributes().get("username").toString().isEmpty()) {
            return false;
        }

        if (((int) headerAccessor.getSessionAttributes().get("parent_id")) == 0) {
            return false;
        }

        return true;
    }
}
