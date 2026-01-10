package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories;


import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
