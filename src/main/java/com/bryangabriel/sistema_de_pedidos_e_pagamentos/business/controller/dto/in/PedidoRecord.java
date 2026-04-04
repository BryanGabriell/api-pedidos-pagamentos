package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record PedidoRecord(
        @NotNull(message = "O Id do Usuario não pode estar vazio")
        @Positive(message = "O id do usuario deve ser positivo")
        Long userId,
        @NotEmpty(message = "Lista não pode estar vazia")
        List<@Valid ItemDoPedidoRecord> itens) {
}
