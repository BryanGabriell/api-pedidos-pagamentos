package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in;


import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoRecord(
        @NotBlank(message = "O campo nome não pode estar vazio")
        @Size(min = 5, max = 100, message = "O nome deve ter entre 5 e 100 caracteres")
        String nome,
        @NotNull(message = "Ops, temos um erro o preço nao pode estar vazio")
        @Positive(message = "O Preço do produto tem que ser positivo")
        @Digits(integer = 10, fraction = 2, message = "Preço invalido")
        BigDecimal preco,
        @NotNull(message = "O estoque não pode estar vazio")
        @PositiveOrZero
        Integer estoque
) {
}
