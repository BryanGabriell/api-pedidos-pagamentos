package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out;



import java.math.BigDecimal;

public record ProdutoRecordOut(Long id,
String nome,
BigDecimal preco,
Integer estoque
) {
}
