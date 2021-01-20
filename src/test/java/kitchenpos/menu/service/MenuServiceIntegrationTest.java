package kitchenpos.menu.service;

import kitchenpos.IntegrationTest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.util.MenuRequestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuServiceIntegrationTest extends IntegrationTest {

    @Autowired
    MenuService menuService;

    @DisplayName("메뉴에 상품을 여러개 등록할 수 있다.")
    @Test
    void createMenu() {
        MenuResponse menuResponse = menuService.create(new MenuRequestBuilder()
                .withName("후라이드+양념")
                .withPrice(31000)
                .withGroupId(1L)
                .addMenuProduct(1L, 1)
                .addMenuProduct(2L, 1)
                .build());

        assertThat(menuResponse.getMenuProducts()).hasSize(2);
        assertThat(menuResponse.getPrice()).isEqualTo(31000);
    }

    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다.")
    @Test
    void isNotCollectMenuPrice() {
        assertThatThrownBy(() -> {
            MenuRequest menuRequest = new MenuRequestBuilder()
                    .withName("후라이드+양념")
                    .withPrice(-1)
                    .withGroupId(1L)
                    .addMenuProduct(2L, 1)
                    .build();
            menuService.create(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 상품이 추가 되어있지 않으면 등록할 수 없다.")
    @Test
    void notAddedProductInMenu() {
        assertThatThrownBy(() -> {
            MenuRequest menuRequest = new MenuRequestBuilder()
                    .withName("후라이드+양념")
                    .withPrice(35000)
                    .withGroupId(1L)
                    .build();
            menuService.create(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴의 상품 가격 * 수량보다 작아야 한다.")
    @Test
    void menuPriceLessThanProductAllPrice() {
        assertThatThrownBy(() -> {
            menuService.create(new MenuRequestBuilder()
                    .withName("후라이드+양념")
                    .withPrice(35000)
                    .withGroupId(1L)
                    .addMenuProduct(1L, 1)
                    .addMenuProduct(2L, 1)
                    .build()
            );
        }).isInstanceOf(IllegalArgumentException.class);
    }
}