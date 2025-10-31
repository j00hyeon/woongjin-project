package com.wjc.codetest.product.service;

import com.wjc.codetest.product.exception.ProductNotFoundException;
import com.wjc.codetest.product.model.request.CreateProductRequest;
import com.wjc.codetest.product.model.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.entity.Product;
import com.wjc.codetest.product.model.request.UpdateProductRequest;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product create(CreateProductRequest dto) {
        Product product = new Product(dto.getCategory(), dto.getName());
        return productRepository.save(product);
    }

    /**
     * [리뷰] 예외 처리 개선
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
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + productId));
    }

    public Product update(UpdateProductRequest dto) {
        Product product = getProductById(dto.getId());
        product.setCategory(dto.getCategory());
        product.setName(dto.getName());
        Product updatedProduct = productRepository.save(product);
        return updatedProduct;
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