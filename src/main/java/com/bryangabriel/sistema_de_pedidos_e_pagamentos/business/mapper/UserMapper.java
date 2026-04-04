package com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.mapper;


import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.in.UserRecord;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.business.controller.dto.out.UserRecordOut;
import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User paraEntity(UserRecord userRecord);

    UserRecordOut paraOut(User user);

}
