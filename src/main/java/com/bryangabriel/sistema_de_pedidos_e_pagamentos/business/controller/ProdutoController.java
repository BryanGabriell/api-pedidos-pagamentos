package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller;


import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.ProdutoService;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.ProdutoAtualizaRecord;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.ProdutoRecord;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.ProdutoRecordOut;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/produtos")
@Tag(name = "Produtos", description = "Endpoints destinados a criação de produtos, atualização, busca por id, e exclusão de produto")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    @Operation(summary = "Cria um novo produto",
            description = "Realiza criação de um novo produto no sistema e retorna os dados salvos com URI  de acesso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Novo produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou erro de validação")
    })
    public ResponseEntity<ProdutoRecordOut> criar(@RequestBody @Valid ProdutoRecord produtoRecord, UriComponentsBuilder uriComponentsBuilder){
        ProdutoRecordOut produto = produtoService.criarProduto(produtoRecord);

        URI uri = uriComponentsBuilder.path("/v1/produtos/{id}").
                buildAndExpand(produto.id()).
                toUri();

       return ResponseEntity.created(uri).body(produto);
    }


    @GetMapping("/{produtoId}")
    @Operation(summary = "Busca um produto pelo id",
            description = "Realiza a busca de produtos no sistema pelo seu identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto buscado no banco com sucesso"),
            @ApiResponse(responseCode = "404",description = "Erro: O produto buscado não foi achado no sistema"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
    public ResponseEntity<ProdutoRecordOut> buscarProdutoPorId(@PathVariable("produtoId") Long produtoId){
    ProdutoRecordOut buscarProduto = produtoService.buscarProdutoPorId(produtoId);

    return ResponseEntity.ok(buscarProduto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza dados de um produto",
            description = "Realiza a atualização de dados de um produto pelo seu identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400",description = "Dados de entrada inválidos ou erro de validação"),
            @ApiResponse(responseCode = "404",description = "Erro: O identificador passado para atualização não existe")
    })
    public ResponseEntity<ProdutoRecordOut> atualizarProduto(@PathVariable Long id, @RequestBody @Valid ProdutoAtualizaRecord produtoAtualizaRecord){
        ProdutoRecordOut atualizarProduto = produtoService.atualizarProduto(id, produtoAtualizaRecord);

        return ResponseEntity.ok(atualizarProduto);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta o produto por id",
            description = "Realiza a exclusão de um produto pelo seu identificador id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Exclusão de produto feita com sucesso"),
            @ApiResponse(responseCode = "400",description = "Dados de entrada inválidos ou erro na validação"),
            @ApiResponse(responseCode = "404", description = "Erro: O identificador passado não existe")
    })
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id){
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }

}
