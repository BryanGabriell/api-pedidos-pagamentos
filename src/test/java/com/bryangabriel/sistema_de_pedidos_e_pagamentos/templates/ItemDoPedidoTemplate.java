package com.bryangabriel.sistema_de_pedidos_e_pagamentos.templates;

import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.ItemDoPedidoRecord;

import java.math.BigDecimal;

public class ItemDoPedidoTemplate {

    public static ItemDoPedidoRecord criarItemRecordComQuantidade(Integer quantidade) {
        return new ItemDoPedidoRecord(
                1L,
                "Produto Teste",
                quantidade,
                new BigDecimal("100.00")
        );
    }
}