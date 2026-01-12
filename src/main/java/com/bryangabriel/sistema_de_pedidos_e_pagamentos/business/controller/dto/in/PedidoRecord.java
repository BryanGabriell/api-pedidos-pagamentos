package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in;

import java.math.BigDecimal;
import java.util.List;

public record PedidoRecord(Long userId,
                           BigDecimal valorTotal,
                           List<ItemDoPedidoRecord> itens) {
}
