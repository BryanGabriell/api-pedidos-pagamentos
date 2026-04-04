package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemDoPedidoRecord(

        @NotNull(message = "O ID do produto é obrigatório") Long produtoId,
        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade mínima deve ser 1") Integer quantidade) {
}
