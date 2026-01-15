package com.govind.order_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateOrderRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String productId;

    @Min(1)
    private int quantity;

}
