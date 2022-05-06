package com.mercadolibre.grupo1.projetointegrador.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * classe responsavel por registrar os informacoes de um cliente (comprador)
 *
 * @Author: Rogerio Lambert
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
public class Customer extends AuthenticableUser {
    private String cpf;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<PurchaseOrder> orders = new ArrayList<>();

    public Customer (AuthenticableUser user, String cpf) {
        super.setId(user.getId());
        super.setEmail(user.getEmail());
        super.setUsername(user.getUsername());
        super.setPassword(user.getUsername());
        super.setRoles(user.getRoles());
        this.cpf = cpf;
    }
}
