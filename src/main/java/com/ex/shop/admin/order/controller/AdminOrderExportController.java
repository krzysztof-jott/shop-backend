package com.ex.shop.admin.order.controller;

import com.ex.shop.admin.order.model.AdminOrder;
import com.ex.shop.admin.order.service.AdminExportService;
import com.ex.shop.common.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/admin/orders/export")
@RequiredArgsConstructor
public class AdminOrderExportController {

    // 20.1 wydzieliłem stałą z metody w 20.0:
    private static final CSVFormat FORMAT = CSVFormat.Builder
            .create(CSVFormat.DEFAULT)
            .setHeader("Id", "PlaceDate", "OrderStatus", "GrossValue", "Firstname", "Lastname", "Street", "Zipcode",
                    "City", "Email", "Phone", "Payment")
            .build();

    private final AdminExportService adminExportService;

    // 17.0 dodaję usługę. Żeby usługa mogła zwracać plik lub resource, który jest plikiem, to najlepiej jest zrobić to przez
    // ResponseEntity z parametrem generycznym:
    @GetMapping
    public ResponseEntity<Resource> exportOrders(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate to,
            @RequestParam OrderStatus orderStatus
    ) {
        // 17.X metoda, która pozwoli pobrać odpowiednie dane:
        List<AdminOrder> adminOrders = adminExportService.exportOrders(
                // konwertuję na odpowiednie wartości. Żeby skonwertować datę z LocalDate na LocalDateTime użyję metody of:
                LocalDateTime.of(from, LocalTime.of(0,0,0)),
                LocalDateTime.of(to, LocalTime.of(23,59,59)), // wracam do serwisu
                orderStatus);
        // 20.2
        ByteArrayInputStream stream = transformToCsv(adminOrders);
        InputStreamResource resource = new InputStreamResource(stream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "orderExport.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

    // 20.0 metoda do przekształcenia danych w csv:
    private ByteArrayInputStream transformToCsv(List<AdminOrder> adminOrders) { // potrzebuję klasy CSV printer:
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             CSVPrinter printer = new CSVPrinter(new PrintWriter(stream), FORMAT)) {
            for (AdminOrder order : adminOrders) {
                printer.printRecord(Arrays.asList(
                        order.getId(),
                        order.getPlaceDate(),
                        order.getOrderStatus().getValue(),
                        order.getGrossValue(),
                        order.getFirstname(),
                        order.getLastname(),
                        order.getStreet(),
                        order.getZipcode(),
                        order.getCity(),
                        order.getEmail(),
                        order.getPhone(),
                        order.getPayment().getName()
                ));
            }
            printer.flush(); // trzeba sflaszować printera
            // muszę zwrócić dane w formie ByteArrayInputStream, a mam dane w postaci OutputStreama:
            return new ByteArrayInputStream(stream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Błąd przetwarzania CSV: " + e.getMessage());
        }
    }
}