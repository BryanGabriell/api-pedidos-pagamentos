package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception;



public class EmailDuplicado extends RuntimeException {
    public EmailDuplicado(String message) {
        super(message);
    }
}
