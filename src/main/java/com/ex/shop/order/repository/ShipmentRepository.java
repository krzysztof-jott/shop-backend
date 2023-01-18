package com.ex.shop.order.repository;

import com.ex.shop.order.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}