package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business;

import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.ProdutoAtualizaRecord;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.ProdutoRecord;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.ProdutoRecordOut;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.mapper.ProdutoMapper;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.Produto;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception.ProdutoNotFound;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception.UsuarioNotFound;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories.ProdutoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Cenários de testes do ProdutoService")
class ProdutoServiceTest {

    @Mock
    ProdutoRepository produtoRepository;

    @Mock
    ProdutoMapper produtoMapper;

    @InjectMocks
    ProdutoService produtoService;

    @Nested
    @DisplayName("Cenarios de sucesso")
    class Sucesso{

        @Test
        @DisplayName("Deve retornar Sucesso ao criar um produto")
        void deveCriarProdutoComSucesso(){
            ProdutoRecord produto = new ProdutoRecord("Coca-Cola", new BigDecimal("10.50"), 80);
            Produto produtoEntity = new Produto();
            produtoEntity.setNome(produto.nome());
            produtoEntity.setPreco(produto.preco());
            produtoEntity.setEstoque(produto.estoque());

            when(produtoMapper.paraEntity(produto)).thenReturn(produtoEntity);
            when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> {
                invocation.getArgument(0);
                produtoEntity.setId(1L);
                return produtoEntity;
            });
            when(produtoMapper.paraOut(any(Produto.class))).thenReturn(new ProdutoRecordOut(produtoEntity.getId(),
                    produtoEntity.getNome(),
                    produtoEntity.getPreco(),
                    produtoEntity.getEstoque()));

            var output = produtoService.criarProduto(produto);

            assertNotNull(output);
            verify(produtoRepository, times(1)).save(produtoEntity);
            verify(produtoMapper, times(1)).paraOut(produtoEntity);

        }

        @Test
        @DisplayName("Deve retornar sucesso ao buscar um produto por Id")
        void deveBuscarProdutoComSucesso(){
            Long id = 1L;
            Produto produto = new Produto();
            produto.setId(id);
            produto.setNome("Coca");
            produto.setPreco(new BigDecimal("10.50"));
            produto.setEstoque(10);

            when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));
            when(produtoMapper.paraOut(produto)).thenReturn(new ProdutoRecordOut(produto.getId(),
                    produto.getNome(),
                    produto.getPreco(),
                    produto.getEstoque()));


            var output = produtoService.buscarProdutoPorId(id);

            assertNotNull(output);
            verify(produtoMapper, times(1)).paraOut(produto);
            verify(produtoRepository, times(1)).findById(id);

        }

        @Test
        @DisplayName("Deve Retornar a atualização dos dados do produto com sucesso")
        void deveAtualizarOProdutoComSucesso(){
            Long id = 2L;
            Produto produtoEntity = new Produto(id,"Coca-cola", new BigDecimal("15.00"), 4);
            ProdutoAtualizaRecord dadosAtualizados = new ProdutoAtualizaRecord("Salgado", new BigDecimal("10.00"), 2);

            when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoEntity));
            when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation ->
                    invocation.getArgument(0));
            when(produtoMapper.paraOut(any(Produto.class))).thenReturn(new ProdutoRecordOut(id, "Salgado", new BigDecimal("10.00"), 2));

            var output = produtoService.atualizarProduto(id,dadosAtualizados);

            assertEquals("Salgado", output.nome());
            assertEquals("Salgado", produtoEntity.getNome());
            assertNotNull(output);
            verify(produtoRepository).findById(id);
            verify(produtoRepository).save(produtoEntity);
            verify(produtoMapper).paraOut(produtoEntity);
        }

        @Test
        @DisplayName("Deve realizar a exclusão de produto com sucesso")
        void deveDeletarProdutoComSucesso(){
            Long id = 1L;
            when(produtoRepository.existsById(id)).thenReturn(true);
            produtoService.deletarProduto(id);
            verify(produtoRepository).deleteById(id);
        }

    }

    @Nested
    @DisplayName("Cenários de Erros")
    class Excecoes{

        @Test
        @DisplayName("Deve lançar ProdutoNotFound quando o ID não existir")
        void atualizarProdutoDeveLancarErroQuandoIdInexistente() {
            Long idInexistente = 99L;
            ProdutoAtualizaRecord record = new ProdutoAtualizaRecord("Nome", new BigDecimal("10.0"), 5);

            when(produtoRepository.findById(idInexistente)).thenReturn(Optional.empty());

            var erro = Assertions.catchThrowable(() ->
                    produtoService.atualizarProduto(idInexistente,record));

            Assertions.assertThat(erro).
                    isInstanceOf(ProdutoNotFound.class).
                    hasMessage("Erro produto não encontrado");
        }

        @Test
        @DisplayName("Deve lançar RuntimeException quando o preço for negativo")
        void atualizarProdutoDeveLancarErroQuandoPrecoNegativo() {
            Long idValido = 1L;
            Produto produtoExistente = new Produto(idValido, "Antigo", new BigDecimal("20.0"), 10);
            ProdutoAtualizaRecord recordComPrecoNegativo = new ProdutoAtualizaRecord(null, new BigDecimal("-5.0"), null);

            when(produtoRepository.findById(idValido)).thenReturn(Optional.of(produtoExistente));

            var erro = Assertions.catchThrowable(() -> {
                produtoService.atualizarProduto(idValido, recordComPrecoNegativo);
            });

            Assertions.assertThat(erro)
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Preço invalido");

            assertEquals("Preço invalido", erro.getMessage());
        }

        @Test
        @DisplayName("Deve lançar RuntimeException quando o estoque for negativo")
        void atualizarProdutoDeveLancarErroQuandoEstoqueNegativo() {
            Long idValido = 1L;
            Produto produtoExistente = new Produto(idValido, "Antigo", new BigDecimal("20.0"), 10);
            ProdutoAtualizaRecord recordComEstoqueNegativo = new ProdutoAtualizaRecord(null, null, -1);

            when(produtoRepository.findById(idValido)).thenReturn(Optional.of(produtoExistente));


            var erro = Assertions.catchThrowable(() -> {
                produtoService.atualizarProduto(idValido, recordComEstoqueNegativo);
            });

            Assertions.assertThat(erro).isInstanceOf(RuntimeException.class).hasMessage("Estoque Invalido");

            assertEquals("Estoque Invalido", erro.getMessage());
        }

        @Test
        @DisplayName("Deve retornar uma exception ao não existir produto quando buscar")
        void deveRetornarUmaExcecaoCasoProdutoNaoExista(){
            Long id = 2L;
            when(produtoRepository.findById(id)).thenReturn(Optional.empty());


            var  erro = Assertions.catchThrowable(() ->
                    produtoService.buscarProdutoPorId(id));

            Assertions.assertThat(erro)
                    .isInstanceOf(ProdutoNotFound.class)
                    .hasMessage("Erro produto não encontrado");

            verify(produtoRepository, times(1)).findById(id);
        }

        @Test
        @DisplayName("Deve Retornar exceção caso produto não exista")
        void deveRetornarUmaExceptionCasoProdutoNaoExista(){
            Long id = 1L;
            when(produtoRepository.existsById(id)).thenReturn(false);

            var erro = Assertions.catchThrowable(() ->
                    produtoService.deletarProduto(id));

            Assertions.assertThat(erro).
                    isInstanceOf(ProdutoNotFound.class).
                    hasMessage("Produto não encontrado");
            assertNotNull(erro);
        }
    }
}