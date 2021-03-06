package com.mercadolibre.grupo1.projetointegrador.dtos;

import com.mercadolibre.grupo1.projetointegrador.entities.BatchStock;
import com.mercadolibre.grupo1.projetointegrador.entities.enums.ProductCategory;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Nayara Coca
 * Classe responsável por filtra dados do estoque no armazém
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchStockDTO {
    private Long batchNumber;
    @NotNull(message = "O ID do produto não pode ser nulo")
    private Long productId;
    @NotNull(message = "A temperatura atual não pode estar vazia")
    private Float currentTemperature;
    @NotNull(message = "A temperatura mínima não pode estar vazia")
    private Float minimumTemperature;
    @NotNull(message = "A quantidade inicial não pode estar vazia")
    private Integer initialQuantity;
    @NotNull(message = "A quantidade atual não pode estar vazia")
    private Integer currentQuantity;
    @NotNull(message = "A data de fabricação não pode estar vazia")
    private LocalDateTime manufacturingDateTime;
    @NotNull(message = "A data de validade não pode estar vazia")
    private LocalDate dueDate;

    public static BatchStockDTO fromBatchItem(BatchStock batchItem) {
        return new BatchStockDTO(
                batchItem.getId(),
                batchItem.getProduct().getId(),
                batchItem.getCurrentTemperature(),
                batchItem.getMinimumTemperature(),
                batchItem.getInitialQuantity(),
                batchItem.getCurrentQuantity(),
                batchItem.getManufacturingDateTime(),
                batchItem.getDueDate()
        );
    }

    public static SimpleBatchStockDTO toSimpleBatchDTO(BatchStock batchItem) {
        return new SimpleBatchStockDTO(
                batchItem.getId(),
                batchItem.getProduct().getId(),
                batchItem.getProduct().getCategory(),
                batchItem.getCurrentQuantity(),
                batchItem.getDueDate()
        );
    }

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimpleBatchStockDTO{
        private Long batchNumber;
        private Long productId;
        private ProductCategory category;
        private Integer quantity;
        private LocalDate dueDate;
    }
}
