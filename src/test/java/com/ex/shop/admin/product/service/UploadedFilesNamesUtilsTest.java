package com.ex.shop.admin.product.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UploadedFilesNamesUtilsTest {
    // 14.5UP dodaję adnotację (test sparametryzowany) i tworzę metodę:
    @ParameterizedTest
    // 14.6UP pozwala w odpowiedni sposób dodać parametry do testów
    @CsvSource({
            "test test.png, test-test.png",
            "hello world.png, hello-world.png",
            "ąęśćżółźń.png, aesczolzn.png",
            "Produkt 1.png, produkt-1.png",
            "Produkt - 1.png, produkt-1.png",
            "Produkt__1.png, produkt-1.png"
    })
    void shouldSlugifyFileName(String in, String out) { // parametr wejściowy i wyjściowy
        String fileName = UploadedFilesNamesUtils.slugifyFileName(in);
        assertEquals(fileName, out);
    }
}