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
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private  final DeliveryService deliveryService;

    private final AuthService authService;

    @GetMapping("/customer/{deliveryId}")
    public ResponseEntity<DeliveryDTO> getCustomerDelivery(@PathVariable Long deliveryId) {
        AuthenticableUser user = authService.getPrincipal();

        DeliveryDTO deliveryDTO = deliveryService.getCustomerDelivery(deliveryId, user);

        return ResponseEntity.ok().body(deliveryDTO);
    }

    @GetMapping("/courier/{deliveryId}")
    public ResponseEntity<DeliveryDTO> getCourierDelivery(@PathVariable Long deliveryId) {
        Courier courier = authService.getPrincipalAs(Courier.class);

        DeliveryDTO deliveryDTO = deliveryService.getCourierDelivery(deliveryId, courier);

        return ResponseEntity.ok().body(deliveryDTO);
    }

    @GetMapping("/customer/list")
    public ResponseEntity<List<DeliveryDTO>> getUserDeliveries() {
        Customer customer = authService.getPrincipalAs(Customer.class);
        return ResponseEntity.ok().body(deliveryService.getCustomerDeliveries(customer));
    }

    @GetMapping("/courier/list")
    public ResponseEntity<List<DeliveryDTO>> getCourierDeliveries() {
        Courier courier = authService.getPrincipalAs(Courier.class);
        return ResponseEntity.ok().body(deliveryService.getCourierDeliveries(courier));
    }

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

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDTO> deleteById(@PathVariable Long deliveryId) {
        return ResponseEntity.ok().body(deliveryService.deleteDelivery(deliveryId));
    }
}
