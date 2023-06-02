package com.BackendDevTest.SimilarProductsDetails.infrastructure.controller;
import com.BackendDevTest.SimilarProductsDetails.infrastructure.dto.ProductDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
public class SimilarProductsDetailController {

    final String URI_GET_SIMILARSIDS_BASE = "http://localhost:3001/product/";
    final String complement = "/similarids";
    final String URLDetail = "http://localhost:3001/product/";

    private final WebClient.Builder webClient;

    @Autowired
    public SimilarProductsDetailController(WebClient.Builder webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/product/{productId}/similar")
    public ResponseEntity<List<ProductDetailDto>> getSimilarProductDetail(@PathVariable String productId) {
        try{
            List<String> listOfIds = webClient.build()
                    .get()
                    .uri(URI_GET_SIMILARSIDS_BASE+productId+complement)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                    .block();
            if (listOfIds!=null && !listOfIds.isEmpty()){
                List<ProductDetailDto> ProducList = listOfIds.stream().map(id -> webClient.build()
                        .get()
                        .uri(URLDetail+id)
                        .retrieve()
                        .bodyToMono(ProductDetailDto.class)
                        .block()).collect(Collectors.toList());
                if (productId!=null && !productId.isEmpty())
                    return new ResponseEntity<>(ProducList, HttpStatus.OK) ;
            }
        }catch (Exception e){
            Logger logger
                    = Logger.getLogger(
                    SimilarProductsDetailController.class.getName());
            logger.setLevel(Level.WARNING);
            logger.warning(e.getMessage());
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
