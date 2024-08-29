package com.gstock.msstock.application;

import com.gstock.msstock.application.exception.AddProductException;
import com.gstock.msstock.application.exception.MicroserviceCommunicationError;
import com.gstock.msstock.domain.Product;
import com.gstock.msstock.domain.Report;
import com.gstock.msstock.domain.Stock;
import com.gstock.msstock.infra.repository.ProductsRepository;
import com.gstock.msstock.infra.repository.ReportRepository;
import com.gstock.msstock.infra.repository.StockRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductsRepository productsRepository;
    private final StockRepository stockRepository;
    private final ReportRepository reportRepository;

    public Stock addProductToStock(String sku, Integer quantity) throws AddProductException, MicroserviceCommunicationError {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive integer");
        }

        try {
            Stock stock = stockRepository.findBySku(sku)
                    .orElseGet(() -> {
                        ResponseEntity<Product> productResponse = productsRepository.getProductsBySku(sku);
                        if (!productResponse.getStatusCode().is2xxSuccessful() || productResponse.getBody() == null) {
                            try {
                                throw new AddProductException("Product not found in msproducts");
                            } catch (AddProductException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        return new Stock(null, sku, 0);
                    });
            stock.setQuantity(stock.getQuantity() + quantity);
            return stockRepository.save(stock);
        } catch (FeignException.FeignClientException e){
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status){
                throw new AddProductException("Product not found in msproducts");
            }
            throw new MicroserviceCommunicationError(e.getMessage(), status);
        }
    }

    public List<Stock> getAll(){
        return stockRepository.findAll();
    }

    public Optional<Stock> getBySku(String sku){
        return stockRepository.findBySku(sku);
    }

    public Report generateReports() throws MicroserviceCommunicationError{
        try {
            int getAllStock = stockRepository.findAll().size();

            List<Product> getAllProducts = productsRepository.getAll();
            int getQuantityProducts = getAllProducts.size();

            BigDecimal totalValueProducts = getAllProducts.stream()
                    .map(Product::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Report report = new Report();
            report.setTotalProductsStock(getAllStock);
            report.setTotalRegisteredProducts(getQuantityProducts);
            report.setTotalValueProducts(totalValueProducts);
            return reportRepository.save(report);
        } catch (FeignException.FeignClientException e){
            int status = e.status();
            throw new MicroserviceCommunicationError(e.getMessage(), status);
        }
    }
}
