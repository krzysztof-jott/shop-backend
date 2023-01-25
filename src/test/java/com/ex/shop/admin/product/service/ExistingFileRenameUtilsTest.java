package com.ex.shop.admin.product.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExistingFileRenameUtilsTest {

    // 15.7UP kolejny test, sprawdza czy nazwa zmieniła się jeden raz:
    @Test
    void shouldNotRenameFile(@TempDir Path tempDir) throws IOException {
        String newName = ExistingFileRenameUtils.renameIfExists(tempDir, "test.png");
        assertEquals("test.png", newName); // nie powienien zmieniać nazwy, bo plik nie istnieje
    }

    @Test
    void shouldRenameExistingFile(@TempDir Path tempDir) throws IOException { // ma zapisywać pliki, które powstają w teście w takiej lokalizacji tymczasowej
        // w teście potrzebuję tymczasowego systemu plików, czyli dodaję Path u góry z adnotacją

        // 15.6UP żeby stworzyć ten plik robię:
        Files.createFile(tempDir.resolve("test.png")); // createFile daje błąd więc dodaję wyjątek

        // 15.3UP muszę przerobić metodę rename...
        String newName = ExistingFileRenameUtils.renameIfExists(tempDir, "test.png");// metoda rename... wymaga Stringów, a ścieżka tempDir w formie Path, więc ma
// inny format. Zatem muszę przerobić metodę rename... tak, żeby przyjmowała Path. Przechodzę do metody
        //15.4UP dodaję asercję:
        assertEquals("test-1.png", newName); // żeby ten test zadziałał, muszę sobie w tej ścieżce stworzyć taki plik
    }

    // 15.8UP test sprawdza, czy nazwa zmieniła się kilka razy:
    @Test
    void shouldRenameManyExistingFiles(@TempDir Path tempDir) throws IOException {
        Files.createFile(tempDir.resolve("test.png"));
        Files.createFile(tempDir.resolve("test-1.png"));
        Files.createFile(tempDir.resolve("test-2.png"));
        Files.createFile(tempDir.resolve("test-3.png"));
        String newName = ExistingFileRenameUtils.renameIfExists(tempDir, "test.png");
        assertEquals("test-4.png", newName);
    }
}