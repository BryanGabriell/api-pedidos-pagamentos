package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.mapper;

import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.ItemDoPedidoRecord;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.ItemDoPedidoRecordOut;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.ItemDoPedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IItemDoPedidoMapper {
    ItemDoPedido paraEntity(ItemDoPedidoRecord itemDoPedido);
    ItemDoPedidoRecordOut paraOut(ItemDoPedido itemPedido);
}