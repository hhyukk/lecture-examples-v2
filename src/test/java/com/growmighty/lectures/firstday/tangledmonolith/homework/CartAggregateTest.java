package com.growmighty.lectures.firstday.tangledmonolith.homework;

import com.growmighty.lectures.firstday.tangledmonolith.cart.Cart;
import com.growmighty.lectures.firstday.tangledmonolith.cart.CartItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * [과제 DDD-2] 장바구니 애그리거트 불변식 테스트 — 뼈대입니다.
 * <p>
 * 1. 먼저 Cart / CartItem 을 아래 규칙대로 고치세요.
 * 2. 각 테스트의 fail(...) 줄을 지우고 given / when / then 을 채우세요.
 * 3. 스프링 없이 도는 순수 단위 테스트로 작성합니다 (@SpringBootTest 금지).
 * <p>
 * 목표 API 예시 (이름은 바꿔도 됩니다)
 * Cart cart = Cart.create(userId);
 * cart.addItem(productId, quantity);
 * cart.changeQuantity(productId, newQuantity);
 * cart.removeItem(productId);
 */
class CartAggregateTest {

    // 테스트 6개 채우기

    @Test
    @DisplayName("새 상품을 담으면 항목이 하나 추가된다")
    void 새_상품을_담으면_항목이_추가된다() {
        // given: 빈 장바구니
        Cart cart = Cart.create(100L);
        // when : addItem(1L, 2)
        cart.addItem(1L, 2);
        // then : 항목 1개, 수량 2
        assertThat(cart.getItems()).hasSize(1);
        CartItem item = cart.getItems().get(0);
        assertThat(item.getProductId()).isEqualTo(1L);
        assertThat(item.getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("같은 상품을 다시 담으면 새 항목이 아니라 수량이 합쳐진다")
    void 같은_상품을_담으면_수량이_합쳐진다() {
        Cart cart = Cart.create(1L);
        cart.addItem(1L, 2);
        cart.addItem(1L, 3);
        assertThat(cart.getItems()).hasSize(1);
        CartItem item = cart.getItems().get(0);
        assertThat(item.getProductId()).isEqualTo(1L);
        assertThat(item.getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("수량이 0 이하이면 담을 수 없다")
    void 수량이_0_이하면_예외() {
        Cart cart = Cart.create(1L);

        assertThatThrownBy(() -> cart.addItem(1L, 0))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> cart.addItem(1L, -1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(cart.getItems()).isEmpty();

        cart.addItem(1L, 1);
        assertThatThrownBy(() -> cart.addItem(1L, 0))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(cart.getItems()).hasSize(1);
        CartItem item = cart.getItems().get(0);
        assertThat(item.getQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("담긴 상품의 수량을 바꿀 수 있다")
    void 수량을_변경한다() {
        Cart cart = Cart.create(1L);
        cart.addItem(1L, 1);
        CartItem item = cart.getItems().get(0);
        assertThat(item.getQuantity()).isEqualTo(1);
        cart.changeQuantity(1L, 10);
        assertThat(item.getQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("장바구니에 없는 상품의 수량을 바꾸면 예외가 난다")
    void 없는_상품의_수량_변경은_예외() {
        Cart cart = Cart.create(1L);
        cart.addItem(1L, 1);
        assertThatThrownBy(() -> cart.changeQuantity(2L, 10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("담긴 상품을 뺄 수 있다")
    void 상품을_뺀다() {
        Cart cart = Cart.create(1L);
        cart.addItem(1L, 1);
        cart.addItem(2L, 1);
        assertThat(cart.getItems()).hasSize(2);
        cart.removeItem(1L);
        assertThat(cart.getItems()).hasSize(1);
    }

    // [도전] 상품 종류 상한 테스트 추가
    @Test
    @DisplayName("상품 종류 상한")
    void 상품_종류가_50개면_새_상품은_담을_수_없다(){
        Cart cart = Cart.create(1L);
        for (int i = 0; i < 50; i++) {
            cart.addItem(i + 1L, 10);
        }
        assertThat(cart.getItems()).hasSize(50);
        assertThatThrownBy(() -> cart.addItem(51L, 10))
                .isInstanceOf(IllegalStateException.class);
        assertThat(cart.getItems()).hasSize(50);
    }
}
