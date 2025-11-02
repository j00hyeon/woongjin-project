package com.wjc.codetest.product.model.response;

import com.wjc.codetest.product.model.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateProductResponse {
    private String name;
    private String category;

    public static UpdateProductResponse fromEntity(Product product) {
        return UpdateProductResponse.builder()
                .name(product.getName())
                .category(product.getCategory())
                .build();
    }
}
