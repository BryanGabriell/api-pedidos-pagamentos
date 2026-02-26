package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "users")
@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 30)
    private String nome;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
}
