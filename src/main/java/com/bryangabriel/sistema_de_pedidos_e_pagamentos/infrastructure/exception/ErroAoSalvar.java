package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception;


public class ErroAoSalvar extends RuntimeException {
    public ErroAoSalvar(String message) {
        super(message);
    }
}
