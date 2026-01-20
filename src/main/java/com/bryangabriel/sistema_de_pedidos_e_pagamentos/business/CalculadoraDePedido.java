package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business;

import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.ItemDoPedido;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CalculadoraDePedido {

    public BigDecimal calcularTotal(List<ItemDoPedido> itens){
        return itens.stream().map(item ->
                item.getPrecoUnitario()
                        .multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
