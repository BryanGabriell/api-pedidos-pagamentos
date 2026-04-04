package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Table(name = "item_pedidos")
@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class ItemDoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedidos_id")
    private Pedido pedido;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private BigDecimal precoUnitario;

}
