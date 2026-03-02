package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business;


import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.ProdutoAtualizaRecord;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.ProdutoRecord;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.ProdutoRecordOut;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.mapper.ProdutoMapper;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.Produto;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.exception.ProdutoNotFound;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;
    public ProdutoService(ProdutoRepository produtoRepository, ProdutoMapper produtoMapper) {
        this.produtoRepository = produtoRepository;
        this.produtoMapper = produtoMapper;
    }

    @Transactional
    public ProdutoRecordOut criarProduto(ProdutoRecord produtoRecord){
        var produto = produtoMapper.paraEntity(produtoRecord);
       var produtoCriado = produtoRepository.save(produto);
        return produtoMapper.paraOut(produtoCriado);
    }

    public ProdutoRecordOut buscarProdutoPorId(Long produtoId){
       return produtoRepository.findById(produtoId).map(produtoMapper::paraOut).orElseThrow(() ->
                new ProdutoNotFound("Erro produto não encotrado"));
    }
    @Transactional
    public ProdutoRecordOut atualizarProduto(Long produtoId, ProdutoAtualizaRecord produtoRecord){
       Produto produto =produtoRepository.findById(produtoId).orElseThrow(() -> new ProdutoNotFound("Erro produto não encotrado"));
       if(produtoRecord.nome() != null){
           produto.setNome(produtoRecord.nome());
       }
       if(produtoRecord.preco() != null){
           if( produtoRecord.preco().compareTo(BigDecimal.ZERO) < 0){
          throw  new RuntimeException("Preço invalido"); }
           produto.setPreco(produtoRecord.preco());
       }
       if (produtoRecord.estoque() != null){
           if(produtoRecord.estoque() < 0){
               throw new RuntimeException("Estoque Invalido");
           }
           produto.setEstoque(produtoRecord.estoque());
       }
       return produtoMapper.paraOut(produto);
    }

    @Transactional
    public void deletarProduto(Long produtoId){
        if(!produtoRepository.existsById(produtoId)){
            throw new ProdutoNotFound("Produto não encontrado");
        }
        produtoRepository.deleteById(produtoId);
    }
}
