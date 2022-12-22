package com.ex.shop.product.controller;

import com.ex.shop.common.dto.ProductListDto;
import com.ex.shop.common.model.Product;
import com.ex.shop.product.service.ProductService;
import com.ex.shop.product.service.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;
import java.util.List;

@RestController // jak dodam endpointy, to stworzy dla nich mapowanie
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public Page<ProductListDto> getProducts(@PageableDefault(size = 15) Pageable pageable) {
        Page<Product> products = productService.getProducts(pageable);
        List<ProductListDto> productListDtos = products.getContent().stream()
                .map(product -> ProductListDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .currency(product.getCurrency())
                        .image(product.getImage())
                        .slug(product.getSlug())
                        .build())
                .toList();
        return new PageImpl<>(productListDtos, pageable, products.getTotalElements()); // wyciągam total z products
    }

    @GetMapping("/products/{slug}")
    public ProductDto getProduct(
                                  @PathVariable
                                  @Pattern(regexp = "[a-z0-9\\-]+")
                                  @Length(max = 255)
                                  String slug) {
        return productService.getProductBySlug(slug);
    }
}