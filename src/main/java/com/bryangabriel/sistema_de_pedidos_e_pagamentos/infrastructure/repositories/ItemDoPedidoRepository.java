package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories;


import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.ItemDoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemDoPedidoRepository extends JpaRepository<ItemDoPedido,Long> {
}
