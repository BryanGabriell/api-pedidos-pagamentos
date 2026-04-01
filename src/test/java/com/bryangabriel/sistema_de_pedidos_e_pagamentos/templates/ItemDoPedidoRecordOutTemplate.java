package com.bryangabriel.sistema_de_pedidos_e_pagamentos.templates;

import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.ItemDoPedidoRecordOut;
import java.math.BigDecimal;

public class ItemDoPedidoRecordOutTemplate {

    public static ItemDoPedidoRecordOut itemDoPedidoRecordOutTemplate() {
        return new ItemDoPedidoRecordOut(
                1L,
                10L,
                "Produto Teste",
                2,
                new BigDecimal("100.00"),
                new BigDecimal("200.00")
        );
    }
}