package com.ex.shop.admin.product.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// 11.0UP tworzę serwis i wstrzykuję w kontrolerze:
@Service
public class AdminProductImageService {

    //12.2 dodaję pole z przerobionej zmiennej. Dodaję adnotację i wklejam nazwę propertisa:
    @Value("${app.uploadDir}")
    private String uploadDir;

    // 11.4UP utorzyłem metodę
    public String uploadImage(String filename, InputStream inputStream) {
        // 11.15 zwracanie nazwy zapisanego pliku i wracam do kontrolera:
        // 14.1UP zamiast file name daję nazwę nowej klasy i nowej metody:
        String newFileName = UploadedFilesNamesUtils.slugifyFileName(filename);

        // 15.0UP tworzę nową klasę:
        // 15.2UP dodaję na początku newFileName (nadpisuję to wyżej):
        newFileName = ExistingFileRenameUtils.renameIfExists(Path.of(uploadDir), newFileName); // czyli tam gdzie będę sprawdzać czy dana nazwa istnieje i newFileName

        //11.9UP przenoszę znowu z kontrolera:
        //12.1UP po przeniesieniu adresu do propertisów zmieniam zmienną na pole i daję do góry:
        /*String uploadDir = "";*/
        Path filePath = Paths.get(uploadDir).resolve(newFileName); // 14.2UP zmieniam filename na newFileName i jest już zwrócone w return

        // 11.7UP to przeniosłem z kontrolera:

        /*        OutputStream outputStream = null;
        try {
            outputStream = Files.newOutputStream(filePath); // 11.12UP był błąd więc try-catch
            inputStream.transferTo(outputStream); // 11.13UP to też tu wrzuciłem do bloku z dołu
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        // 11.14 przerabiam try-catcha całego i wrzucam do niego jeszcze OutputStream:
        try(OutputStream outputStream = Files.newOutputStream(filePath)) {
            inputStream.transferTo(outputStream); // 11.13UP to też tu wrzuciłem do bloku z dołu
        } catch (IOException e) {
            throw new RuntimeException("nie mogę zapisać pliku", e); // wyjątek, gdy nie mogę zapisać pliku do outputStreama
        }
        return newFileName; // zwracam nazwę nowozapisanego pliku
    }

    public Resource serveFiles(String filename) {
        //13.0 przenoszę z kontrolera:
        FileSystemResourceLoader fileSystemResourceLoader = new FileSystemResourceLoader();
        return fileSystemResourceLoader.getResource(uploadDir + filename); //13.1 zamiast Resource file robię return i teraz muszę
        // tam dodać w kontrolerze Resource file
    }
}