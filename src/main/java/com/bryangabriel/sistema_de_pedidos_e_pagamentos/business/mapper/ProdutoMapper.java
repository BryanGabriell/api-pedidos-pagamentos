package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.mapper;


import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.ProdutoRecord;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.ProdutoRecordOut;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.Produto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    Produto paraEntity(ProdutoRecord produtoRecord);

    ProdutoRecordOut paraOut(Produto produto);
}
