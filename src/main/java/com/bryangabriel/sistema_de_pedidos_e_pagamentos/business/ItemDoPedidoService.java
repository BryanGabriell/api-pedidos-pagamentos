package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business;


import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.ItemDoPedidoRecordOut;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.mapper.IItemDoPedidoMapper;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.ItemDoPedido;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception.ItemProdutoNotFound;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception.PedidoNotFound;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception.ProdutoNotFound;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories.ItemDoPedidoRepository;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories.PedidoRepository;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ItemDoPedidoService {

    private final ItemDoPedidoRepository itemDoPedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemDoPedidoValidator itemDoPedidoValidator;
    private final PedidoRepository pedidoRepository;
    private final CalculadoraDePedido calculadoraDePedido;
    private final IItemDoPedidoMapper iItemDoPedidoMapper;

    public ItemDoPedidoService(ItemDoPedidoRepository itemDoPedidoRepository, ProdutoRepository produtoRepository, ItemDoPedidoValidator itemDoPedidoValidator, PedidoRepository pedidoRepository, CalculadoraDePedido calculadoraDePedido, IItemDoPedidoMapper iItemDoPedidoMapper) {
        this.itemDoPedidoRepository = itemDoPedidoRepository;
        this.produtoRepository = produtoRepository;
        this.itemDoPedidoValidator = itemDoPedidoValidator;
        this.pedidoRepository = pedidoRepository;
        this.calculadoraDePedido = calculadoraDePedido;
        this.iItemDoPedidoMapper = iItemDoPedidoMapper;
    }

    @Transactional
    public ItemDoPedidoRecordOut adicionarItem(Long produtoId, Long pedidoId, Integer quantidade) {
        ItemDoPedido itemRetorno = null;
        var pedido = pedidoRepository.findById(pedidoId).orElseThrow(() ->
                new PedidoNotFound("Pedido Com o " + pedidoId + "Não Existe"));
        var produto = produtoRepository.findById(produtoId).orElseThrow(() ->
                new ProdutoNotFound("Esse Produto Com o " + produtoId + " Não existe"));
        itemDoPedidoValidator.validaStatusDoPedido(pedido.getStatusPedido());
        itemDoPedidoValidator.validaQuantidadeValida(quantidade);
        if (pedido.getItens() == null) {
            pedido.setItens(new ArrayList<>());
        }
        ItemDoPedido itemExistente = pedido.getItens()
                .stream()
                .filter(item -> item.getProduto().getId().equals(produtoId))
                .findFirst()
                .orElse(null);

        if (itemExistente != null) {
            itemExistente.setQuantidade(itemExistente.getQuantidade() + quantidade);
            itemRetorno = itemExistente;
        } else {
            ItemDoPedido novoItem = new ItemDoPedido();
            novoItem.setPedido(pedido);
            novoItem.setProduto(produto);
            novoItem.setQuantidade(quantidade);
            novoItem.setPrecoUnitario(produto.getPreco());

            pedido.getItens().add(novoItem);
            itemRetorno = novoItem;
        }
        pedido.setValorTotal(calculadoraDePedido.calcularTotal(pedido.getItens()));

    pedidoRepository.save(pedido);


        return  iItemDoPedidoMapper.paraOut(itemRetorno);
    }
    @Transactional
    public ItemDoPedidoRecordOut atualizarQuantidadeItem(Long pedidoId, Long itemId, Integer novaQuantidade){
        var pedido = pedidoRepository.findById(pedidoId).orElseThrow(() ->
                new PedidoNotFound("Erro Pedido Com o " + pedidoId + "Não Encontrado"));

        itemDoPedidoValidator.validaStatusDoPedido(pedido.getStatusPedido());
        itemDoPedidoValidator.validaQuantidadeValida(novaQuantidade);

        ItemDoPedido itemDoPedido = pedido.getItens()
                .stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ItemProdutoNotFound("Erro item não encontrado"));

        itemDoPedido.setQuantidade(novaQuantidade);
        pedido.setValorTotal(calculadoraDePedido.calcularTotal(pedido.getItens()));
            pedidoRepository.save(pedido);

        return iItemDoPedidoMapper.paraOut(itemDoPedido);
    }

    @Transactional
    public void removerItem(Long pedidoId,Long itemId){
        var pedido = pedidoRepository.findById(pedidoId).orElseThrow(() ->
                new PedidoNotFound("Erro Pedido Com o " + pedidoId + "Não Encontrado"));
        itemDoPedidoValidator.validaStatusDoPedido(pedido.getStatusPedido());

        ItemDoPedido item = pedido.getItens()
                .stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() ->
                        new ItemProdutoNotFound("Esse item não existe"));
            pedido.getItens().remove(item);

        pedido.setValorTotal(calculadoraDePedido.calcularTotal(pedido.getItens()));
        pedidoRepository.save(pedido);
    }
}