package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out;


import java.math.BigDecimal;

public record ItemDoPedidoRecordOut(
        Long produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subTotal
) {
}
