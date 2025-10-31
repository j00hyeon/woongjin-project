package com.wjc.codetest.product.model.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * [리뷰 1] Entity 패키지 구조 개선
 * [문제] model/domain 패키지에 Entity가 위치해 계층 구조 불명확
 * [원인] 도메인 개념과 영속성 계층이 혼재
 * [개선안] domain/entity 패키지로 분리해 Entity임을 명확하게 표현
 */

/**
 * [리뷰 2] @Setter 제거
 * [문제] @Setter 사용 시 외부에서 무분별하게 변경 가능
 * [원인] Lombok @Setter가 모든 필드에 public setter 생성
 * [개선안] @Setter 제거, 필요한 경우 메서드 추가 (updateCategory, updateName 등)
 */

/**
 * [리뷰 3] 중복 Getter 제거
 * [문제] @Getter annotation과 getter method 중복 작성
 * [개선안] @Getter 어노테이션으로 통일
 */
@Entity
@Getter
public class Product {

    /**
     * [리뷰 4] JPA 기본키 생성 전략 개선
     * [문제] AUTO 사용 시 H2가 SEQUENCE 방식 선택
     * [원인] MODE=MySQL로 MySQL 모드이지만 AUTO는 H2 기본 동작(SEQUENCE)을 따름
     * [개선안] IDENTITY로 변경해 AUTO_INCREMENT 방식 사용
     */
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category")
    private String category;

    @Column(name = "name")
    private String name;

    protected Product() {
    }

    public Product(String category, String name) {
        this.category = category;
        this.name = name;
    }
}
