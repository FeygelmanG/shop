package com.example.shop;

import com.example.shop.controllers.ShopController;
import com.example.shop.models.ShopDto;
import com.example.shop.models.ShopPojo;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;

import java.util.Objects;

import static io.qameta.allure.Allure.step;


public class ShopControllerTests {
    private static final Faker faker = new Faker();

    ShopController shopController = new ShopController();

    @Test
    @DisplayName("ControllerTests.Добавление тестового магазина")
    public void shouldAddShop() {
        String shopName = faker.funnyName().name();
        ShopDto shopDto = new ShopDto(0L, shopName, faker.random().nextBoolean());
        Assertions.assertEquals(200, shopController.addShop(shopDto).getStatusCode().value());

    }

    @Test
    @DisplayName("ControllerTests.Негативная проверка добавления тестового магазина с именем > 256 символов")
    public void shouldNotAddShopWithExtraLongName() {
        String shopName = "s".repeat(256) +
                faker.funnyName().name();
        ShopDto shopDto = new ShopDto(0L, shopName, faker.random().nextBoolean());
        var responseAddShop = shopController.addShop(shopDto);
        Assertions.assertEquals(400, responseAddShop.getStatusCode().value());
    }

    @Test
    @DisplayName("ControllerTests.Негативная проверка добавления тестового магазина с именем, начинающимся " +
            "с маленькой буквы")
    public void shouldNotBigFirstLetterNameShop() {
        String shopName = faker.funnyName().name().toLowerCase();
        ShopDto shopDto = new ShopDto(0L, shopName, faker.random().nextBoolean());

        var responseAddShop = shopController.addShop(shopDto);
        Assertions.assertEquals(400, responseAddShop.getStatusCode().value());
        Assertions.assertEquals("Name should begin with a capital letter", responseAddShop.getBody());
    }

    @Test
    @DisplayName("ControllerTests.Негативная проверка добавления тестового магазина с именем < 6 символов")
    public void shouldNot6LettersShop() {
        String shopName = faker.funnyName().name().substring(1, 5);
        ShopDto shopDto = new ShopDto(0L, shopName, faker.random().nextBoolean());

        var responseAddShop = shopController.addShop(shopDto);
        Assertions.assertEquals(400, responseAddShop.getStatusCode().value());
        Assertions.assertEquals("Name should be more than 6 letters", responseAddShop.getBody());
    }

    @Test
    @DisplayName("ControllerTests.Получение всех магазинов")
    public void shouldGetAllShop() {
        Assertions.assertEquals(200, shopController.getShops().getStatusCode().value());
    }

    @Test
    @DisplayName("ControllerTests.Получение магазина по id")
    public void shouldGetShopByIdTest() {
        String shopName = faker.funnyName().name();
        Boolean shopPublic = faker.random().nextBoolean();

        step("Создать тестовый магазин", () -> {
            ShopDto shopDto = new ShopDto(0L, shopName, shopPublic);
            Assertions.assertEquals(200, shopController.addShop(shopDto).getStatusCode().value());
        });
        step("Получить магазин по полученному id тестового магазина", () -> {
            var responseAddShop = shopController.getShop(getIdShop(shopName));
            Assertions.assertEquals(200, responseAddShop.getStatusCode().value());
            Assertions.assertEquals(Objects.requireNonNull(responseAddShop.getBody()).getShopName(), shopName);
            Assertions.assertEquals(responseAddShop.getBody().getShopPublic(), shopPublic);

        });
    }

    @Test
    @DisplayName("ControllerTests.Удаление тестового магазина")
    public void shouldDeleteShop() {
        String shopName = faker.funnyName().name();

        step("Создать тестовый магазин", () -> {
            ShopDto shopDto = new ShopDto(0L, shopName, faker.random().nextBoolean());
            Assertions.assertEquals(200, shopController.addShop(shopDto).getStatusCode().value());
        });

        step("Удалить магазин по полученному id тестового магазина", () -> Assertions.assertEquals(204,
                shopController.deleteShop(getIdShop(shopName)).getStatusCode().value()));
    }

    public Long getIdShop(String shopName) {
        var shopsList = shopController.getShops().getBody();
        Long shopId = 0L;
        assert shopsList != null;
        Assertions.assertTrue(shopsList.size() > 0);
        int i = shopsList.size() - 1;
        String shopNameSelect = "";
        while ((i >= 0) & (!shopNameSelect.equals(shopName))) {
            ShopPojo testShop = shopsList.get(i);
            shopNameSelect = testShop.getShopName();
            shopId = testShop.getShopId();
            i--;
        }
        return shopId;
    }
}
