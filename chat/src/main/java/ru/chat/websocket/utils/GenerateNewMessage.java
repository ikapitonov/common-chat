package ru.chat.websocket.utils;

import ru.chat.websocket.model.ChatFrom;
import ru.chat.websocket.model.ChatTo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class GenerateNewMessage {

    private static String getDate () {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));

        return dateFormat.format(cal.getTime());
    }

    private static String getMessage (String message) {
        String str = message.trim();

        return str.length() > 498 ? str.substring(0, 498) : str;
    }

    public static ChatTo run (ChatFrom message, byte isAdmin, String username) {
        ChatTo newMessage = new ChatTo();

        newMessage.setText(getMessage(message.getContent()));
        newMessage.setAdmin(isAdmin);
        newMessage.setUsername(username);
        newMessage.setDate(getDate());
        newMessage.setType("MESSAGE");

        return newMessage;
    }
}
