package com.ex.shop.security;

import com.ex.shop.security.model.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true) // włącza logi Spring Security
public class SecurityConfig {

    private String secret;

    public SecurityConfig(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    // 2.0 pierwszy bean:
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           // 11.1 dodaję:
                                           AuthenticationManager authenticationManager,
                                           UserDetailsService userDetailsService) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                // chcę mieć zablokowany panel admina i odblokowany dostęp do części ogólnej:
                // 33.0 zamieniam authenticated() na hasRole()
                .requestMatchers("/admin/**").hasRole(UserRole.ROLE_ADMIN.getRole()) // wszystko co po admin jest zablokowane
                .anyRequest().permitAll()); // wszystkie inne są dostępne
        http.csrf().disable(); // wyłączam csrf
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // wyłączam sesje http, będzie bezstanowa
        // 11.0 podłączam filtr:
        http.addFilter(new JwtAuthorizationFilter(authenticationManager, userDetailsService, secret));
        return http.build();        // buduję konfigurację
    }

    // 7.0
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 40.1 usuwam beana:
/*    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {

        // 22.0 usuwam,
*//*        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("test")
                .roles("ADMIN")
                .build();*//*
        // 22.1 i teraz mam bazodanowego user details menadzera:
        return new JdbcUserDetailsManager(dataSource); // teraz mogę zaczytać użytkowników z bazy danych
    }*/
}