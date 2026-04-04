package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in;


import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProdutoAtualizaRecord(
        @Size(min = 5, max = 100)
        String nome,
        @Positive
        BigDecimal preco,
        @PositiveOrZero
        Integer estoque
) {
}
