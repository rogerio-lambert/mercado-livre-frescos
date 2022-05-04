package com.mercadolibre.grupo1.projetointegrador.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("couriers")
public class Courier extends AuthenticableUser {
    @OneToMany(mappedBy = "Delivery", cascade = CascadeType.ALL)
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