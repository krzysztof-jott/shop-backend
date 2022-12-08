package com.ex.shop.admin.product.service;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Files;
import java.nio.file.Path;

class ExistingFileRenameUtils {
    // 15.1UP metodą będzie zwracała zmienioną nazwę. Jak będe miał plik np. test.png to chcę, żeby jego nazwa zmieniła się na test-1.png:
    // 15.4UP Zamieniam typ uploadDir ze Stringa na Path
    public static String renameIfExists(Path uploadDir, String fileName) { // statyczna, nie będę nic wstrzykiwać
        //15.5UP było jak niżej i zmieniam na exist z parametrem tylko:
        // if (Files.exists(Paths.get(uploadDir, fileName))) {
        if (Files.exists(uploadDir.resolve(fileName))) { // jeśli istnieje dany plik w danej ścieżce to trzeba zmienić nazwę
            //15.2 tworzę test

            // 16.0UP sprawdzenie, czy plik z już zmienioną nazwą istnieje na dysku. Wydzielam metodę, dodaję tu return:
            return renameAndCheck(uploadDir, fileName);
        }
        return fileName; // jeśli plik nie istnieje, powinno zwrócić tę samą nazwę pliku
    }

    // 16.1UP wydzielona metoda. Służy do rekurencji. :
    private static String renameAndCheck(Path uploadDir, String fileName) {
        String newName = renameFileName(fileName);
        if (Files.exists(uploadDir.resolve(newName))) {
            newName = renameAndCheck(uploadDir, newName);
        }
        return newName;
    }

    private static String renameFileName(String fileName) {
        // 16.2UP implementuję faktyczną zmianę nazwy. Zaczynam od wyciagnięcia podstawowej nazwy i oddzielenia od rozszerzenia:
        String name = FilenameUtils.getBaseName(fileName);
        // 16.3UP sprawdzam czy istnieje już jakiś suffix w nazwie. test-1 : baza to test, -1 to rozszerzenie,
        // po użyciu regexa, będę miał tablicę z poszczególnymi elementami rozdzielonymi.
        // rozdzielam na myślniku i sprawdzam co jest za myślnikiem
        // wyrażenie (?=) sprawdza co jest za pierwszym znalezonym znakiem (tutaj za myślnikiem)
        // interesują mnie cyfry więc podaje wzorzez dla cyfr [0-9]
        // + oznacza jeden lub więcej, mogę tych cyfr wstawić dowolną ilość. $ oznacza znaknik końca, że chcę dodać na końcu to
        String[] split = name.split("-(?=[0-9]+$)");
        // 16.4UP z tego wzorca muszę wyciągnąć licznik:
        int counter = split.length > 1 ? Integer.parseInt(split[1]) + 1 : 1;

        return split[0] + "-" + counter + "." + FilenameUtils.getExtension(fileName);
    }
}