package com.ex.shop.admin.order.service;

import com.ex.shop.admin.order.model.AdminOrder;
import com.ex.shop.admin.order.model.AdminOrderLog;
import com.ex.shop.admin.order.repository.AdminOrderLogRepository;
import com.ex.shop.admin.order.repository.AdminOrderRepository;
import com.ex.shop.common.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final AdminOrderRepository adminOrderRepository;
    private final AdminOrderLogRepository adminOrderLogRepository;
    private final EmailNotificationForStatusChange emailNotificationForStatusChange;

    public Page<AdminOrder> getOrders(Pageable pageable) {
        return adminOrderRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by("id").descending()
                ));
    }

    public AdminOrder getOrder(Long id) {
        return adminOrderRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void patchOrder(Long id, Map<String, String> values) {
        AdminOrder adminOrder = adminOrderRepository.findById(id).orElseThrow();
        patchValues(adminOrder, values);
    }

    private void patchValues(AdminOrder adminOrder, Map<String, String> values) {
        if (values.get("orderStatus") != null) {
            processOrderStatusChange(adminOrder, values);
        }
    }

    private void processOrderStatusChange(AdminOrder adminOrder, Map<String, String> values) {
        OrderStatus oldStatus = adminOrder.getOrderStatus();
        OrderStatus newStatus = OrderStatus.valueOf(values.get("orderStatus"));
        if (oldStatus == newStatus) {
            return;
        }
        adminOrder.setOrderStatus(newStatus);
        logStatusChange(adminOrder.getId(), oldStatus, newStatus);
        emailNotificationForStatusChange.sendEmailNotification(newStatus, adminOrder);
    }

    private void logStatusChange(Long orderId, OrderStatus oldStatus, OrderStatus newStatus) {
        adminOrderLogRepository.save(AdminOrderLog.builder()
                                                  .created(LocalDateTime.now())
                                                  .orderId(orderId)
                                                  .note("Zmiana statusu zamówienia z " + oldStatus.getValue() + " na " + newStatus.getValue() + ".")
                                                  .build());
    }
}