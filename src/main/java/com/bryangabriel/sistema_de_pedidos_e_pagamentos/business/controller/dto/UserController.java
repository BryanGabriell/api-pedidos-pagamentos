package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto;


import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.UserService;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.UserRecord;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.UserRecordOut;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/usuarios")
@Tag(name = "Usuários", description = "Endpoints destinados à criação, consulta, atualização e exclusão de usuários.")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Cria um novo usuário",
            description = "Realiza a criação de um novo usuário no sistema e retorna os dados salvos com URI  de acesso.")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou erro de validação")
    @ApiResponse(responseCode = "409", description = "Conflito: Usuário ja cadastrado (ex: e-mail duplicado")
    public ResponseEntity<UserRecordOut> criarUsuario(@RequestBody @Valid UserRecord userRecord, UriComponentsBuilder uriComponentsBuilder) {
        UserRecordOut usuario = userService.criarUsuario(userRecord);

        URI uri = uriComponentsBuilder
                .path("/v1/usuarios/{id}")
                .buildAndExpand(usuario.id())
                .toUri();
        return ResponseEntity.created(uri).body(usuario);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um usuários por ID",
            description = "Retorna os detalhes de um usuário especifico com base no identificador único fornecido.")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<UserRecordOut> buscarPorId(@PathVariable Long id) {
        UserRecordOut usuario = userService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    @Operation(summary = "Lista todos os usuários",
            description = "Retorna uma lista de todos os usuários cadastrados. Se não houver usuários, retorna uma lista vazia.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso (pode estar vazia)")
    public ResponseEntity<Page<UserRecordOut>> listarUsuarios(Pageable pageable) {
        Page<UserRecordOut> listar = userService.listarUsuarios(pageable);
        return ResponseEntity.ok(listar);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza os dados de um usuário",
            description = "Modifica as informações de um usuário existente com base no ID fornecido e nos novos dados enviados no corpo da requisição.")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<UserRecordOut> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid UserRecord userRecord) {
        UserRecordOut usuarioAtualizado = userService.atualizarUsuario(id, userRecord);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um usuário",
            description = "Remove permanentemente o registro de um usuário do sistema através do ID informado.")
    @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        userService.deletarUsuario(id);
        return ResponseEntity.noContent().build();

    }
}