package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business;

import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.ItemDoPedidoRecord;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.PedidoRecordOut;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.mapper.IPedidoMapper;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.Pedido;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.Produto;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.User;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.enums.StatusDoPedido;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception.PedidoNotFound;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception.ProdutoNotFound;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception.UsuarioNotFound;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories.PedidoRepository;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories.ProdutoRepository;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories.UserRepository;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.templates.ItemDoPedidoTemplate;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.templates.PedidoRecordOutTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do Serviço de Pedidos")
@Slf4j
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CalculadoraDePedido calculadoraDePedido;

    @Mock
    private IPedidoMapper pedidoMapper;

    @InjectMocks
    private PedidoService pedidoService;


    @Nested
    @DisplayName("Cenários de Sucesso")
    class Sucesso {

        @Test
        @DisplayName("Deve criar o pedido com sucesso")
        void deveCriarPedidoComSucesso() {

            Long userId = 10L;
            Long produtoId = 3L;
            ItemDoPedidoRecord itemFake = ItemDoPedidoTemplate.criarItemRecordComQuantidade(5);

            User userFake = new User();
            userFake.setId(userId);

            Produto produtoFake = new Produto();
            produtoFake.setId(produtoId);
            produtoFake.setNome("Skate");
            produtoFake.setPreco(new BigDecimal("300.00"));
            produtoFake.setEstoque(10);

            when(userRepository.findById(userId)).thenReturn(Optional.of(userFake));
            when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produtoFake));
            when(calculadoraDePedido.calcularTotal(any())).thenReturn(new BigDecimal("1500.00"));
            when(pedidoMapper.paraOut(any(Pedido.class)))
                    .thenReturn(PedidoRecordOutTemplate.pedidoRecordOutTemplate());

            PedidoRecordOut resultado = pedidoService.criar(userId, produtoId, itemFake);

            Assertions.assertNotNull(resultado);
            verify(userRepository, times(1)).findById(userId);
            verify(produtoRepository, times(1)).findById(produtoId);
            verify(calculadoraDePedido, times(1)).calcularTotal(any());
            verify(pedidoRepository, times(1)).save(any(Pedido.class));
            verify(pedidoMapper, times(1)).paraOut(any(Pedido.class));
        }

        @Test
        @DisplayName("Deve pagar o pedido com sucesso")
        void pagarPedidoComSucesso() {
            Long pedidoId = 15L;

            Pedido pedido = new Pedido();
            pedido.setId(pedidoId);
            pedido.setStatusPedido(StatusDoPedido.CRIADO);
            pedido.setItens(new ArrayList<>());
            pedido.setValorTotal(new BigDecimal("300.00"));

            when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
            when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(pedidoMapper.paraOut(any(Pedido.class)))
                    .thenReturn(PedidoRecordOutTemplate.pedidoRecordOutTemplate());

            PedidoRecordOut output = pedidoService.pagarPedido(pedidoId);

            Assertions.assertNotNull(output);
            Assertions.assertEquals(StatusDoPedido.PAGO, pedido.getStatusPedido());
            verify(pedidoRepository, times(1)).findById(pedidoId);
            verify(pedidoRepository, times(1)).save(pedido);
            verify(pedidoMapper, times(1)).paraOut(pedido);
        }

        @Test
        @DisplayName("Deve cancelar o pedido com sucesso")
        void cancelarPedidoComSucesso() {
            Long pedidoId = 19L;
            Long userId = 11L;

            User user = new User();
            user.setId(userId);

            Pedido pedido = new Pedido();
            pedido.setId(pedidoId);
            pedido.setStatusPedido(StatusDoPedido.CRIADO);
            pedido.setUser(user);

            when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
            when(pedidoMapper.paraOut(any(Pedido.class)))
                    .thenReturn(PedidoRecordOutTemplate.pedidoRecordOutTemplate());

            PedidoRecordOut output = pedidoService.cancelarPedido(pedidoId);

            Assertions.assertNotNull(output);
            Assertions.assertEquals(StatusDoPedido.CANCELADO, pedido.getStatusPedido());
            verify(pedidoRepository, times(1)).findById(pedidoId);
            verify(pedidoRepository, times(1)).save(pedido);
            verify(pedidoMapper, times(1)).paraOut(pedido);
        }
        @Test
        @DisplayName("Deve listar pedidos por usuário com sucesso")
        void listarPedidoPorUsuarioComSucesso() {
            Long userId = 10L;

            User user = new User();
            user.setId(userId);

            Pedido pedido1 = new Pedido();
            Pedido pedido2 = new Pedido();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(pedidoRepository.findByUserId(userId)).thenReturn(List.of(pedido1, pedido2));
            when(pedidoMapper.paraOut(any(Pedido.class)))
                    .thenReturn(PedidoRecordOutTemplate.pedidoRecordOutTemplate());

            List<PedidoRecordOut> resultado = pedidoService.listarPedidoPorUsuario(userId);

            Assertions.assertNotNull(resultado);
            Assertions.assertEquals(2, resultado.size());
            verify(userRepository, times(1)).findById(userId);
            verify(pedidoRepository, times(1)).findByUserId(userId);
            verify(pedidoMapper, times(2)).paraOut(any(Pedido.class));
        }
    }


    @Nested
    @DisplayName("Cenários de Exceção")
    class Excecoes {

        @Test
        @DisplayName("Deve lançar exceção quando usuário não existir")
        void deveLancarExcecaoQuandoUsuarioNaoExiste() {
            Long userId = 10L;
            Long produtoId = 11L;
            ItemDoPedidoRecord itemFake = ItemDoPedidoTemplate.criarItemRecordComQuantidade(5);

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            UsuarioNotFound exception = Assertions.assertThrows(
                    UsuarioNotFound.class,
                    () -> pedidoService.criar(userId, produtoId, itemFake)
            );

            String mensagemEsperada = "Erro ao criar o pedido o " + userId + "não existe";
            Assertions.assertEquals(mensagemEsperada, exception.getMessage());

            verify(userRepository, times(1)).findById(userId);
            verify(produtoRepository, never()).findById(any());
            verify(pedidoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando produto não existir")
        void deveLancarExcecaoQuandoProdutoNaoExiste() {
            Long produtoId = 30L;
            Long userId = 15L;
            ItemDoPedidoRecord itemFake = ItemDoPedidoTemplate.criarItemRecordComQuantidade(5);

            when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
            when(produtoRepository.findById(produtoId)).thenReturn(Optional.empty());

            ProdutoNotFound exceptionProduto = Assertions.assertThrows(
                    ProdutoNotFound.class,
                    () -> pedidoService.criar(userId, produtoId, itemFake)
            );

            String mensagemEsperada = "Erro produto não existe";
            Assertions.assertEquals(mensagemEsperada, exceptionProduto.getMessage());

            verify(userRepository, times(1)).findById(userId);
            verify(produtoRepository, times(1)).findById(produtoId);
            verify(pedidoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção ao pagar pedido inexistente")
        void deveLancarExcecaoQuandoPedidoNaoExisteAoPagar() {
            Long pedidoId = 50L;

            when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

            Assertions.assertThrows(
                    PedidoNotFound.class,
                    () -> pedidoService.pagarPedido(pedidoId)
            );

            verify(pedidoRepository, times(1)).findById(pedidoId);
            verify(pedidoRepository, never()).save(any());
            verify(pedidoMapper, never()).paraOut(any());
        }

        @Test
        @DisplayName("Deve lançar exceção ao cancelar pedido inexistente")
        void deveLancarExcecaoQuandoPedidoNaoExisteAoCancelar() {
            Long pedidoId = 60L;

            when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

            Assertions.assertThrows(
                    PedidoNotFound.class,
                    () -> pedidoService.cancelarPedido(pedidoId)
            );

            verify(pedidoRepository, times(1)).findById(pedidoId);
            verify(pedidoRepository, never()).save(any());
            verify(pedidoMapper, never()).paraOut(any());
        }
        @Test
        @DisplayName("Deve lançar exceção quando o usuário não existir")
        void listarPedidoPorUsuarioUsuarioNaoExiste() {
            Long userId = 20L;

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            UsuarioNotFound exception = Assertions.assertThrows(
                    UsuarioNotFound.class,
                    () -> pedidoService.listarPedidoPorUsuario(userId)
            );

            Assertions.assertEquals("Usuario não existe", exception.getMessage());
            verify(userRepository, times(1)).findById(userId);
            verify(pedidoRepository, never()).findByUserId(any());
            verify(pedidoMapper, never()).paraOut(any());
        }
    }
}