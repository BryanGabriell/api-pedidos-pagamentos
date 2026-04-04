package com.bryangabriel.sistema_de_pedidos_e_pagamentos.templates;

import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.ItemDoPedidoRecord;

public class ItemDoPedidoTemplate {

    public static ItemDoPedidoRecord criarItemRecordComQuantidade(Integer quantidade) {
        return new ItemDoPedidoRecord(
                1L,
                quantidade
        );
    }
}