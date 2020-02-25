package ru.chat.websocket.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


import ru.chat.websocket.model.OnlyStatus;
import ru.chat.websocket.utils.Counter;
import ru.chat.websocket.utils.Getter;
import ru.chat.websocket.utils.IsValid;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

//    @Autowired
//    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
     //   System.out.println(SimpAttributesContextHolder.currentAttributes().getSessionId());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        if (!IsValid.check(headerAccessor)) {
            return ;
        }

        OnlyStatus message = new OnlyStatus();
        message.setType("REMOVE");

        Counter.remove(headerAccessor.getSessionAttributes().get("parent_id").toString());

        simpMessagingTemplate.convertAndSend("/topic/sites/" + Getter.id(headerAccessor), message);
    }
}
