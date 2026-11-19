package com.growmighty.lectures.firstday.tangledmonolith.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //근거: FK가 seller_id에 있음
    //판매자 없는 상품은 불가능 하지만 상품 없는 판매자는 가능.
    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Setter
    @Column(nullable = false)
    private Integer stockQuantity;

    @Lob
    private String description;

    public static Product create(Long sellerId, String name, BigDecimal price, Integer stockQuantity, String description) {
        Product product = new Product();
        product.sellerId = sellerId;
        product.name = name;
        product.price = price;
        product.stockQuantity = stockQuantity;
        product.description = description;
        return product;
    }
}
