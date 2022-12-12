package com.ex.shop.admin.category.controller.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

// 7.0 tworzę DTO, do zapisu i edycji i ustawiam to DTO w metodach:
@Getter
public class AdminCategoryDto {
    @NotBlank
    @Length(min = 4)
    private String name;
    private String description;
    @NotBlank
    @Length(min = 4)
    private String slug;
}