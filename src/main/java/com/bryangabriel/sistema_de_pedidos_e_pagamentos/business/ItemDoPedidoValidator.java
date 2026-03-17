package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business;


import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.enums.StatusDoPedido;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception.QuantidadeInvalida;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception.StatusInvalido;
import org.springframework.stereotype.Component;

@Component
public class ItemDoPedidoValidator {

    public void validaQuantidadeValida(Integer quantidade){
        if (!(quantidade <= 0)){
            throw new QuantidadeInvalida("A quantidade deve ser maior que zero");
        }
    }

    public void validaStatusDoPedido(StatusDoPedido statusDoPedido){
        if (!statusDoPedido.equals(StatusDoPedido.CRIADO)){
            throw new StatusInvalido("Operação não permitida: o pedido deve estar no status CRIADO (status atual: " + statusDoPedido + ").");
        }
    }


}
