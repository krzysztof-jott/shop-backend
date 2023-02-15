package com.ex.shop.admin.order.service;

import com.ex.shop.admin.order.model.AdminOrder;
import com.ex.shop.admin.order.model.AdminOrderLog;
import com.ex.shop.admin.order.model.AdminOrderStatus;
import com.ex.shop.admin.order.repository.AdminOrderLogRepository;
import com.ex.shop.admin.order.repository.AdminOrderRepository;
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

    // 2.2
    @Transactional
    public void patchOrder(Long id, Map<String, String> values) {
        AdminOrder adminOrder = adminOrderRepository.findById(id).orElseThrow();
        // 2.3 tworzę metodę prywatną:
        patchValues(adminOrder, values);
    }

    private void patchValues(AdminOrder adminOrder, Map<String, String> values) {
        if (values.get("orderStatus") != null) {
            // 2.4 muszę wybrać odpowiedni status. Muszę skonwertować Stringa na enuma:
            // 12.0 wydzielam metodę:
            // 9.2 wydzielam z tego newStatus
            processOrderStatusChange(adminOrder, values);
        }
    }

    private void processOrderStatusChange(AdminOrder adminOrder, Map<String, String> values) {
        AdminOrderStatus oldStatus = adminOrder.getOrderStatus();
        AdminOrderStatus newStatus = AdminOrderStatus.valueOf(values.get("orderStatus"));
        // 28.0 dodaję if:
        if (oldStatus == newStatus) {
            return;
        }
        adminOrder.setOrderStatus(newStatus);
        //adminOrder.setOrderStatus(AdminOrderStatus.valueOf(values.get("orderStatus")));
        // 9.1 wywołuję metodę i tworzę zmienne old i newStatus:
        logStatusChange(adminOrder.getId(), oldStatus, newStatus);
        // 12.1 dodaję jeszcze jedną metodę, która będzie wysyłać powiadomienia:
        emailNotificationForStatusChange.sendEmailNotification(newStatus, adminOrder);
    }

    // przenoszę te 2 metody do EmailNotificationForStatusChange:
 /*   // 12.2 wysyłam tylko niektóre zmiany statusów, żeby nie spamować:
    private void sendEmailNotification(AdminOrderStatus newStatus, AdminOrder adminOrder) {
        if (newStatus == AdminOrderStatus.PROCESSING) {
            // 12.3 metoda do wysyłania mejla:
            sendEmail(adminOrder.getEmail(),
                    "Zamówienie " + adminOrder.getId() + " zmieniło status na " + newStatus.getValue(),
                    // potrzebuję id i stsatusu zamówienia:
                    createProcessingEmailMessage(adminOrder.getId(), newStatus)); // dodaję statyczny import
        } else if (newStatus == AdminOrderStatus.COMPLETED) {
            sendEmail(adminOrder.getEmail(),
                    "Zamówienie " + adminOrder.getId() + " zostało zrealizowane",
                    createCompletedEmailMessage(adminOrder.getId(), newStatus));
        } else if (newStatus == AdminOrderStatus.REFUND) {
            sendEmail(adminOrder.getEmail(),
                    "Zamówienie " + adminOrder.getId() + " zostało zwrócone",
                    createRefundEmailMessage(adminOrder.getId(), newStatus));
        }
    }

    // 12.4:
    private void sendEmail(String email, String subject, String mesage) {

    }*/

    // 9.0
    private void logStatusChange(Long orderId, AdminOrderStatus oldStatus, AdminOrderStatus newStatus) {
        // 9.3 tworzę encję builderem:
        adminOrderLogRepository.save(AdminOrderLog.builder()
                .created(LocalDateTime.now())
                .orderId(orderId)
                .note("Zmiana statusu zamówienia z " + oldStatus.getValue() + " na " + newStatus.getValue() + ".")
                .build());
    }
}