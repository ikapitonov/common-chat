package ru.chat.utils;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;

public class Email {
    private JavaMailSender sender;

    private String title;
    private String text;
    private String address;

    public Email (JavaMailSender sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean send () {
        try {
            sendEmail();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    private void sendEmail() throws Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(address);
        helper.setText(text);
        helper.setSubject(title);

        sender.send(message);
    }
}
