package com.ex.shop.admin.order.model;

import com.ex.shop.order.model.PaymentType;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "payment")
@Getter
public class AdminPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private PaymentType type;
    private boolean defaultPayment;
    private String note;
}