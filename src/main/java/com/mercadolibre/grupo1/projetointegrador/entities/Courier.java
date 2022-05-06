package com.mercadolibre.grupo1.projetointegrador.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * @author Rogério Lambert
 * Classe responsavavel pela configuração da entidade(e tabela no BD) de entregador,
 * sendo parte do requisito 6
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "couriers")
public class Courier extends AuthenticableUser {
    @OneToMany(mappedBy = "courier", cascade = CascadeType.ALL)
    private List<Delivery> deliveries;

    public Courier (AuthenticableUser user, List<Delivery> deliveries) {
        super.setId(user.getId());
        super.setEmail(user.getEmail());
        super.setUsername(user.getUsername());
        super.setPassword(user.getUsername());
        super.setRoles(user.getRoles());
        this.deliveries = deliveries;
    }
}