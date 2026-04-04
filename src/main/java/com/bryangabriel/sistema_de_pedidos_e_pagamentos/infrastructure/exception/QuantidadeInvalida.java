package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception;


public class QuantidadeInvalida extends RuntimeException {
    public QuantidadeInvalida(String message) {
        super(message);
    }
}
