package com.bryangabriel.sistema_de_pedidos_e_pagamentos.templates;

import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.ItemDoPedidoRecordOut;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.PedidoRecordOut;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.enums.StatusDoPedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PedidoRecordOutTemplate {

    public static PedidoRecordOut pedidoRecordOutTemplate() {
        ItemDoPedidoRecordOut itemOut = ItemDoPedidoRecordOutTemplate.itemDoPedidoRecordOutTemplate();

        return new PedidoRecordOut(
                1L,
                1L,
                StatusDoPedido.CRIADO,
                new BigDecimal("200.00"),
                LocalDate.now(),
                List.of(itemOut)
        );
    }
}