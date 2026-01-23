package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto;

import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.PedidoService;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.ItemDoPedidoRecord;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.PedidoRecordOut;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/pedidos")
@Tag(name = "Pedidos", description = "Endpoints destinados à criação, pagamento e cancelamento de pedidos")
public class PedidoController {

 private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }
    @PostMapping
    @Operation(summary = "Cria um novo pedido com um item",
    description = "Realiza a abertura de um pedido para um usuário específico, associa um produto inicial, " +
            "calcula automaticamente o valor total com base no preço do produto e salva no banco de dados.")
    @ApiResponse(responseCode = "201", description = "Pedido gerado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Usuário ou Produto não encontrado")
    public ResponseEntity<PedidoRecordOut> criar(@RequestParam Long userId, @RequestParam Long produtoId, @RequestBody @Valid ItemDoPedidoRecord itemDoPedidoRecord, UriComponentsBuilder uriComponentsBuilder){
     PedidoRecordOut pedido = pedidoService.criar(userId, produtoId, itemDoPedidoRecord);

     URI uri = uriComponentsBuilder
             .path("/v1/pedidos/{id}")
             .buildAndExpand(pedido.pedidoId())
             .toUri();

     return ResponseEntity.created(uri).body(pedido);
    }
    @PatchMapping("/{pedidoId}/pagar")
    @Operation(summary = "Pagar um pedido",
    description = "Altera o status do pedido para PAGO e abate os itens do estoque")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido pago com sucesso e estoque atualizado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada (Estoque insuficiente ou status inválido)")
    })
    public ResponseEntity<PedidoRecordOut> pagarPedido(@PathVariable Long pedidoId){
        PedidoRecordOut pagarPedido = pedidoService.pagarPedido(pedidoId);
        return ResponseEntity.status(200).body(pagarPedido);
    }

    @PatchMapping("/{pedidoId}/cancelar")
    @Operation(summary = "Cancela um pedido pendente",
            description = "Altera o status de um pedido de CRIADO para CANCELADO. Pedidos já pagos não podem ser cancelados por este método.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description = "ID do pedido não localizado"),
            @ApiResponse(responseCode = "422", description = "Pedido já pago ou já cancelado")
    })
    public ResponseEntity<PedidoRecordOut> cancelar(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pedidoService.cancelarPedido(pedidoId));
    }

    @GetMapping("/{userId}/listar")
    @Operation(summary = "Lista pedidos por um usuario",
            description = "Lista pedidos por usuario utilizando o endereço id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido listado por usuario com sucesso"),
            @ApiResponse(responseCode = "404", description = "ID do usuario não localizado")
    })
    public ResponseEntity<List<PedidoRecordOut>> listarPedidosPorUsuario(@PathVariable Long userId) {
        List<PedidoRecordOut> listar = pedidoService.listarPedidoPorUsuario(userId);
        return ResponseEntity.ok(listar);
    }
}
