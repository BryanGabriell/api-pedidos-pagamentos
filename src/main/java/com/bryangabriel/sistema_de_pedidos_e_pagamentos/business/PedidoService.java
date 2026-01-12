package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business;


import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.mapper.IPedidoMapper;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories.PedidoRepository;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final IPedidoMapper iPedidoMapper;

    public PedidoService(PedidoRepository pedidoRepository, IPedidoMapper iPedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.iPedidoMapper = iPedidoMapper;
    }
}
