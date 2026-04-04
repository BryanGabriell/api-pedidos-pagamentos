package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception;


public class ProdutoNotFound extends RuntimeException {
    public ProdutoNotFound(String message) {
        super(message);
    }
}
