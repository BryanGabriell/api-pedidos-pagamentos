package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Table(name = "produtos")
@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "preco", nullable = false)
    private BigDecimal preco;
    @Column(name = "estoque", nullable = false)
    private Integer estoque;
}
