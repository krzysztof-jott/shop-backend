package com.ex.shop.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;

    // 9.0
    private Long expirationTime;
    private String secret;

    // 9.1
    public LoginController(AuthenticationManager authenticationManager,
                           // 9.2 żeby wstrzyknąć potrzebuję adnotacji @Value
                           @Value("${jwt.expirationTime}") Long expirationTime,
                           @Value("${jwt.secret}") String secret) {
        this.authenticationManager = authenticationManager;
        this.expirationTime = expirationTime;
        this.secret = secret;
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginCredentials loginCredentials) {
        // 7.1 teraz tu używam:
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginCredentials.getUsername(), loginCredentials.getPassword())
        );
        UserDetails principal = (UserDetails) authenticate.getPrincipal();
        // 10.0
        String token = JWT.create()
                // co token będzie zawierał, subject czyi principal:
                .withSubject(principal.getUsername())
                // data ważności tokena:
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                // podpisuję tokena:
                .sign(Algorithm.HMAC256(secret));
        // 13.1 teraz zwracam typ Token zamiast String. Angular może mieć problem z konwertowaniem Stringa
        return new Token(token);
    }

    @Getter
    private static class LoginCredentials { // klasa zagnieżdzona

        private String username;
        private String password;
    }

    // 13.0 dodaję nową klasę:
    @Getter
    @AllArgsConstructor
    private static class Token {

        private String token;
    }
}