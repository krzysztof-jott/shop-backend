package com.ex.shop.admin.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product") // 12.1 podaję nazwę, jakiej tabeli tyczy się ta klasa
@Getter
@Builder // 16.7 dodaję adnotację, żeby stworzyć encję. Pojawia się błąd, bo @Builder dodaje wieloargumentowy konstruktor,
// który zawiera wszystkie te pola z klasy, więc muszę dodać @NoArgsConstruktor. W encji kiedy jest konstruktor wieloagrumentowy, trzeba
// jawnie dodać konstruktor bezargumentowy, stąd adnotacja @NoArgs...
@NoArgsConstructor
@AllArgsConstructor // 16.8 trzeba dodać, bo z @Builder pojawia się błąd
public class AdminProduct {
    // 12.2 kopiuję wstępnie pola z product i dodaję Getter:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private String description;
    private BigDecimal price;
    private String currency;

}
