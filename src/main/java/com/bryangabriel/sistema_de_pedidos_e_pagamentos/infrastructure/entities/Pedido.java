package com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.entities;

import com.bryangabriel.sistema_de_pedidos_e_pagamentos.infrastructure.enums.StatusDoPedido;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "pedidos")
@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pedido", nullable = false)
    private StatusDoPedido statusPedido;
    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    @CreatedDate
    @Column(name = "criadoEm", nullable = false)
    private LocalDate criadoEm;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ItemDoPedido> itens = new ArrayList<>();

    @PrePersist
    private void prePersist(){
        statusPedido = StatusDoPedido.CRIADO;
    }
}
