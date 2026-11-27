package com.growmighty.lectures.firstday.tangledmonolith.cart;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    // 수량은 1 이상
    // public 제거
    static CartItem create(Long productId, int quantity) {
        isValidQuantity(quantity);
        CartItem cartItem = new CartItem();
        cartItem.productId = productId;
        cartItem.quantity = quantity;
        return cartItem;
    }

    void assignCart(Cart cart) {
        this.cart = cart;
    }

    void addQuantity(int quantity){
        isValidQuantity(quantity);
        this.quantity+=quantity;
    }

    void changeQuantity(int newQuantity){
        isValidQuantity(newQuantity);
        this.quantity=newQuantity;
    }

    public static void isValidQuantity(int quantity){
        if(quantity<1){
            throw new IllegalArgumentException("수량이 1 미만입니다. 수량: "+quantity);
        }
    }
}
