package com.ex.shop.order.service;

import com.ex.shop.order.model.Shipment;
import com.ex.shop.order.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    // 16.0 dodaję metodę, będzie zwracała listę dostaw:
    public List<Shipment> getShipments() {
        return shipmentRepository.findAll();
    }
}