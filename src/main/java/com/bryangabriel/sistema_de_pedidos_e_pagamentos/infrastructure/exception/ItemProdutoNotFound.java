package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception;


public class ItemProdutoNotFound extends RuntimeException {
    public ItemProdutoNotFound(String message) {
        super(message);
    }
}
