package com.mercadolibre.grupo1.projetointegrador.services;

import com.mercadolibre.grupo1.projetointegrador.dtos.FindProductResponseDTO;
import com.mercadolibre.grupo1.projetointegrador.entities.Agent;
import com.mercadolibre.grupo1.projetointegrador.entities.Warehouse;
import com.mercadolibre.grupo1.projetointegrador.entities.enums.SortingType;
import com.mercadolibre.grupo1.projetointegrador.exceptions.EntityNotFoundException;
import com.mercadolibre.grupo1.projetointegrador.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindProductsService {
    private final SellerRepository sellerRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final BatchStockRepository batchStockRepository;
    private final SectionRepository sectionRepositor;
    private final AgentService agentService;
    private final WarehouseService warehouseService;

//    public boolean validateFindProduct()
    public void validateAgent(Agent agent) {
        String errorMessage = "O representante com ID " + agent.getId() + " não está cadastrado em nenhuma warehouse";
        try {
            Warehouse warehouse = warehouseService.findById(agent.getWarehouse().getId());
        }
        catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(errorMessage);
        }
    }

    public FindProductResponseDTO findProducts(Long productId, SortingType sortingType, Long agentId) {
        /**
         * verificar se o agente esta persistido
         * verificar se ele esta associado a alguma warehouse
         * buscar lotes do produto filtrados pela warehouse do agente
         * aplicar ordenação se esta for presente
         * gerar o DTO
         */
        Agent agent = agentService.findById(agentId);
        validateAgent(agent);


        return new FindProductResponseDTO();
    }
}
