package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRecord(

       @NotBlank(message = "O campo nome não pode estar vazio")
       @Size(min = 3, max = 30, message = "O nome deve ter entre 3 e 30 caracteres") String nome,

       @NotBlank(message = "O campo e-mail não pode estar vazio")
       @Email(message = "Formato de e-mail invalido") String email
) {
}
