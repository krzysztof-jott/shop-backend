package com.ex.shop.admin.order.model;

public enum AdminOrderStatus {
    // 4.0 dodaję statusy z frontu
    NEW("Nowe"),
    PROCESSING("Przetwarzane"),
    PAID("Opłacone"),
    COMPLETED("Zrealizowane"),
    REFUND("Zwrócono środki");

    // 4.1 dodaję pole:
    private String value;
    // 4.2 dodaję konstruktor:

    AdminOrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}