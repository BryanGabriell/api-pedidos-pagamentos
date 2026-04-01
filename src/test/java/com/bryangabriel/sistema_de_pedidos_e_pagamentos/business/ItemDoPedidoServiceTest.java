package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business;

import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.ItemDoPedidoRecordOut;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.mapper.IItemDoPedidoMapper;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.ItemDoPedido;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.Pedido;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.Produto;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.enums.StatusDoPedido;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception.*;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories.ItemDoPedidoRepository;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories.PedidoRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(value =
        MockitoExtension.class)
class  ItemDoPedidoServiceTest {

 @Mock
 private PedidoRepository pedidoRepository;
 @Mock
 private ProdutoRepository produtoRepository;
 @Mock
 private ItemDoPedidoValidator itemDoPedidoValidator;
 @Mock
 private CalculadoraDePedido calculadoraDePedido;
 @Mock
 private IItemDoPedidoMapper iItemDoPedidoMapper;
 @Mock
 private ItemDoPedidoRepository itemDoPedidoRepository;

 @InjectMocks
 private ItemDoPedidoService itemDoPedidoService;


 @Nested
 @DisplayName("Cenários de Sucesso")
 class Sucesso {
  @Test
  @DisplayName("Deve Adiciona Item Do Pedido")
  void deveAdicionaItemDoPedidoComSucesso() {
   Long produtoId = 10L;
   Long pedidoId = 1L;
   Integer quantidade = 10;
   BigDecimal calculoTotal = new BigDecimal("2000.00");

   Produto produto = new Produto();
   produto.setId(produtoId);
   produto.setNome("Teclado Mecânico");
   produto.setPreco(new BigDecimal("200.00"));

   Pedido pedido = new Pedido();
   pedido.setId(pedidoId);
   pedido.setStatusPedido(StatusDoPedido.CRIADO);
   pedido.setItens(new ArrayList<>());

   when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
   when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
   when(calculadoraDePedido.calcularTotal(any())).thenReturn(calculoTotal);

   ItemDoPedidoRecordOut itemDoPedidoRecordOut = new ItemDoPedidoRecordOut(3L, produtoId, produto.getNome(), quantidade, produto.getPreco(), calculoTotal);
   when(iItemDoPedidoMapper.paraOut(any(ItemDoPedido.class))).thenReturn(itemDoPedidoRecordOut);

   var resultado = itemDoPedidoService.adicionarItem(produtoId, pedidoId, quantidade);

   assertNotNull(resultado);
   verify(pedidoRepository, times(1)).findById(pedidoId);
   verify(produtoRepository, times(1)).findById(produtoId);
   verify(pedidoRepository).save(pedido);
  }

  @Test
  @DisplayName("Deve atualizar quantidade com sucesso")
  void deveAtualizaQuantidadeComSucesso(){
  Long pedidoId = 2L;
  Long itemId = 4L;
  Integer novaQuantidade = 20;
   BigDecimal calculoTotal = new BigDecimal("2500.00");


  ItemDoPedido itemDoPedido = new ItemDoPedido();
  itemDoPedido.setId(itemId);
  itemDoPedido.setQuantidade(10);

   Pedido pedido = new Pedido();
   pedido.setId(pedidoId);
   pedido.setStatusPedido(StatusDoPedido.CRIADO);
   pedido.setItens(new ArrayList<>(List.of(itemDoPedido)));

   ItemDoPedidoRecordOut itemDoPedidoRecordOut = new ItemDoPedidoRecordOut(
           itemDoPedido.getId(),
           10L,
           "VideoGame",
           novaQuantidade,
           itemDoPedido.getPrecoUnitario(),
           calculoTotal
   );

  when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
  when(calculadoraDePedido.calcularTotal(any())).thenReturn(calculoTotal);
  when(iItemDoPedidoMapper.paraOut(itemDoPedido)).thenReturn(itemDoPedidoRecordOut);

 var resultado = itemDoPedidoService.atualizarQuantidadeItem(pedidoId,itemId,novaQuantidade);

 assertNotNull(resultado);
 assertEquals(novaQuantidade , itemDoPedido.getQuantidade());
   assertEquals(calculoTotal, pedido.getValorTotal());
   verify(pedidoRepository, times(1)).save(pedido);
   verify(calculadoraDePedido).calcularTotal(pedido.getItens());
   verify(iItemDoPedidoMapper, times(1)).paraOut(itemDoPedido);
  }

  @Test
  @DisplayName("Deve retornar sucesso ao remover item")
  void deveRetornarSucessoAoRemoverItem(){
   Long pedidoId = 2L;
   Long itemId = 4L;
   BigDecimal novoCalculoTotal = new BigDecimal("2500.00");
   ItemDoPedido itemDoPedido = new ItemDoPedido();
   itemDoPedido.setId(itemId);
   itemDoPedido.setQuantidade(10);
   Pedido pedido = new Pedido();

   pedido.setId(pedidoId);
   pedido.setId(pedidoId);
   pedido.setStatusPedido(StatusDoPedido.CRIADO);
   pedido.setItens(new ArrayList<>(List.of(itemDoPedido)));

   when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
   when(calculadoraDePedido.calcularTotal(any())).thenReturn(novoCalculoTotal);

    itemDoPedidoService.removerItem(pedidoId,itemId);

   assertTrue(pedido.getItens().isEmpty(), "A lista de itens deveria estar vazia");
   assertEquals(novoCalculoTotal, pedido.getValorTotal());
   verify(calculadoraDePedido).calcularTotal(pedido.getItens());
   verify(pedidoRepository, times(1)).save(pedido);
  }
 }

 @Nested
 @DisplayName("Erros - Adicionar Item")
 class ErrosAdicionar {
  @Test
  @DisplayName("Deve retornar um PedidoNotFound quando id não existir")
  void adicionaItemDeveLancarErroQuandoIdDePedidoInexistente(){
   Long produtoId = 10L;
   Long pedidoId = 1L;
   Integer quantidade = 10;

   when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

   var erro = Assertions.catchThrowable(() ->
           itemDoPedidoService.adicionarItem(produtoId,pedidoId,quantidade));

   Assertions.assertThat(erro)
           .isInstanceOf(PedidoNotFound.class)
           .hasMessage("Pedido Com o " + pedidoId + "Não Existe");

   verify(pedidoRepository, never()).save(any());
  }

  @Test
  @DisplayName("Deve retornar ProdutoNotFound quando id não existir")
  void adicionaItemDeveLancarErroQuandoIdDeProdutoInexistente(){
   Long produtoId = 10L;
   Long pedidoId = 1L;
   Integer quantidade = 10;

   when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(new Pedido()));
   when(produtoRepository.findById(produtoId)).thenReturn(Optional.empty());

   var erro = Assertions.catchThrowable(() ->
           itemDoPedidoService.adicionarItem(produtoId,pedidoId,quantidade));

   Assertions.assertThat(erro).isInstanceOf(ProdutoNotFound.class).hasMessage("Esse Produto Com o " + produtoId + " Não existe");

   verify(produtoRepository, never()).save(any());
  }
  @Test
  @DisplayName("Deve lancar StatusInvalido quando o pedido nao for CRIADO")
  void adicionarItemDeveLancarErroQuandoStatusInvalido() {
   Long produtoId = 10L;
   Long pedidoId = 1L;
   StatusDoPedido statusInvalido = StatusDoPedido.CANCELADO;

   Pedido pedido = new Pedido();
   pedido.setId(pedidoId);
   pedido.setStatusPedido(statusInvalido);

   Produto produto = new Produto();
   produto.setId(produtoId);

   when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
   when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));

   doThrow(new StatusInvalido("Operação não permitida: o pedido deve estar no status CRIADO (status atual: " + statusInvalido + ")."))
           .when(itemDoPedidoValidator).validaStatusDoPedido(statusInvalido);

   var erro = Assertions.catchThrowable(() ->
           itemDoPedidoService.adicionarItem(produtoId, pedidoId, 5));

   Assertions.assertThat(erro)
           .isInstanceOf(StatusInvalido.class)
           .hasMessage("Operação não permitida: o pedido deve estar no status CRIADO (status atual: " + statusInvalido + ").");

   verify(pedidoRepository, never()).save(any());
  }

  @Test
  @DisplayName("Deve lancar QuantidadeInvalida quando quantidade for menor ou igual a zero")
  void adicionarItemDeveLancarErroQuandoQuantidadeInvalida() {
   Long produtoId = 10L;
   Long pedidoId = 1L;
   Integer quantidadeInvalida = 0;

   Pedido pedido = new Pedido();
   Produto produto = new Produto();

   when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
   when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));

   doThrow(new QuantidadeInvalida("A quantidade deve ser maior que zero"))
           .when(itemDoPedidoValidator).validaQuantidadeValida(quantidadeInvalida);
   var erro = Assertions.catchThrowable(() ->
           itemDoPedidoService.adicionarItem(produtoId, pedidoId, quantidadeInvalida));

   Assertions.assertThat(erro)
           .isInstanceOf(QuantidadeInvalida.class)
           .hasMessage("A quantidade deve ser maior que zero");

   verify(pedidoRepository, never()).save(any());
  }
 }

 @Nested
 @DisplayName("Erros - Atualizar Quantidade")
 class ErrosAtualizar {

  @Test
  @DisplayName("Deve retornar um PedidoNotFound ao buscar pedido por Id e não existir")
  void atualizaItemQuantidadeDeveRetornarErroCasoIdNaoexista(){
   Long itemId = 1L;
   Long pedidoId = 10L;
   Integer novaQuantidade = 10;

   when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

   var erro = Assertions.catchThrowable(() ->
           itemDoPedidoService.atualizarQuantidadeItem(pedidoId,itemId,novaQuantidade));

   Assertions.assertThat(erro).isInstanceOf(PedidoNotFound.class).hasMessage("Erro Pedido Com o " + pedidoId + "Não Encontrado");

   verify(pedidoRepository, never()).save(any());
  }

  @Test
  @DisplayName("Deve retornar erro caso item não seja encontrado por id")
  void atualizaQuantidadeDeveRetornarUmErroCasoItemNaoExista(){
   Long itemBuscado = 1L;
   Long itemDiferenteId = 25L;
   Long pedidoId = 10L;
   Integer novaQuantidade = 10;

   ItemDoPedido itemErrado = new ItemDoPedido();
   itemErrado.setId(itemDiferenteId);

   Pedido pedido = new Pedido();
   pedido.setId(pedidoId);
   pedido.setStatusPedido(StatusDoPedido.CRIADO);
   pedido.setItens(new ArrayList<>(List.of(itemErrado)));

   when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

   var erro = Assertions.catchThrowable(() ->
           itemDoPedidoService.atualizarQuantidadeItem(pedidoId,itemBuscado,novaQuantidade));

   Assertions.assertThat(erro).isInstanceOf(ItemProdutoNotFound.class).hasMessage("Erro item não encontrado");
   verify(pedidoRepository, never()).save(any());
  }

  @Test
  @DisplayName("Deve lancar QuantidadeInvalida quando quantidade for menor ou igual a zero")
  void atualizaQuantidadeItemDeveLancarErroQuandoQuantidadeInvalida() {
   Long itemId = 10L;
   Long pedidoId = 1L;
   Integer quantidadeInvalida = 0;

   ItemDoPedido itemDoPedido = new ItemDoPedido();
   itemDoPedido.setId(itemId);

   Pedido pedido = new Pedido();
   pedido.setId(pedidoId);
   pedido.setItens(new ArrayList<>(List.of(itemDoPedido)));

   when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));


   doThrow(new QuantidadeInvalida("A quantidade deve ser maior que zero"))
           .when(itemDoPedidoValidator).validaQuantidadeValida(quantidadeInvalida);
   var erro = Assertions.catchThrowable(() ->
           itemDoPedidoService.atualizarQuantidadeItem(pedidoId, itemId, quantidadeInvalida));

   Assertions.assertThat(erro)
           .isInstanceOf(QuantidadeInvalida.class)
           .hasMessage("A quantidade deve ser maior que zero");

   verify(pedidoRepository, never()).save(any());
  }

  @Test
  @DisplayName("Deve lancar StatusInvalido quando o pedido nao for CRIADO")
  void atualizaQuantidadeItemDeveLancarErroQuandoStatusInvalido() {
   Long itemId = 10L;
   Long pedidoId = 1L;
   StatusDoPedido statusInvalido = StatusDoPedido.CANCELADO;

   Pedido pedido = new Pedido();
   pedido.setId(pedidoId);
   pedido.setStatusPedido(statusInvalido);

   when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

   doThrow(new StatusInvalido("Operação não permitida: o pedido deve estar no status CRIADO (status atual: " + statusInvalido + ")."))
           .when(itemDoPedidoValidator).validaStatusDoPedido(statusInvalido);

   var erro = Assertions.catchThrowable(() ->
           itemDoPedidoService.atualizarQuantidadeItem(pedidoId, itemId, 5));

   Assertions.assertThat(erro)
           .isInstanceOf(StatusInvalido.class)
           .hasMessage("Operação não permitida: o pedido deve estar no status CRIADO (status atual: " + statusInvalido + ").");

   verify(pedidoRepository, never()).save(any());
  }
 }
 @Nested
 @DisplayName("Cenários de Erro - Remover Item")
 class ErrosRemoverItem {

  @Test
  @DisplayName("Deve lançar PedidoNotFound quando o ID do pedido não existir no banco")
  void deveLancarPedidoNotFound_QuandoPedidoInexistente() {
   Long pedidoId = 1L;
   Long itemId = 50L;

   when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

   assertThrows(PedidoNotFound.class, () ->
           itemDoPedidoService.removerItem(pedidoId, itemId)
   );
   verify(pedidoRepository, never()).save(any());
   verifyNoInteractions(calculadoraDePedido);
  }

  @Test
  @DisplayName("Deve lançar exceção quando o validador reprovar o status do pedido")
  void deveLancarExcecao_QuandoStatusDoPedidoForInvalido() {
   Long pedidoId = 1L;
   Long itemId = 50L;

   Pedido pedidoMock = new Pedido();
   pedidoMock.setId(pedidoId);
   pedidoMock.setStatusPedido(StatusDoPedido.PAGO);

   when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoMock));

   doThrow(new RuntimeException("Pedido com status PAGO não pode ser alterado"))
           .when(itemDoPedidoValidator).validaStatusDoPedido(StatusDoPedido.PAGO);


   assertThrows(RuntimeException.class, () ->
           itemDoPedidoService.removerItem(pedidoId, itemId)
   );
   verify(pedidoRepository, never()).save(any());
  }

  @Test
  @DisplayName("Deve lançar ItemProdutoNotFound quando o item não estiver na lista do pedido")
  void deveLancarItemProdutoNotFound_QuandoItemNaoPertenceAoPedido() {

   Long pedidoId = 1L;
   Long itemIdErrado = 99L;

   Pedido pedidoMock = new Pedido();
   pedidoMock.setId(pedidoId);
   pedidoMock.setStatusPedido(StatusDoPedido.CRIADO);
   pedidoMock.setItens(new ArrayList<>());
   when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedidoMock));

   assertThrows(ItemProdutoNotFound.class, () ->
           itemDoPedidoService.removerItem(pedidoId, itemIdErrado)
   );
   verify(itemDoPedidoValidator).validaStatusDoPedido(any());
   verify(pedidoRepository, never()).save(any());
  }
 }
}