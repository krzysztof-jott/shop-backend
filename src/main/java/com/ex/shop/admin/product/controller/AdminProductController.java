package com.ex.shop.admin.product.controller;

import com.ex.shop.admin.product.controller.dto.AdminProductDto;
import com.ex.shop.admin.product.controller.dto.UploadResponse;
import com.ex.shop.admin.product.model.AdminProduct;
import com.ex.shop.admin.product.service.AdminProductImageService;
import com.ex.shop.admin.product.service.AdminProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import static com.ex.shop.admin.common.utils.SlugifyUtils.slugifySlug;

@RestController
@RequiredArgsConstructor
public class AdminProductController {

    private static final Long EMPTY_ID = null;
    private final AdminProductService productService;
    private final AdminProductImageService productImageService;

    @GetMapping("/admin/products")
    public Page<AdminProduct> getProducts(Pageable pageable) { // 13.1 też dodaję Pageable
        return productService.getProducts(pageable);
    }

    @GetMapping("/admin/products/{id}")
    public AdminProduct getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PostMapping("/admin/products")
    public AdminProduct createProduct(@RequestBody @Valid AdminProductDto adminProductDto) {
        return productService.createProduct(mapAdminProduct(adminProductDto, EMPTY_ID)
        );
    }

    @CacheEvict(cacheNames = "productBySlug", key="#adminProductDto.slug")
    @PutMapping("/admin/products/{id}")
    public AdminProduct updateProduct(@RequestBody @Valid AdminProductDto adminProductDto, @PathVariable Long id) {
        return productService.updateProduct(mapAdminProduct(adminProductDto, id));
    }

    @DeleteMapping("/admin/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @PostMapping("/admin/products/upload-image")
    public UploadResponse uploadImage(@RequestParam("file") MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            String savedFileName = productImageService.uploadImage(multipartFile.getOriginalFilename(), inputStream);
            return new UploadResponse(savedFileName);
        } catch (IOException e) {
            throw new RuntimeException("coś poszło źle przy wgrywaniu pliku", e);
        }
    }

    @GetMapping("/data/productImage/{filename}")
    public ResponseEntity<Resource> serveFiles(@PathVariable String filename) throws IOException {
        Resource file = productImageService.serveFiles(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Path.of(filename)))
                .body(file);
    }

    @GetMapping("/admin/products/clearCache")
    @CacheEvict(value = "productBySlug")
    public void clearProductsCache() {
    }

    private static AdminProduct mapAdminProduct(AdminProductDto adminProductDto, Long id) {
        return AdminProduct.builder()
                           .id(id)
                           .name(adminProductDto.getName())
                           .description(adminProductDto.getDescription())
                           .fullDescription(adminProductDto.getFullDescription())
                           .categoryId(adminProductDto.getCategoryId())
                           .price(adminProductDto.getPrice())
                           .salePrice(adminProductDto.getSalePrice())
                           .currency(adminProductDto.getCurrency())
                           .image(adminProductDto.getImage())
                           .slug(slugifySlug(adminProductDto.getSlug()))
                           .build();
    }
}