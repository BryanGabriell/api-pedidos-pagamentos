package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.repositories;

import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
}
