package com.ex.shop.security.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private boolean enabled;
    // 31.0 dodaję pole i adnotację, która znaczy, że role z tabeli z tabeli authorities będą mapowane na tę listę. Wskazuję
    // tabele, z której znajdują się te role i kolumnę, z której będę wyciągał wartości:
    @ElementCollection
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"))
    @Column(name = "authority") // kolumna, z której chcę wyciągnąć wartości
    @Enumerated(EnumType.STRING) // 32.0 dodaję enuma
    private List<UserRole> authorities;
    private String hash;
    private LocalDateTime HashDate;
}