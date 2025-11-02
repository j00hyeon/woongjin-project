package com.wjc.codetest.product.controller;

import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.model.response.ProductListResponse;
import com.wjc.codetest.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RESTful API 설계 개선
 * [문제] URL에 동사 포함, 일관성 없는 URL 패턴
 * [원인] REST 원칙 미준수
 * [개선안] URL은 명사(리소스)만, 동작은 HTTP 메서드로 표현
 */

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /**
     * 상품 단건 조회
     */
    @GetMapping(value = "/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId){
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    /**
     * 상품 생성
     * [리뷰] HTTP 상태 코드 개선
     * [문제] Entity 직접 반환으로 내부 구조 노출
     * [개선안] 201 Created 반환
     */
    @PostMapping
    public ResponseEntity<Void> createProduct(@Valid @RequestBody CreateProductRequest dto){
        productService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping(value = "/{productId}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable Long productId){
        productService.deleteById(productId);
        return ResponseEntity.ok(true);
    }

    /**
     * 상품 수정
     * [리뷰] RESTful API 개선
     * [개선안]
     *  - PathVariable로 리소스 식별
     *  - RequestBody에 ID 제거
     */
    @PatchMapping(value = "/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId,
                                                 @RequestBody UpdateProductRequest dto){
        Product product = productService.update(productId, dto);
        return ResponseEntity.ok(product);
    }

    /**
     * 카테고리별 상품 목록 조회
     */
    @GetMapping(value = "/list")
    public ResponseEntity<ProductListResponse> getProductListByCategory(@RequestBody GetProductListRequest dto){
        Page<Product> productList = productService.getListByCategory(dto);
        return ResponseEntity.ok(new ProductListResponse(productList.getContent(), productList.getTotalPages(), productList.getTotalElements(), productList.getNumber()));
    }

    /**
     * 카테고리 목록 조회
     */
    @GetMapping(value = "/categories")
    public ResponseEntity<List<String>> getProductListByCategory(){
        List<String> uniqueCategories = productService.getUniqueCategories();
        return ResponseEntity.ok(uniqueCategories);
    }
}