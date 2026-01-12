package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.mapper;

import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.ItemDoPedidoRecord;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.PedidoRecord;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.ItemDoPedidoRecordOut;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.PedidoRecordOut;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.ItemDoPedido;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.Pedido;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface IItemDoPedidoMapper {

    ItemDoPedido paraEntity(ItemDoPedidoRecord itemDoPedido);

    ItemDoPedidoRecordOut paraOut(ItemDoPedido itemPedido);
}
