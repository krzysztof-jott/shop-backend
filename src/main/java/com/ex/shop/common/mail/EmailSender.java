package com.ex.shop.common.mail;

public interface EmailSender {
    void send(String to, String subject, String msg);
}