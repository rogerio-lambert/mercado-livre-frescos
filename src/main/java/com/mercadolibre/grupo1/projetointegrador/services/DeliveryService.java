package com.mercadolibre.grupo1.projetointegrador.services;

import com.mercadolibre.grupo1.projetointegrador.dtos.DeliveryDTO;
import com.mercadolibre.grupo1.projetointegrador.entities.*;
import com.mercadolibre.grupo1.projetointegrador.exceptions.EntityNotFoundException;
import com.mercadolibre.grupo1.projetointegrador.exceptions.ForbiddenException;
import com.mercadolibre.grupo1.projetointegrador.exceptions.NotFoundException;
import com.mercadolibre.grupo1.projetointegrador.repositories.CourierRepository;
import com.mercadolibre.grupo1.projetointegrador.repositories.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final CourierRepository courierRepository;
    private final PurchaseOrderService purchaseOrderService;

    private Delivery getDelivery(Long deliveryId) {
        String errorMessage = "A entrega com ID " + deliveryId + " não está cadastrada";
        return deliveryRepository
                .findById(deliveryId)
                .orElseThrow(() ->
                        new EntityNotFoundException(errorMessage));
    }

    public DeliveryDTO getCustomerDelivery(Long deliveryId, AuthenticableUser user) {

        Delivery delivery = getDelivery(deliveryId);

        // Verifica se usuário tem acesso à entrega específica
        if (!user.getRoles().contains(new Role(1L, "ROLE_AGENT"))
                && !user.getId().equals(delivery.getPurchaseOrder().getCustomer().getId())) {
            throw new ForbiddenException("Esta entrega não pertence à este usuário");
        }

        return new DeliveryDTO(delivery);
    }

    public DeliveryDTO getCourierDelivery(Long deliveryId, Courier courier) {

        Delivery delivery = getDelivery(deliveryId);

        if (!courier.getId().equals(delivery.getCourier().getId())) {
            throw new ForbiddenException("Esta entrega não pertence à este entregador");
        }

        return new DeliveryDTO(delivery);
    }

    public List<DeliveryDTO> getCourierDeliveries(Courier courier) {
        List<Delivery> deliveries = deliveryRepository.findAllByCourier(courier);
        if(deliveries.isEmpty()) {
            throw new NotFoundException("Não foram encontradas entregas atribuídas ao entregador com id: " + courier.getId());
        }
        return DeliveryDTO.generateList(deliveries);
    }

    public List<DeliveryDTO> getCustomerDeliveries(Customer customer) {
        List<Delivery> deliveries = deliveryRepository.findAllByCustomer(customer.getId());
        if(deliveries.isEmpty()) {
            throw new NotFoundException("Não foram encontradas entregas atribuídas ao cliente com id: " + customer.getId());
        }
        return DeliveryDTO.generateList(deliveries);
    }

    private Courier getCourier(Long courierId) {
        String message = "Não foi encontrado o entregador com id: " + courierId;
        return courierRepository.findById(courierId)
            .orElseThrow(() -> new EntityNotFoundException(message));
    }

    private Delivery persistDelivery(Long deliveryId, DeliveryDTO deliveryDTO) {
        Courier courier = getCourier(deliveryDTO.getCourierId());
        PurchaseOrder purchaseOrder = purchaseOrderService.showProductsInOrders(deliveryDTO.getPurchaseOrderId());
        return deliveryRepository.save(Delivery.builder()
                .id(deliveryId)
                .courier(courier)
                .purchaseOrder(purchaseOrder)
                .deliveryAddress(deliveryDTO.getDeliveryAddress())
                .deliverySchedule(deliveryDTO.getDeliverySchedule())
                .build());
    }

    public DeliveryDTO createDelivery(DeliveryDTO deliveryDTO) {
        Delivery delivery = persistDelivery(null, deliveryDTO);
        return new DeliveryDTO(delivery);
    }

    public DeliveryDTO updateDelivery(DeliveryDTO deliveryDTO, Long deliveryId) {
        getDelivery(deliveryId);
        Delivery delivery = persistDelivery(deliveryId, deliveryDTO);
        return new DeliveryDTO(delivery);
    }

    public DeliveryDTO deleteDelivery(Long deliveryId) {
        Delivery delivery = getDelivery(deliveryId);
        deliveryRepository.deleteById(deliveryId);
        return new DeliveryDTO(delivery);
    }
}
