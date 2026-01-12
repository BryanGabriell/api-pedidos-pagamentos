package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in;


import java.math.BigDecimal;

public record ItemDoPedidoRecord(Long produtoId,
                                 String nomeDoProduto,
                                 Integer quantidade,
                                 BigDecimal precoUnitario) {
}
