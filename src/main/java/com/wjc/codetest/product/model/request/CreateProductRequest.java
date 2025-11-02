package com.wjc.codetest.product.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CreateProductRequest 개선
 * [문제]
 *  - 불필요한 생성자 다수 존재
 *  - 검증 로직 부재
 * [원인] JSON 직렬화 방식 이해 부족
 * [개선안]
 *  - 생성자 제거 (Jackson이 기본 생성자 + Setter 사용)
 *  - 검증 로직 추가
 */
@Getter
@Setter
@NoArgsConstructor
public class CreateProductRequest {
    @NotBlank(message = "카테고리를 입력하세요.")
    private String category;

    @NotBlank(message = "상품명을 입력하세요.")
    private String name;
}
