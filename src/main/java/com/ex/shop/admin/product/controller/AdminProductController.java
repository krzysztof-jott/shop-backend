package com.ex.shop.admin.product.controller;

import com.ex.shop.admin.product.controller.dto.AdminProductDto;
import com.ex.shop.admin.product.controller.dto.UploadResponse;
import com.ex.shop.admin.product.model.AdminProduct;
import com.ex.shop.admin.product.service.AdminProductImageService;
import com.ex.shop.admin.product.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

// 12.0 tworzę nową klasę:
@RestController
@RequiredArgsConstructor
public class AdminProductController {

    private static final Long EMPTY_ID = null;
    // 13.0 wstrzykuję serwis:
    private final AdminProductService productService;
    // 11.1 UP wstrzykuję serwis:
    private final AdminProductImageService productImageService;

    @GetMapping("/admin/products")
    public Page<AdminProduct> getProducts(Pageable pageable) { // 13.1 też dodaję Pageable
        return productService.getProducts(pageable);
    }

    // 16.0 dodaję metodę:
    @GetMapping("/admin/products/{id}")
    public AdminProduct getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    // 16.3 dodawanie nowych produktów. Tworzenie nowych zasobów:
    @PostMapping("/admin/products")
    public AdminProduct createProduct(@RequestBody @Valid AdminProductDto adminProductDto) { // 16.5 muszę tu stworzyć jakiś obiekt. Tworzę DTO
        return productService.createProduct(mapAdminProduct(adminProductDto, EMPTY_ID) // 16.4 w serwisie będzie nowa metoda createProduct. Będę potrzebował
                // 17.2 tworzenie stałej EMPTY_ID nie jest konieczne, jest dla lepszej czytelości, żeby było widać, że to jest puste id, a nie null
                // @RequestBody wyżej i obiektu, który przyjmie to co będzie wysyłać usługa.
                // 16.9 w ten sposób możemy dodawać poszczególne pola i przemapuję to co jest w DTO na encję:
                // 17.1 zwijam to wszystko do metody mapAdminProduct:
/*                .name(adminProductDto.getName())
                .description(adminProductDto.getDescription())
                .category(adminProductDto.getCategory())
                .price(adminProductDto.getPrice())
                .currency(adminProductDto.getCurrency())
                .build() // w ten sposób przemapowałem DTO na AdminProduct. Tworzę teraz metodę serwisową*/
        );
    }
    // 17.0 usługa do zapisywania edytowanych produktów. Edycja zasobów:
    @PutMapping("/admin/products/{id}")
    public AdminProduct updateProduct(@RequestBody @Valid AdminProductDto adminProductDto, @PathVariable Long id) { // 17.1 będzie potrzebne jeszcze id
        return productService.updateProduct(mapAdminProduct(adminProductDto, id) // 31.0 bez @Valid Hibernate Validator nie sprawdzi
                // klasy. Po to żeby wyzwolić uruchomienia Hibernate Validator
        );
    }

    // 44.0 dodaję metodę:
    @DeleteMapping("/admin/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id); // 44.1 tworzę tę metodę w serwisie (tu jeszcze błąd robi ona póki co)
    }

    // 9.6UP dodaję adnotację mapowania i urla:
    @PostMapping("/admin/products/upload-image")
    // 4.0UP dodaję usługę do zapisywania obrazu. Żeby zadziałała muszę jej przesłać spacjalny obiekt typu MultipartFile i dodaję adnotaję:
    public UploadResponse uploadImage(@RequestParam("file") MultipartFile multipartFile) {

        // 4.2UP pobieram nazwę pliku z multipartfile (tworzę zmienną filename)
        // 11.16 inlajnuje to niżej do savedFilename:
        /*String filename = multipartFile.getOriginalFilename(); // nazwa przesłanego pliku*/
        // 4.3UP tworzę ścieżkę gdzie będę zapisywać plik. Tworzę katalog data i dodaję do .gitignora:
        // 11.10UP przeniesione do serwisu:
        /*String uploadDir = "./data/productImages/"; // ta ścieżka musi istnieć na dysku jeśli chce zapisać dysk*/
        // 4.4UP potrzebuję jeszcze ścieżki w postaci obiektu Path:
        // 11.11UP przeniesione do serwisu
        /*Path filePath = Paths.get(uploadDir).resolve(filename); // przekazuję nazwę pliku w resolve*/
        // 4.5UP teraz się zajmę bajtami z przesłanego pliku, z muliparta wyciągam inpitStream i użyję w bloku try. Bloku,
        // który pozwala użyć tego inputStreama i później, gdy nie będzie już używany zostanie na nim wywołana metoda close():
        try (InputStream inputStream = multipartFile.getInputStream()) { // dodaję klauzulę catch, bo jest błąd
            // 4.6UP żeby zapisać plik, potrzebuję outputStreama (mam tylko input póki co):
            // 11.5UP przenoszę do serwisu
            /*OutputStream outputStream = Files.newOutputStream(filePath);// tworzę zmienną outputStream*/
            // 4.7UP łączę teraz wszystko (input i output) za pomocą metody transfer:
            // 11.6UP to też przenoszę do serwisu:
//            inputStream.transferTo(outputStream);
            // 4.8UP zwracam odpowiedź:
            // 11.3UP tu ją przeniosłem:
            String savedFileName = productImageService.uploadImage(multipartFile.getOriginalFilename(), inputStream);
            return new UploadResponse(savedFileName); // 11.8UP zmienna zamiast filename musi być teraz savedFileName
        } catch (IOException e) {
            throw new RuntimeException("coś poszło źle przy wgrywaniu pliku", e);
        }
//        return null; usuwam na koniec

        // 11.2UP wywułuję metodę i zaczynam przenosić. Przenoszę nie całego multiparta, tylko co mnie interesuje, czyli nazwa pliku,
        // który przesyłam (String) i inputStream. Metoda zwraca nazwę pliku zapisanego. Przenoszę ją wyzej do try():
//        String savedFileName = productImageService.uploadImage();
    }


    // 10.0UP dodaję usługę, będzie zwracała jakiś resourse. Adres skopiowany z fronenedu (bez api). ResponseEntuty dlatego, że będę ustawiać
    // dodatkowe nagłówki dla zwracanego pliku:
    @GetMapping("/data/productImage/{filename}")
    public ResponseEntity<Resource> serveFiles(@PathVariable String filename) throws IOException {
        // 10.2UP kopiuję uploadDir:
        // 13.0UP usuwam bo było w tej pierwszej metodzie:
        /*String uploadDir = "./data/productImages/";*/
        // 10.1UP korzystam ze specjalnej klasy, która załaduje plik z dysku:
        /*FileSystemResourceLoader fileSystemResourceLoader = new FileSystemResourceLoader(); przeniesione do serwisu*/
        // 10.5UP tworzę zmienną file do tego co po prawej stronie było:
        /*Resource file = fileSystemResourceLoader.getResource(uploadDir + filename);// ścieżka katalogu i nazwa pliku, który chcę otworzyć*/
        Resource file = productImageService.serveFiles(filename);
        return ResponseEntity.ok()
                // 10.3UP dodaję odpowiednie nagłówki. Wartość nagłówka pobieram z klasy Files, ścieżką jest nazwa pliku. Dodaję wyjatek :
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Path.of(filename)))
                // 10.4UP wstawiam body odpowiedzi:
                .body(file); // teraz metoda będzie zwracała Resource, czyli plik zapisany na dysku
    }

    private static AdminProduct mapAdminProduct(AdminProductDto adminProductDto, Long id) {
        return AdminProduct.builder() // to akurat w takiej formie (bo w DTO nie ma id?)
                .id(id)
                .name(adminProductDto.getName())
                .description(adminProductDto.getDescription())
                .category(adminProductDto.getCategory())
                .price(adminProductDto.getPrice())
                .currency(adminProductDto.getCurrency()/*.toUpperCase(Locale.ROOT)  32. usuwam po walidacji*/)
                // 3.1UP przemapowuję pole image i dodaję pole w DTO:
                .image(adminProductDto.getImage())
                .build(); // wyekstrachowana metoda, bo powtarza sie to samo w reszcie metod, więc żeby ciągle nie pisać tego samego
    }
}