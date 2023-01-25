package com.ex.shop.common.mail;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

//@Configuration
public class EmailConfig {

    // 37.0 dodaję definicję beanów:
    @Bean
    // 37.1 żeby te definicje się wczytały, muszę dodać kolejną deklarację:
    @ConditionalOnProperty(name = "app.email.sender", matchIfMissing = true, havingValue = "emailSimpleService")
    public EmailSender emailSimpleService(JavaMailSender javaMailSender) {
        return new EmailSimpleService(javaMailSender);
    }

    @Bean
    @ConditionalOnProperty(name = "app.email.sender", havingValue = "fakeEmailService")
    public EmailSender fakeEmailService() {
        return new FakeEmailService();
    }
}