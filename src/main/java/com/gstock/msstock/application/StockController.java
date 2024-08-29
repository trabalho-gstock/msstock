package com.gstock.msstock.application;

import com.gstock.msstock.application.exception.AddProductException;
import com.gstock.msstock.application.exception.MicroserviceCommunicationError;
import com.gstock.msstock.application.representation.AddProductRequest;
import com.gstock.msstock.domain.Report;
import com.gstock.msstock.domain.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping
    public ResponseEntity addProduct(@RequestBody AddProductRequest request) {
        try {
            Stock stock = stockService.addProductToStock(request.getSku(), request.getQuantity());
            return ResponseEntity.ok().build();
        } catch (AddProductException e) {
            return ResponseEntity.notFound().build();
        } catch (MicroserviceCommunicationError e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Stock>> getAllStock(){
        return ResponseEntity.ok(stockService.getAll());
    }

    @GetMapping(params = "sku")
    public ResponseEntity<Optional<Stock>> getProductBySku(@RequestParam("sku") String sku){
        return ResponseEntity.ok(stockService.getBySku(sku));
    }

    @GetMapping("generate-report")
    public ResponseEntity<Report> getGeneratedReport() throws MicroserviceCommunicationError{
        return ResponseEntity.ok(stockService.generateReports());
    }
}