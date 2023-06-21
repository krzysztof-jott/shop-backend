package com.ex.shop.security.model;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_ADMIN("ADMIN"),
    ROLE_CUSTOMER("CUSTOMER");

    private String role;

    UserRole(String role) {
        this.role = role;
    }
}