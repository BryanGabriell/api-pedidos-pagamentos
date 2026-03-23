package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto;


import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.ItemDoPedidoService;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.ItemDoPedidoRecordOut;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("v1/item-do-pedido")
@Tag(name = "Item Do Pedido", description = "Adiciona Endpoints de, Adicionar,Atualizar e Remover Itens")
public class ItemDoPedidoController {
 private final ItemDoPedidoService itemDoPedidoService;

    public ItemDoPedidoController(ItemDoPedidoService itemDoPedidoService) {
        this.itemDoPedidoService = itemDoPedidoService;
    }
    @PostMapping("/{pedidoId}/produto/{produtoId}")
    @Operation(summary = "Adiciona um novo item no sistema",
            description = "Realiza a abertura de um novo item no sistema de pedidos")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Item adicionado ao sistema com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados inseridos, não encontrados")
    }
    )
    public ResponseEntity<ItemDoPedidoRecordOut> adicionarItem(@PathVariable Long pedidoId, @PathVariable Long produtoId, @RequestParam Integer quantidade, UriComponentsBuilder uriComponentsBuilder){
     ItemDoPedidoRecordOut adicionarItem = itemDoPedidoService.adicionarItem(produtoId, pedidoId, quantidade);
    URI uri = uriComponentsBuilder.path("/v1/pedidos/{id}/itens")
             .buildAndExpand(adicionarItem.id())
            .toUri();
    return ResponseEntity.created(uri).body(adicionarItem);
    }
    @Operation(summary = "Atualiza a quantidade do item",
            description = "Realiza a atualização da quantidade de itens no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada, inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados inserido, não encontrados"),
            @ApiResponse(responseCode = "422", description = "Os dados enviados conflitam com as regras de negócio atuais")
    })
    @PatchMapping("/v1/pedidos/{pedidoId}/itens/{itemId}")
    public ResponseEntity<ItemDoPedidoRecordOut> atualizaQuantidadeItem(@PathVariable Long pedidoId, @PathVariable Long itemId,@RequestParam Integer novaQuantidade){
    ItemDoPedidoRecordOut atualizaQuantidade = itemDoPedidoService.atualizarQuantidadeItem(pedidoId, itemId, novaQuantidade);
    return ResponseEntity.ok(atualizaQuantidade);
    }
    @Operation(summary = "Deleta um item",
            description = "Realiza a remoção de um item do sistema de pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Remoção do item feita com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada, inválidos"),
            @ApiResponse(responseCode = "404", description = "Dados inseridos, não encontrados"),
            @ApiResponse(responseCode = "422", description = "Os dados enviados conflitam com as regras de negócio atuais")
    })
    @DeleteMapping("/v1/pedidos/{pedidoId}/itens/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long pedidoId, @PathVariable Long itemId){
     itemDoPedidoService.removerItem(pedidoId, itemId);
     return ResponseEntity.noContent().build();
    }
}
