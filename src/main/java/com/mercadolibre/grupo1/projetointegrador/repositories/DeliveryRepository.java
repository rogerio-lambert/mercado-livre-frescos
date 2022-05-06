package com.mercadolibre.grupo1.projetointegrador.repositories;

import com.mercadolibre.grupo1.projetointegrador.entities.Courier;
import com.mercadolibre.grupo1.projetointegrador.entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Rogério Lambert
 * Interface responsavavel pela configuração dos métodos de gestão do banco de dados de entregas,
 * sendo parte do requisito 6
 */

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    //Busca uma entrega pelo ID
    Optional<Delivery> findById(Long id);

    //Lista todas as entregas de um entregador no banco
    List<Delivery> findAllByCourier(Courier courier);

    //Lista todas as entregas associadas a um determinado cliente
    @Query(value = "SELECT d FROM Delivery d " +
            "INNER JOIN  PurchaseOrder p ON d.purchaseOrder.id = p.id " +
            "INNER JOIN Customer c ON p.customer.id = c.id " +
            "WHERE c.id = :customerId"
    )
    List<Delivery> findAllByCustomer(Long customerId);

}
