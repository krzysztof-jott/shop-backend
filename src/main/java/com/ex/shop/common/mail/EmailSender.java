package com.ex.shop.common.mail;

public interface EmailSender {

    // 35.0 dodaję metodę:
    void send(String to, String subject, String msg);
}