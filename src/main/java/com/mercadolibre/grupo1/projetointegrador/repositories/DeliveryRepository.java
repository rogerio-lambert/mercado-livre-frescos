package com.mercadolibre.grupo1.projetointegrador.repositories;

import com.mercadolibre.grupo1.projetointegrador.entities.Courier;
import com.mercadolibre.grupo1.projetointegrador.entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findById(Long id);

    List<Delivery> findAllByCourier(Courier courier);

    @Query(value = "SELECT d FROM Delivery d " +
            "INNER JOIN  PurchaseOrder p ON d.purchaseOrder.id = p.id " +
            "INNER JOIN Customer c ON p.customer.id = c.id " +
            "WHERE c.id = :customerId"
    )
    List<Delivery> findAllByCustomer(Long customerId);

}
