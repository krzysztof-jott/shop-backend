package com.ex.shop.common.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailClientService {

    // 40.3 wstrzykuję propertisa przez Value:
    @Value("${app.email.sender}")
    private String isFakeProp;

    // 38.0 wstrzykuję:
//    private final List<EmailSender> list; // 38.1 Spring wstrzyknie wszystkie implementacje interfesju EmailSender do tej listy

    // 38.2
    private final Map<String, EmailSender> senderMap; // <nazwa beana, implementacja serwisu>

    // 38.3 pobieram konkretną implementację (domyślną), metoda zwraca domyślną implementację:
    public EmailSender getInstance() {
        // 40.2 dodaję warunek i przyrówneję do fejkowego serwisu:
        if (isFakeProp.equals("fakeEmailService")) {
            return senderMap.get("fakeEmailService"); // to pozwala wymienić domyślną implementację na tę fejkową
        }
        return senderMap.get("emailSimpleService"); // 38.4 podaję nazwę beana. Pobiera instancję tego beana.
    }

    // 41.0 przeciążam metodę:
    public EmailSender getInstance(String beanName) {
        if (isFakeProp.equals("fakeEmailService")) {
            return senderMap.get("fakeEmailService");
        }
        return senderMap.get(beanName); // teraz mogę wybrać sobie instancję
    }
}