package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception;

public class PedidoError extends RuntimeException {
    public PedidoError(String message) {
        super(message);
    }
}
