package com.ex.shop.common.model;

public enum OrderStatus {
    NEW("Nowe"),
    PAID("Opłacone"),
    PROCESSING("Przetwarzane"),
    WAITING_FOR_DELIVERY("Czeka na dostawę"),
    COMPLETED("Zrealizowane"),
    CANCELED("Anulowane"),
    REFUND("Zwrócone");

    // 4.1 dodaję pole:
    private String value;
    // 4.2 dodaję konstruktor:

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}