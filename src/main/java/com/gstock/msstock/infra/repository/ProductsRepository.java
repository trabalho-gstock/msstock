package com.gstock.msstock.infra.repository;

import com.gstock.msstock.domain.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "msproducts", path = "/products")
public interface ProductsRepository {

    @GetMapping(params = "sku")
    public ResponseEntity<Product> getProductsBySku(@RequestParam("sku") String sku);

    @GetMapping
    public List<Product> getAll();
}
