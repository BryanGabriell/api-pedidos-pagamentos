package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out;

import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.enums.StatusDoPedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PedidoRecordOut(
        Long pedidoId,
        Long userId,
        StatusDoPedido statusDoPedido,
        BigDecimal valorTotal,
        LocalDate criadoEm,
        List<ItemDoPedidoRecordOut> itens



) {
}
