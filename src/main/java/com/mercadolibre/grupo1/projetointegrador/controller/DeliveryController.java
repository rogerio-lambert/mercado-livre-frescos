package com.mercadolibre.grupo1.projetointegrador.controller;

import com.mercadolibre.grupo1.projetointegrador.dtos.DeliveryDTO;
import com.mercadolibre.grupo1.projetointegrador.entities.AuthenticableUser;
import com.mercadolibre.grupo1.projetointegrador.entities.Courier;
import com.mercadolibre.grupo1.projetointegrador.entities.Customer;
import com.mercadolibre.grupo1.projetointegrador.services.AuthService;
import com.mercadolibre.grupo1.projetointegrador.services.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * @author Rogério Lambert
 * Classe responsavavel pela configuração dos endpoints relacionados a entregas,
 * sendo parte do requisito 6
 */

@RestController
@RequestMapping("/api/v1/fresh-products/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private  final DeliveryService deliveryService;

    private final AuthService authService;

    //Endpoint para busca de uma entrega, podendo ser acessada por um cliente que seja 'dono' da entrega ou por um representante
    @GetMapping("/customer/{deliveryId}")
    public ResponseEntity<DeliveryDTO> getCustomerDelivery(@PathVariable Long deliveryId) {
        AuthenticableUser user = authService.getPrincipal();

        DeliveryDTO deliveryDTO = deliveryService.getCustomerDelivery(deliveryId, user);

        return ResponseEntity.ok().body(deliveryDTO);
    }

    //Endpoint para busca de uma entrega, podendo ser acessada por um entregador responsável pela entrega
    @GetMapping("/courier/{deliveryId}")
    public ResponseEntity<DeliveryDTO> getCourierDelivery(@PathVariable Long deliveryId) {
        Courier courier = authService.getPrincipalAs(Courier.class);

        DeliveryDTO deliveryDTO = deliveryService.getCourierDelivery(deliveryId, courier);

        return ResponseEntity.ok().body(deliveryDTO);
    }
    //Endpoint para busca de uma lista de entregas de um um cliente
    @GetMapping("/customer/list")
    public ResponseEntity<List<DeliveryDTO>> getCustomerDeliveries() {
        Customer customer = authService.getPrincipalAs(Customer.class);
        return ResponseEntity.ok().body(deliveryService.getCustomerDeliveries(customer));
    }
    //Endpoint para busca de uma lista de entregas associadas a um entregador
    @GetMapping("/courier/list")
    public ResponseEntity<List<DeliveryDTO>> getCourierDeliveries() {
        Courier courier = authService.getPrincipalAs(Courier.class);
        return ResponseEntity.ok().body(deliveryService.getCourierDeliveries(courier));
    }

    //Endpoint responsável por comandar a criação de uma nova entrega
    @PostMapping("/")
    public ResponseEntity<DeliveryDTO> createDelivery(@Valid @RequestBody DeliveryDTO deliveryDTO,
                                                      UriComponentsBuilder uriBuilder){
        DeliveryDTO deliveryDTOCreated = deliveryService.createDelivery(deliveryDTO);
        URI uri = uriBuilder
                .path("/{deliveryId}")
                .buildAndExpand(deliveryDTOCreated.getId())
                .toUri();
        return ResponseEntity.created(uri).body(deliveryDTOCreated);
    }
    //Endpoint responsável por comandar a edição de uma entrega
    @PutMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDTO> updateDelivery(@PathVariable Long deliveryId,
                                                      @Valid @RequestBody DeliveryDTO deliveryDTO,
                                                      UriComponentsBuilder uriBuilder){
        URI uri = uriBuilder
                .path("/{deliveryId}")
                .buildAndExpand(deliveryId)
                .toUri();
        return ResponseEntity.created(uri).body(deliveryService.updateDelivery(deliveryDTO, deliveryId));
    }

    //Endpoint responsável por comandar a exclusão de uma entrega
    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDTO> deleteById(@PathVariable Long deliveryId) {
        return ResponseEntity.ok().body(deliveryService.deleteDelivery(deliveryId));
    }
}