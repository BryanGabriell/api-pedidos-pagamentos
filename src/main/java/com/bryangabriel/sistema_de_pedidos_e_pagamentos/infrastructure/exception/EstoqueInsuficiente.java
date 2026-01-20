package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception;



public class EstoqueInsuficiente extends RuntimeException {
    public EstoqueInsuficiente(String message) {
        super(message);
    }
}
