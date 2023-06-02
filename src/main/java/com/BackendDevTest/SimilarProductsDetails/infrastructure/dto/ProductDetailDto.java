package com.BackendDevTest.SimilarProductsDetails.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDto {
    private String id;
    private String name;
    private BigDecimal price;
    private boolean availability;
}
