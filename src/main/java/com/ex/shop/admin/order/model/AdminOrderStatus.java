package com.ex.shop.admin.order.model;

public enum AdminOrderStatus {
    // 4.0 dodaję statusy z frontu
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

    AdminOrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}