package com.ex.shop;

import com.ex.shop.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// 23.2 dodaję adnotację:
@EnableScheduling
@RequiredArgsConstructor
public class ShopApplication {

	private final CartRepository cartRepository;

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}
}