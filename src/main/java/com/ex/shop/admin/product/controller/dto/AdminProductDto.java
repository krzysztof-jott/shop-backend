package com.ex.shop.admin.product.controller.dto;

import com.ex.shop.admin.product.model.AdminProductCurrency;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

// 16.6 bez pola id!
@Getter
public class AdminProductDto {
    // 31.0 ustawiam adnotacje walidacyjne:
    //    @NotEmpty // nie będzie można przesłać pustego stringa   NIE POTRZEBNY JUŻ, BO JEST LENGTH
    @NotBlank // żeby stringiem nie był tylko biały znak, np spacja
    @Length(min = 4)
    private String name;
    @NotBlank
    @Length(min = 4)
    private String category;
    @NotBlank
    @Length(min = 4)
    private String description;
    @NotNull
    @Min(0) // bo BigDecimal, więc nie Length, a Min
    private BigDecimal price;
    private AdminProductCurrency currency; // pole będzie musiało mieć wartość z tego enuma
    //3.2UP dodaję pole image:
    private String image;
    // 20.2UP dodaję polę i robię walidacje. Przechodzę do kontrolera (admin):
    @NotBlank
    @Length(min = 4)
    private String slug;
}