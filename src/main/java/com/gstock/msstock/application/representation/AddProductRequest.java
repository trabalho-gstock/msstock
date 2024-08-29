package com.gstock.msstock.application.representation;

import lombok.Data;

@Data
public class AddProductRequest {
    private String sku;
    private Integer quantity;
}
