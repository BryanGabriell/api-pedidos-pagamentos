package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception;

public class StatusInvalido extends RuntimeException {
    public StatusInvalido(String message) {
        super(message);
    }
}
