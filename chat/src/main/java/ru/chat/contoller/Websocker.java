package ru.chat.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;


import ru.chat.model.messages.Messages;
import ru.chat.websocket.model.ChatFrom;
import ru.chat.websocket.model.ChatTo;
import ru.chat.websocket.model.OnlyStatus;
import ru.chat.websocket.utils.Counter;
import ru.chat.websocket.utils.GenerateNewMessage;
import ru.chat.websocket.utils.Getter;
import ru.chat.websocket.utils.IsValid;


@Controller
public class Websocker {

    @Autowired
    private Messages sqlMessage;

    @Autowired
    private SimpUserRegistry userRegistry;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat.sendMessage/*")
    public void SendMessage (SimpMessageHeaderAccessor headerAccessor, @Payload ChatFrom chatMessage) {

        if (!IsValid.check(headerAccessor) || chatMessage.getContent().trim().isEmpty()) {
            return ; // удалить юзера
        }

        ChatTo message = GenerateNewMessage.run(chatMessage,  (byte) headerAccessor.getSessionAttributes().get("admin"), headerAccessor.getSessionAttributes().get("username").toString());

        if (sqlMessage.insert((int) headerAccessor.getSessionAttributes().get("parent_id"), message.getText(), message.getUsername(),  (byte) headerAccessor.getSessionAttributes().get("admin")))
        {
            simpMessagingTemplate.convertAndSend("/topic/sites/" + Getter.id(headerAccessor), message);
        }
    }

    @MessageMapping("/chat.addUser/*")
    public void addUser (SimpMessageHeaderAccessor headerAccessor, @Payload ChatFrom chatMessage) {
        if (!IsValid.check(headerAccessor)) {
            return ; // удалить юзера
        }

        OnlyStatus message = new OnlyStatus();
        message.setType("ADD");

        Counter.add(headerAccessor.getSessionAttributes().get("parent_id").toString());

        simpMessagingTemplate.convertAndSend("/topic/sites/" + Getter.id(headerAccessor), message);
    }
}
