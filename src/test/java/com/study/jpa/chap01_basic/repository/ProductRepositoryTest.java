package com.study.jpa.chap01_basic.repository;

import com.study.jpa.chap01_basic.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional // jpa는 트랜잭션 단위로 동작하기 때문
@Rollback(false) // 테스트 클래스에서 트랜잭션을 사용하면 Rollback이 자동으로 됨
class ProductRepositoryTest {
@Autowired
    ProductRepository productRepository;

@Test
@DisplayName("데이터베이스")
void  testSave(){
    Product p = Product.builder()
            .name("아이폰")
            .price(100000)
            .category(Product.Category.FASHION)
            .build();

    Product saved = productRepository.save(p);
assertNotNull(saved);
}


}