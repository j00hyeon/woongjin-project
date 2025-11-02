package com.wjc.codetest.product.model.response;

import com.wjc.codetest.product.model.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateProductResponse {
    private Long id;
    private String name;
    private String category;

    public static CreateProductResponse fromEntity(Product product) {
        return CreateProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .build();
    }
}
