package com.mercadolibre.grupo1.projetointegrador.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Rogério Lambert
 * classe responsavavel pela configuração da entidade(e tabela no BD) de entrega,
 * sendo parte do requisito 6
 */

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime deliverySchedule;
    @OneToOne
    private PurchaseOrder purchaseOrder;
    private String deliveryAddress;
    @ManyToOne
    @JoinColumn(name = "courier_id", referencedColumnName = "id")
    private Courier courier;

}
