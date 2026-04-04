package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception;


public class UsuarioNotFound extends RuntimeException {
    public UsuarioNotFound(String message) {
        super(message);
    }
}
