package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business;


import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.ItemDoPedidoRecord;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.PedidoRecordOut;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.mapper.IPedidoMapper;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.ItemDoPedido;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.Pedido;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.Produto;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.User;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.enums.StatusDoPedido;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception.*;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories.PedidoRepository;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories.ProdutoRepository;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final IPedidoMapper pedidoMapper;
    private final CalculadoraDePedido calculadoraDePedido;
    private final UserRepository userRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository, IPedidoMapper pedidoMapper, CalculadoraDePedido calculadoraDePedido, UserRepository userRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;

        this.calculadoraDePedido = calculadoraDePedido;
        this.userRepository = userRepository;
        this.produtoRepository = produtoRepository;
    }
    @Transactional
    public PedidoRecordOut criar(Long userId, Long produtoId, ItemDoPedidoRecord itemDoPedidoRecord){
        User usuarioPedido = userRepository.findById(userId).orElseThrow(() ->
                new UsuarioNotFound("Erro ao criar o pedido o " + userId + "não existe"));

        Pedido pedido = new Pedido();
        pedido.setUser(usuarioPedido);

        Produto produto = produtoRepository.findById(produtoId).orElseThrow(() ->
                new ProdutoNotFound("Erro produto não existe"));

      ItemDoPedido itemDoPedido = new ItemDoPedido();
      itemDoPedido.setPedido(pedido);
      itemDoPedido.setProduto(produto);
      itemDoPedido.setQuantidade(itemDoPedidoRecord.quantidade());
      itemDoPedido.setPrecoUnitario(produto.getPreco());

    if(pedido.getItens() == null){
        pedido.setItens(new ArrayList<>());
    }
      pedido.getItens().add(itemDoPedido);

    pedido.setValorTotal( calculadoraDePedido.calcularTotal(pedido.getItens()));
      pedidoRepository.save(pedido);

      return pedidoMapper.paraOut(pedido);
    }
    @Transactional
    public PedidoRecordOut pagarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() ->
                        new PedidoNotFound("Erro pedido com o " + pedidoId + " não encontrado"));

        switch (pedido.getStatusPedido()) {
            case CANCELADO -> throw new StatusInvalido("Erro. Você não pode pagar um pedido que está CANCELADO");
            case PAGO -> throw new StatusInvalido("Erro. O pedido já está pago");
            case CRIADO -> {

            }
        }
        pedido.getItens().forEach(item -> {
            Produto produto = item.getProduto();
            if (produto.getEstoque() < item.getQuantidade()) {
                throw new EstoqueInsuficiente("Estoque insuficiente para o produto: " + produto.getNome());
            }
        });

        pedido.getItens().forEach(item -> {
            Produto produto = item.getProduto();
            produto.setEstoque(produto.getEstoque() - item.getQuantidade());
        });

        pedido.setStatusPedido(StatusDoPedido.PAGO);
        pedidoRepository.save(pedido);

        return pedidoMapper.paraOut(pedido);
    }
    @Transactional
    public PedidoRecordOut cancelarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new PedidoNotFound("Pedido não encontrado"));

        switch (pedido.getStatusPedido()) {
            case PAGO -> throw new PedidoError("Erro: você não pode cancelar um pedido já pago");
            case CANCELADO -> throw new PedidoError("Erro: o pedido já está cancelado");
            case CRIADO -> {
                pedido.setStatusPedido(StatusDoPedido.CANCELADO);
                pedidoRepository.save(pedido);
            }
        }

        return pedidoMapper.paraOut(pedido);
    }

   public List<PedidoRecordOut> listarPedidoPorUsuario(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UsuarioNotFound("Usuario não existe"));

       List<Pedido> pedidos = pedidoRepository.findByUserId(userId);

       return pedidos.stream()
               .map(pedidoMapper::paraOut).
               toList();

    }
}



