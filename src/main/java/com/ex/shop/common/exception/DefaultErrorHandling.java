package com.ex.shop.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Date;
import java.util.NoSuchElementException;

@ControllerAdvice // obsługa specyficznych wyjątków
public class DefaultErrorHandling {

    @ExceptionHandler({NoSuchElementException.class})
    // trzeba podać klasę wyjątku, przy której ma się odpalić ta metoda
    @ResponseBody // żeby zwróciło JSONa
    // będzie zwracać kod błędu. Jako parametr ten wyjątek, który będzie przychodził:
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new DefaultErrorDto(
                        new Date(),
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        "Zasób nie istnieje",
                        request.getRequestURI() // ścieżka
                ));
    }
}