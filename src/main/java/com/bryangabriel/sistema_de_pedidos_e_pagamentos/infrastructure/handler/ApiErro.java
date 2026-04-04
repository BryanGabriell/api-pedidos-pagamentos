package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.handler;


public record ApiErro(
        String mensagem,
        int status,
        String path
) {
}
