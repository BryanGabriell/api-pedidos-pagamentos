package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception;


public class PedidoNotFound extends RuntimeException {
    public PedidoNotFound(String message) {
        super(message);
    }
}
