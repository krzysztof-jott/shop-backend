package com.ex.shop.common.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
// 35.1 dodaję, że implementuje EmailSender
public class EmailSimpleService implements EmailSender {

    private final JavaMailSender mailSender;

    @Async
    // 35.2 dodaję adnotację:
    @Override
    public void send(String to, String subject, String msg) { // 33.0 tworzę metodę
        SimpleMailMessage message = new SimpleMailMessage();
        // 33.1 podaję skąd wysyłamy, kto jest nadawcą:
        message.setFrom("Shop <sklep@sklep.pl>");
        // 34.0 dodaję:
        message.setReplyTo("Shop <sklep@sklep.pl>");
        // 33.2 do kogo:
        message.setTo(to);
        message.setSubject(subject); // temat meila
        message.setText(msg);
        mailSender.send(message);
        log.info("Email wysłany");
    }
}