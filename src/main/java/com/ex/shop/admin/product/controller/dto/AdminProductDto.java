package com.ex.shop.admin.product.controller.dto;

import com.ex.shop.admin.product.model.AdminProductCurrency;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
public class AdminProductDto {
    @NotBlank // żeby Stringiem nie był tylko biały znak, np spacja
    @Length(min = 4)
    private String name;
    private Long categoryId;
    @NotBlank
    @Length(min = 4)
    private String description;
    private String fullDescription;
    @NotNull
    @Min(0) // bo BigDecimal, więc nie Length, a Min
    private BigDecimal price;
    private AdminProductCurrency currency; // pole będzie musiało mieć wartość z tego enuma
    private String image;
    @NotBlank
    @Length(min = 4)
    private String slug;
}