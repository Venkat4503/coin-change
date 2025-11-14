package com.adp.coinchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class CoinChangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoinChangeApplication.class, args);
	}
}
