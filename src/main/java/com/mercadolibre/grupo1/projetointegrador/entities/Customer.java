package com.mercadolibre.grupo1.projetointegrador.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
public class Customer extends AuthenticableUser {
    private String cpf;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<PurchaseOrder> orders = new ArrayList<>();
}
