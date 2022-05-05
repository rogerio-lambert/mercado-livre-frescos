package com.mercadolibre.grupo1.projetointegrador.dtos;

import com.mercadolibre.grupo1.projetointegrador.entities.Delivery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {
    private Long id;
    @NotNull(message = "O horário de agendamento deve ser preenchido")
    private LocalDateTime deliverySchedule;
    @NotNull(message = "O id 'purchaseOrder' do compra deve ser informado")
    private Long purchaseOrderId;
    @NotNull(message = "O endereço de entrega deve ser informado")
    private String deliveryAddress;
    @NotNull(message = "O id do entregador deve ser informado")
    private Long courierId;

    public DeliveryDTO(Delivery delivery) {
        id = delivery.getId();
        deliverySchedule = delivery.getDeliverySchedule();
        purchaseOrderId = delivery.getPurchaseOrder().getId();
        deliveryAddress = delivery.getDeliveryAddress();
        courierId = delivery.getCourier().getId();
    }

    public static List<DeliveryDTO> generateList (List<Delivery> deliveries) {
        List<DeliveryDTO> deliveryDTOList = new ArrayList<>();
        for (Delivery delivery : deliveries) {
            deliveryDTOList.add(new DeliveryDTO(delivery));
        }
        return deliveryDTOList;
    }
}
