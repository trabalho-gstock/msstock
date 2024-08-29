package com.gstock.msstock.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sku;
    private Integer quantity;

    public Stock(Long id, String sku, Integer quantity){
        this.id = id;
        this.sku = sku;
        this.quantity = quantity;
    }
}
