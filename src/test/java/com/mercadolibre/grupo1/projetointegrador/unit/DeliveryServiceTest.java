package com.mercadolibre.grupo1.projetointegrador.unit;

import com.mercadolibre.grupo1.projetointegrador.dtos.DeliveryDTO;
import com.mercadolibre.grupo1.projetointegrador.entities.*;
import com.mercadolibre.grupo1.projetointegrador.exceptions.EntityNotFoundException;
import com.mercadolibre.grupo1.projetointegrador.exceptions.ForbiddenException;
import com.mercadolibre.grupo1.projetointegrador.repositories.CourierRepository;
import com.mercadolibre.grupo1.projetointegrador.repositories.DeliveryRepository;
import com.mercadolibre.grupo1.projetointegrador.services.DeliveryService;
import com.mercadolibre.grupo1.projetointegrador.services.PurchaseOrderService;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * @author Rogério Lambert
 * Testes unitarios do service de gestão das entregas, realizada como requisito 6 do bootcamper Rogério Lambert
 */
@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;
    @Mock
    private CourierRepository courierRepository;
    @Mock
    private PurchaseOrderService purchaseOrderService;

    @InjectMocks
    private DeliveryService deliveryService;

    @BeforeEach
    private void LoaldMocks() {

    }

    @Test
    @DisplayName("Testa função que retorna uma entrega de um cliente quando acionada por um cliente ou agente")
    public void itShouldReturnDeliveryDtoOfCustomer() {
        //Setup do teste
        AuthenticableUser user = AuthenticableUser.builder().id(1L).roles(new HashSet<>()).build();
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .customer(new Customer(user,null))
                .id(1L)
                .build();
        Courier courier = new Courier(AuthenticableUser.builder().id(2L).build(), null);
        LocalDateTime schedule = LocalDateTime.of(2022, 5, 6,14,30);
        Delivery delivery = Delivery.builder()
                .id(1L)
                .deliveryAddress("Rua Direita 1")
                .courier(courier)
                .deliverySchedule(schedule)
                .purchaseOrder(purchaseOrder)
                .build();
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));

        //execução
        DeliveryDTO deliveryDTO = deliveryService.getCustomerDelivery(1L, user);

        //testes

        assertEquals(deliveryDTO.getId(), 1L);
        assertEquals(deliveryDTO.getCourierId(), 2L);
        assertEquals(deliveryDTO.getDeliveryAddress(), "Rua Direita 1");
        assertEquals(deliveryDTO.getPurchaseOrderId(), 1L);
        assertEquals(deliveryDTO.getDeliverySchedule(), schedule);
    }

    @Test
    @DisplayName("Testa se exceção correta é lançada quando a entrega não é encontrata")
    public void itShouldThrowsExceptionWhenDeliveryIsNotFound() {
        //Setup do teste
        AuthenticableUser user = AuthenticableUser.builder().id(1L).roles(new HashSet<>()).build();
        when(deliveryRepository.findById(1L)).thenReturn(Optional.empty());

        //execução
        Exception exception= assertThrows(EntityNotFoundException.class, () -> deliveryService.getCustomerDelivery(1L, user));

        //testes

        assertEquals(exception.getMessage(), "A entrega com ID 1 não está cadastrada");
    }

    @Test
    @DisplayName("Testa se exceção correta é lançada quando quando a entrega não pertence ao cliente")
    public void itShouldThrowsExceptionWhenDeliveryIsNotBelongsToCustomer() {
        //Setup do teste
        AuthenticableUser user = AuthenticableUser.builder().id(1L).roles(new HashSet<>()).build();
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .customer(new Customer(user,null))
                .id(1L)
                .build();
        user.setId(3L);
        LocalDateTime schedule = LocalDateTime.of(2022, 5, 6,14,30);
        Delivery delivery = Delivery.builder()
                .purchaseOrder(purchaseOrder)
                .build();
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));

        //execução
        Exception exception= assertThrows(ForbiddenException.class, () -> deliveryService.getCustomerDelivery(1L, user));

        //testes

        assertEquals(exception.getMessage(), "Esta entrega não pertence à este usuário");
    }

    @Test
    @DisplayName("Testa função que retorna uma entrega de um entregador")
    public void itShouldReturnDeliveryDtoOfCourier() {
        //Setup do teste
        PurchaseOrder purchaseOrder = PurchaseOrder.builder().id(1L).build();
        Courier courier = new Courier(AuthenticableUser.builder().id(2L).build(), null);
        LocalDateTime schedule = LocalDateTime.of(2022, 5, 6,14,30);
        Delivery delivery = Delivery.builder()
                .id(1L)
                .deliveryAddress("Rua Direita 1")
                .courier(courier)
                .deliverySchedule(schedule)
                .purchaseOrder(purchaseOrder)
                .build();
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));

        //execução
        DeliveryDTO deliveryDTO = deliveryService.getCourierDelivery(1L, courier);

        //testes

        assertEquals(deliveryDTO.getId(), 1L);
        assertEquals(deliveryDTO.getCourierId(), 2L);
        assertEquals(deliveryDTO.getDeliveryAddress(), "Rua Direita 1");
        assertEquals(deliveryDTO.getPurchaseOrderId(), 1L);
        assertEquals(deliveryDTO.getDeliverySchedule(), schedule);
    }

    @Test
    @DisplayName("Testa se exceção correta é lançada quando quando a entrega não pertence ao entrador")
    public void itShouldThrowsExceptionWhenDeliveryIsNotBelongsToCourier() {
        //Setup do teste
        PurchaseOrder purchaseOrder = PurchaseOrder.builder().id(1L).build();
        Courier courier = new Courier(AuthenticableUser.builder().id(2L).build(), null);
        LocalDateTime schedule = LocalDateTime.of(2022, 5, 6,14,30);
        Delivery delivery = Delivery.builder()
                .id(1L)
                .deliveryAddress("Rua Direita 1")
                .courier(courier)
                .deliverySchedule(schedule)
                .purchaseOrder(purchaseOrder)
                .build();
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        Courier courier2 = new Courier(AuthenticableUser.builder().id(6L).build(), null);


        //execução
        Exception exception= assertThrows(ForbiddenException.class,
                () -> deliveryService.getCourierDelivery(1L, courier2));

        //testes

        assertEquals(exception.getMessage(), "Esta entrega não pertence à este entregador");
    }

    @Test
    @DisplayName("Testa função que retorna uma lista entregas de um cliente")
    public void itShouldReturnListOfDeliveryDtoOfCustomer() {
        //Setup do teste
        Customer customer = new Customer(AuthenticableUser.builder().id(1L).build(), null);

        List<Delivery> deliveries = Arrays.asList(
                Delivery.builder()
                        .id(1L)
                        .deliveryAddress("Rua Direita 1")
                        .courier(new Courier(AuthenticableUser.builder().id(1L).build(), null))
                        .deliverySchedule(LocalDateTime.of(2022, 5, 6,14,30))
                        .purchaseOrder(PurchaseOrder.builder().id(1L).build())
                        .build(),
                Delivery.builder()
                        .id(2L)
                        .deliveryAddress("Rua Direita 1")
                        .courier(new Courier(AuthenticableUser.builder().id(1L).build(), null))
                        .deliverySchedule(LocalDateTime.of(2022, 5, 6,14,30))
                        .purchaseOrder(PurchaseOrder.builder().id(1L).build())
                        .build()
        );
        when(deliveryRepository.findAllByCustomer(1L)).thenReturn(deliveries);

        //execução
        List<DeliveryDTO> deliveryDTOs = deliveryService.getCustomerDeliveries(customer);

        //testes
        assertEquals(deliveryDTOs.size(), 2);
        assertEquals(deliveryDTOs.get(0).getId(), 1L);
        assertEquals(deliveryDTOs.get(1).getId(), 2L);
        assertEquals(deliveryDTOs.get(0).getDeliveryAddress(), "Rua Direita 1");
    }

    @Test
    @DisplayName("Testa função que retorna uma lista entregas de um entregador")
    public void itShouldReturnListOfDeliveryDtoOfCourier() {
        //Setup do teste
        Courier courier = new Courier(AuthenticableUser.builder().id(1L).build(), null);

        List<Delivery> deliveries = Arrays.asList(
                Delivery.builder()
                        .id(1L)
                        .deliveryAddress("Rua Direita 1")
                        .courier(new Courier(AuthenticableUser.builder().id(1L).build(), null))
                        .deliverySchedule(LocalDateTime.of(2022, 5, 6,14,30))
                        .purchaseOrder(PurchaseOrder.builder().id(1L).build())
                        .build(),
                Delivery.builder()
                        .id(2L)
                        .deliveryAddress("Rua Direita 1")
                        .courier(new Courier(AuthenticableUser.builder().id(1L).build(), null))
                        .deliverySchedule(LocalDateTime.of(2022, 5, 6,14,30))
                        .purchaseOrder(PurchaseOrder.builder().id(1L).build())
                        .build()
        );
        when(deliveryRepository.findAllByCourier(courier)).thenReturn(deliveries);

        //execução
        List<DeliveryDTO> deliveryDTOs = deliveryService.getCourierDeliveries(courier);

        //testes
        assertEquals(deliveryDTOs.size(), 2);
        assertEquals(deliveryDTOs.get(0).getId(), 1L);
        assertEquals(deliveryDTOs.get(1).getId(), 2L);
        assertEquals(deliveryDTOs.get(0).getDeliveryAddress(), "Rua Direita 1");
    }

    @Test
    @DisplayName("Testa função que comanda a criação do uma entrega")
    public void itShouldCreateADelivery() {
        //Setup do teste
        AuthenticableUser user = AuthenticableUser.builder().id(1L).roles(new HashSet<>()).build();
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .customer(new Customer(user,null))
                .id(1L)
                .build();
        Courier courier = new Courier(AuthenticableUser.builder().id(2L).build(), null);
        LocalDateTime schedule = LocalDateTime.of(2022, 5, 6,14,30);
        DeliveryDTO deliveryDTO = DeliveryDTO.builder()
                .id(1L)
                .deliveryAddress("Rua Direita 1")
                .courierId(2L)
                .deliverySchedule(schedule)
                .purchaseOrderId(1L)
                .build();
        Delivery delivery = Delivery.builder()
                .id(null)
                .deliveryAddress("Rua Direita 1")
                .courier(courier)
                .deliverySchedule(schedule)
                .purchaseOrder(purchaseOrder)
                .build();
        when(deliveryRepository.save(delivery)).thenReturn(delivery);
        when(courierRepository.findById(2L)).thenReturn(Optional.of(courier));
        when(purchaseOrderService.showProductsInOrders(1L)).thenReturn(purchaseOrder);

        //execução
        DeliveryDTO returnedDTO = deliveryService.createDelivery(deliveryDTO);

        //testes
        assertEquals(deliveryDTO.getCourierId(), 2L);
        assertEquals(deliveryDTO.getDeliveryAddress(), "Rua Direita 1");
        assertEquals(deliveryDTO.getPurchaseOrderId(), 1L);
        assertEquals(deliveryDTO.getDeliverySchedule(), schedule);
    }

    @Test
    @DisplayName("Testa se exceção correta é lançada quando o entregador não é encontrado")
    public void itShouldThrowsExceptionWhenCourierWasNotFound() {
        //Setup do teste
        DeliveryDTO deliveryDTO = DeliveryDTO.builder().courierId(1L).build();
        when(courierRepository.findById(1L)).thenReturn(Optional.empty());
        //execução
        Exception exception= assertThrows(EntityNotFoundException.class,
                () -> deliveryService.createDelivery(deliveryDTO));

        //testes

        assertEquals(exception.getMessage(), "Não foi encontrado o entregador com id: 1");
    }

    @Test
    @DisplayName("Testa função que comanda a edição do uma entrega existente")
    public void itShouldUpdateADelivery() {
        //Setup do teste
        AuthenticableUser user = AuthenticableUser.builder().id(1L).roles(new HashSet<>()).build();
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .customer(new Customer(user,null))
                .id(1L)
                .build();
        Courier courier = new Courier(AuthenticableUser.builder().id(2L).build(), null);
        LocalDateTime schedule = LocalDateTime.of(2022, 5, 6,14,30);
        DeliveryDTO deliveryDTO = DeliveryDTO.builder()
                .id(1L)
                .deliveryAddress("Rua Direita 1")
                .courierId(2L)
                .deliverySchedule(schedule)
                .purchaseOrderId(1L)
                .build();
        Delivery delivery = Delivery.builder()
                .id(1L)
                .deliveryAddress("Rua Direita 1")
                .courier(courier)
                .deliverySchedule(schedule)
                .purchaseOrder(purchaseOrder)
                .build();
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(deliveryRepository.save(delivery)).thenReturn(delivery);
        when(courierRepository.findById(2L)).thenReturn(Optional.of(courier));
        when(purchaseOrderService.showProductsInOrders(1L)).thenReturn(purchaseOrder);

        //execução
        DeliveryDTO returnedDTO = deliveryService.updateDelivery(deliveryDTO, 1L);

        //testes
        assertEquals(deliveryDTO.getCourierId(), 2L);
        assertEquals(deliveryDTO.getDeliveryAddress(), "Rua Direita 1");
        assertEquals(deliveryDTO.getPurchaseOrderId(), 1L);
        assertEquals(deliveryDTO.getDeliverySchedule(), schedule);
    }

    @Test
    @DisplayName("Testa função que apaga uma entrega")
    public void itShouldDeleteADelivery() {
        //Setup do teste
        AuthenticableUser user = AuthenticableUser.builder().id(1L).roles(new HashSet<>()).build();
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .customer(new Customer(user,null))
                .id(1L)
                .build();
        Courier courier = new Courier(AuthenticableUser.builder().id(2L).build(), null);
        LocalDateTime schedule = LocalDateTime.of(2022, 5, 6,14,30);
        Delivery delivery = Delivery.builder()
                .id(1L)
                .deliveryAddress("Rua Direita 1")
                .courier(courier)
                .deliverySchedule(schedule)
                .purchaseOrder(purchaseOrder)
                .build();
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));

        //execução
        DeliveryDTO deliveryDTO = deliveryService.deleteDelivery(1L);

        //testes
        Mockito.verify(deliveryRepository, Mockito.times(1)).deleteById(1L);
        assertEquals(deliveryDTO.getId(), 1L);
        assertEquals(deliveryDTO.getCourierId(), 2L);
        assertEquals(deliveryDTO.getDeliveryAddress(), "Rua Direita 1");
        assertEquals(deliveryDTO.getPurchaseOrderId(), 1L);
        assertEquals(deliveryDTO.getDeliverySchedule(), schedule);
    }


}
