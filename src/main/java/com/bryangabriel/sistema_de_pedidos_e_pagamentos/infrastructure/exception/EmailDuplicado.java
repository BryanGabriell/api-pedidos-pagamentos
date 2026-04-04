package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception;

 @author bryan
 
public class EmailDuplicado extends RuntimeException {
  public EmailDuplicado(String message) {
    super(message);
  }
}
