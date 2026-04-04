package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.mapper;

import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.PedidoRecord;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.PedidoRecordOut;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.Pedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IPedidoMapper {

    Pedido paraEntity(PedidoRecord pedido);

    PedidoRecordOut paraOut(Pedido pedido);
}
