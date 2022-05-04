package com.mercadolibre.grupo1.projetointegrador.controller;

import com.mercadolibre.grupo1.projetointegrador.dtos.DeliveryDTO;
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
    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDTO> getById(@PathVariable Long deliveryId) {
        return ResponseEntity.ok().body(new DeliveryDTO());
    }

    @GetMapping("/userlist")
    public ResponseEntity<List<DeliveryDTO>> getUserDeliveries() {
        return ResponseEntity.ok().body(Arrays.asList(new DeliveryDTO()));
    }

    @GetMapping("/courierlist")
    public ResponseEntity<List<DeliveryDTO>> getCourierDeliveries() {
        return ResponseEntity.ok().body(Arrays.asList(new DeliveryDTO()));
    }

    @PostMapping("/")
    public ResponseEntity<DeliveryDTO> createDelivery(@Valid @RequestBody DeliveryDTO deliveryDTO, UriComponentsBuilder uriBuilder){
        URI uri = uriBuilder
                .path("/{deliveryId}")
                .buildAndExpand(deliveryDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(new DeliveryDTO());
    }

    @PutMapping("/")
    public ResponseEntity<DeliveryDTO> updateDelivery(@Valid @RequestBody DeliveryDTO deliveryDTO, UriComponentsBuilder uriBuilder){
        URI uri = uriBuilder
                .path("/{deliveryId}")
                .buildAndExpand(deliveryDTO.getId())
                .toUri();
        return ResponseEntity.created().body(new DeliveryDTO());
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDTO> deleteById(@PathVariable Long deliveryId) {
        return ResponseEntity.ok().body(new DeliveryDTO());
    }
}
