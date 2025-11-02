package com.wjc.codetest.product.service;

import com.wjc.codetest.product.exception.DuplicateProductNameException;
import com.wjc.codetest.product.exception.ProductNotFoundException;
import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.model.response.ProductResponse;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 상품 생성 로직 개선
     * [문제]
     *  - 중복 이름 검증 누락
     *  - entity 반환
     * [개선안]
     *  - 중복 이름 검증 추가
     */
    @Transactional
    public void create(CreateProductRequest dto) {
        validateDuplicateName(dto.getName());
        Product product = new Product(dto.getCategory(), dto.getName());
        productRepository.save(product);
    }

    private void validateDuplicateName(String productName) {
        if (productRepository.existsByName(productName)) {
            throw new DuplicateProductNameException("이미 존재하는 상품명입니다: " + productName);
        }
    }

    /**
     * 예외 처리 개선
     * [문제]
     *  - isPresent() + get() 조합 사용
     *   -> 빈 Optional에 get() 호출 시 NoSuchElementException 발생
     *   -> Optional을 단순 null 체크용으로만 사용
     *  - RuntimeException 사용은 명확한 예외 원인을 알기 어려움
     * [원인] Optional 메서드 활용도 낮음
     * [개선안]
     *  - orElseThrow()로 가독성 향상
     *  - 커스텀 예외(ProductNotFoundException) 생성으로 명확한 예외 처리
     *  - GlobalExceptionHandler에서 404 상태 코드 반환
     */
    public ProductResponse getProductDtoById(Long productId) {
        return ProductResponse.fromEntity(getProductById(productId));
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + productId));
    }

    /**
     * 상품 수정 로직 개선
     * [문제]
     *  - Service에서 setter 직접 사용
     *  - 중복 이름 검증 누락
     *  - 부분 업데이트 검증 부재
     * [원인] 도메인 로직이 Service 계층에 노출
     * [개선안]
     *  - Entity에 update() 로직 추가
     *  - @Transactional + 더티체킹으로 save() 제거
     *  - 중복 이름 검증 추가
     *  - 최소 1개 필드 입력 검증
     */
    @Transactional
    public Product update(Long productId, UpdateProductRequest dto) {
        Product product = getProductById(productId);
        validateAtLeastOneField(dto);
        if ((dto.getName() != null) && (!dto.getName().isBlank())) {
            validateDuplicateName(dto.getName());
        }
        product.update(dto.getCategory(), dto.getName());
        return product;
    }

    private void validateAtLeastOneField(UpdateProductRequest dto) {
        if ((dto.getCategory() == null || dto.getCategory().isBlank()) &&
                (dto.getName() == null || dto.getName().isBlank())) {
            throw new IllegalArgumentException("최소 하나의 필드는 입력해야 합니다.");
        }
    }

    public void deleteById(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }

    public Page<Product> getListByCategory(GetProductListRequest dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.ASC, "category"));
        return productRepository.findAllByCategory(dto.getCategory(), pageRequest);
    }

    public List<String> getUniqueCategories() {
        return productRepository.findDistinctCategories();
    }
}