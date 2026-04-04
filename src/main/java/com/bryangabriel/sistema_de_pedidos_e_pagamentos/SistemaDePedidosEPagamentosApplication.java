package com.bryangabriel.sistema_de_pedidos_e_pagamentos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SistemaDePedidosEPagamentosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaDePedidosEPagamentosApplication.class, args);
	}

}
