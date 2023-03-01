package com.ex.shop.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ex.shop.security.model.ShopUserDetails;
import com.ex.shop.security.model.User;
import com.ex.shop.security.model.UserRole;
import com.ex.shop.security.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    // 9.0
    private Long expirationTime;
    private String secret;

    // 9.1
    public LoginController(AuthenticationManager authenticationManager,
                           // 9.2 żeby wstrzyknąć potrzebuję adnotacji @Value
                           UserRepository userRepository,
                           @Value("${jwt.expirationTime}") Long expirationTime,
                           @Value("${jwt.secret}") String secret) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.expirationTime = expirationTime;
        this.secret = secret;
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginCredentials loginCredentials) {
        // 13.1 teraz zwracam typ Token zamiast String. Angular może mieć problem z konwertowaniem Stringa
        // 28.4 inlajnuje:
        return authenticate(loginCredentials.getUsername(), loginCredentials.getPassword());
    }

    // 28.0
    @PostMapping("/register")
    public Token register(@RequestBody @Valid RegisterCredentials registerCredentials) {
        // 28.2 muszę sprawdzić czy hasła się zgadzają, trzeba powtórzyć walidację z frontendu i sprawdzic czy user
        // już istnieje. Jak jest wszystko ok to można dodać usera do bazy i wywołać springowy mechanizm logowania.
        if (!registerCredentials.getPassword().equals(registerCredentials.getRepeatPassword())) {
            // jeśli różne to wyjątek:
            throw new IllegalArgumentException("Hasła nie są identyczne");
        }
        // 29.0 czy użytkownik istnieje, tworzę metodę (exist to słowo kluczowe Spring Data):
        if (userRepository.existsByUsername(registerCredentials.getUsername())) {
            throw new IllegalArgumentException("Taki użytkownik już istnieje w bazie danych");
        }
        // 29.1 zapisuję użytkownika do bazy:
        userRepository.save(User.builder()
                .username(registerCredentials.getUsername())
                // hasło musi być zakodowane. w {} podaję algorytm, w którym jest zaszyfrowane (domyślnie bcrypt):
                .password("{bcrypt}" + new BCryptPasswordEncoder().encode(registerCredentials.getPassword()))
                .enabled(true)
                // 32.1 lista ról:
                .authorities(List.of(UserRole.ROLE_CUSTOMER)) // dopiero teraz mam prawidłowo zapisanego użytkownika
                .build());
        // 32.2 mogę stworzyć teraz tokena:
        return authenticate(registerCredentials.getUsername(), registerCredentials.getPassword());
                // 34.1 muszę tu wygenerować wartość true/false, żeby wiedzieć czy dany user ma dostęp do panelu administracyjnego,
                // czyli kiedy ma rolę ADMIN. Spring zwraca rolę w Authentication

    }
    // 28.3 wydzieliłem metodę:
    private Token authenticate(String username, String password) {
        // 42.0 dodaję:
        User user = userRepository.findByUsername(username).orElseThrow();
        // 7.1 teraz tu używam:
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getId(), password) // zmieniam username na user i getId
        );
        // 42.X zamieniam UserDetails na ShopUserDetails:
        ShopUserDetails principal = (ShopUserDetails) authenticate.getPrincipal();
        // 10.0
        // 34.2 przenoszę tu new Token z returna powyżej i wydzielam matodę:
        String token = JWT.create()
                // co token będzie zawierał, subject czyi principal:
                .withSubject(String.valueOf(principal.getId())) // muszę tu mieć Stringa, a jest Long
                // data ważności tokena:
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                // podpisuję tokena:
                .sign(Algorithm.HMAC256(secret));
        // 34.3 strimem wyfiltruje to co mi jest trzebne:
        return new Token(token, principal.getAuthorities().stream()
                // mapuję na Stringa:
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .filter(s -> UserRole.ROLE_ADMIN.name().equals(s)) // filtruję z ról użytkownika tylko rolę admina
                // jeśli mam taką rolę, to zmapuję sobie na true albo na false:
                .map(s -> true) // jeśli istnieje to true
                .findFirst() // biorę pierwszy element i zwracam
                .orElse(false)); // jeśli nie istnieje to false
    }

    @Getter
    private static class LoginCredentials { // klasa zagnieżdzona
        private String username;
        private String password;
    }

    // 28.1
    @Getter
    private static class RegisterCredentials {
        @Email
        private String username;
        @NotBlank
        private String password;
        @NotBlank
        private String repeatPassword;
    }

    // 13.0 dodaję nową klasę:
    @Getter
    @AllArgsConstructor
    private static class Token {
        private String token;
        // 34.0
        private boolean adminAccess;
    }
}