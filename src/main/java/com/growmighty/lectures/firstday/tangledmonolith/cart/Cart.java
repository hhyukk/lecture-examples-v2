package com.growmighty.lectures.firstday.tangledmonolith.cart;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Column(nullable = false)
    private Long userId;

    public static Cart create(Long userId) {
        Cart cart = new Cart();
        cart.userId = userId;
        return cart;
    }

    // addItem(productId, quantity) 로 변경
    // 같은 상품을 다시 담으면 수량 합산 (더할 수량도 1 이상인지 검사)
    public void addItem(Long productId, int quantity) {
        for(CartItem cartItem:this.items){
            if(cartItem.getProductId().equals(productId)){
                cartItem.addQuantity(quantity);
                return;
            }
        }
        CartItem item = CartItem.create(productId, quantity);
        this.items.add(item);

        if (item.getCart() != this) {
            item.assignCart(this);
        }
    }

    // changeQuantity(productId, n) 추가 - 없는 상품이면 예외
    // removeItem(productId) 추가 - 없는 상품이면 예외
    public void changeQuantity(Long productId, int quantity){
        for(CartItem cartItem:this.items){
            if(cartItem.getProductId().equals(productId)){
                cartItem.changeQuantity(quantity);
                return;
            }
        }
        throw new IllegalArgumentException("장바구니에 없는 상품입니다. productId: " + productId);
    }

    public void removeItem(Long productId){
        for(CartItem cartItem:this.items){
            if(cartItem.getProductId().equals(productId)){
                this.items.remove(cartItem);
                return;
            }
        }
        throw new IllegalArgumentException("장바구니에 없는 상품입니다. productId: " + productId);
    }

    public void clear() {
        this.items.clear();
    }
}
