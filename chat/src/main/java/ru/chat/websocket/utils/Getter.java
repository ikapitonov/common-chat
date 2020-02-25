package ru.chat.websocket.utils;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public class Getter {

    public static String id (SimpMessageHeaderAccessor headerAccessor) {
        return headerAccessor.getSessionAttributes().get("parent_id").toString();
    }
}
