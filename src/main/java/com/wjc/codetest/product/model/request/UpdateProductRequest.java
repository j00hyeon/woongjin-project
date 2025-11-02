package com.wjc.codetest.product.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * UpdateProductRequest 개선
 * [문제]
 *  - id 필드 포함으로 RequestBody에서 ID 조작 가능
 *  - 여러 생성자가 있으면 Jackson이 기본 생성자를 못찾음
 * [원인] 커스텀 생성자 존재 시 기본 생성자가 자동 생성 안됨
 * [개선안]
 *  - id 제거, PathVariable로 리소스 식별
 *  - @NoArgsConstructor 사용으로 기본 생성자 명시
 */
@Getter
@Setter
@NoArgsConstructor
public class UpdateProductRequest {
    private String category;
    private String name;
}
