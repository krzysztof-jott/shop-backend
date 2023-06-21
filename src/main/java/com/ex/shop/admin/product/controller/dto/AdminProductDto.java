package com.ex.shop.admin.product.controller.dto;

import com.ex.shop.admin.product.model.AdminProductCurrency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import java.math.BigDecimal;

@Getter
public class AdminProductDto {
    @NotBlank
    @Length(min = 4)
    private String name;
    private Long categoryId;
    @NotBlank
    @Length(min = 4)
    private String description;
    private String fullDescription;
    @NotNull
    @Min(0)
    private BigDecimal price;
    @Min(0)
    private BigDecimal salePrice;
    private AdminProductCurrency currency;
    private String image;
    @NotBlank
    @Length(min = 4)
    private String slug;
}