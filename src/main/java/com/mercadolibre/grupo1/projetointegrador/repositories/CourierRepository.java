package com.mercadolibre.grupo1.projetointegrador.repositories;

import com.mercadolibre.grupo1.projetointegrador.entities.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Rogério Lambert
 * Interface responsavavel pela configuração dos métodos de gestão do banco de dados de entregadores,
 * sendo parte do requisito 6
 */
@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
    Optional<Courier> findById(Long courierId);
}
