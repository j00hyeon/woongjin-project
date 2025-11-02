package com.wjc.codetest.product.repository;

import com.wjc.codetest.product.model.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String productName);

    Page<Product> findAllByCategory(String name, Pageable pageable);

    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findDistinctCategories();

//    @Query("SELECT p.category FROM Product p GROUP BY p.category")
//    List<String> findDistinctCategories();

//    @Query("SELECT p FROM Product p")
//    List<Product> findCategories();
}
