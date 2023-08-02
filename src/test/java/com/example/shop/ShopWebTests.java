package com.example.shop;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.example.shop.models.ShopPojo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.*;
import static com.example.shop.Configuration.buildFactory;
import static com.example.shop.Configuration.createNewSession;
import static io.qameta.allure.Allure.step;

public class ShopWebTests extends BaseTest {

    @BeforeEach
    public void setSelenide() {
        open("http://localhost:4000");
    }

    @Test
    @DisplayName("WebTests.Удаление последнего из добавленных магазинов по Id")
    public void shouldDeleteShops() {

        SessionFactory factory = buildFactory();
        Session session = createNewSession(Objects.requireNonNull(factory));
        String shopId;
        var shopsList = session.createNativeQuery("SELECT * FROM shops ORDER BY shop_id DESC", ShopPojo.class).list();
        Assertions.assertTrue(shopsList.size() > 0);
        ShopPojo testShop = shopsList.get(0);
        shopId = testShop.getShopId().toString();
        session.close();

        step("Ввести " + shopId + " в поле Id для 'Delete a shop' и нажать кнопку 'Delete shop'", () -> inputDeleteId(shopId));

        step("Проверить, что в списке магазинов не отображается магазин с id " + shopId, () -> {
            ElementsCollection itemsSearch = $$x("//td[contains(text(),'" + shopId + "')]");
            itemsSearch.shouldBe(CollectionCondition.size(0));
        });
    }

    @Test
    @DisplayName("WebTests.Попытка удалить магазин без указания Id")
    public void shouldNotDeleteShopsWithoutId() {

        String shopId = "";

        step("Оставить пустое значение в поле Id для 'Delete a shop' и нажать кнопку 'Delete shop'", () -> inputDeleteId(shopId));

        step("Проверить, появляется ошибка", () -> {
            ElementsCollection itemsSearch = $$x("//div[@id='id_validation'][contains(@style,'visibility: visible;')]");
            itemsSearch.shouldBe(CollectionCondition.size(1));
        });
    }

    public void inputDeleteId(String textId) {
        SelenideElement deleteField = $x("//input[@id=\"id\"]");
        deleteField.sendKeys(textId);
        SelenideElement magnifier = $x("//button[@onclick=\"deleteShop();\"]");
        magnifier.click();
    }

    @Test
    @DisplayName("WebTests.Негативная проверка добавления тестового магазина с именем, начинающимся " +
            "с маленькой буквы")
    public void shouldNotBigFirstLetterNameShop() {

        String shopId = "";
// TODO: 02.08.2023
        step("Оставить пустое значение в поле Id для 'Delete a shop' и нажать кнопку 'Delete shop'", () -> inputDeleteId(shopId));

        step("Проверить, появляется ошибка", () -> {
            ElementsCollection itemsSearch = $$x("//div[@id='id_validation'][contains(@style,'visibility: visible;')]");
            itemsSearch.shouldBe(CollectionCondition.size(1));
        });
    }

    @Test
    @DisplayName("WebTests.Негативная проверка добавления тестового магазина с именем < 6 символов")
    public void shouldNot6LettersShop() {
// TODO: 02.08.2023
        String shopId = "";

        step("Оставить пустое значение в поле Id для 'Delete a shop' и нажать кнопку 'Delete shop'", () -> inputDeleteId(shopId));

        step("Проверить, появляется ошибка", () -> {
            ElementsCollection itemsSearch = $$x("//div[@id='id_validation'][contains(@style,'visibility: visible;')]");
            itemsSearch.shouldBe(CollectionCondition.size(1));
        });
    }

    // TODO: 02.08.2023  
//    Должна быть возможность обновлять список текущих магазинов с помощью
//    специальной кнопки refresh, чтобы в случае обновления списка по api, можно
//    было увидеть актуальный список магазинов
//6. Должна быть навигация по странице в хэдере сайта
//7. Должны быть кнопки в футере для перехода на страницы соц сетей

}
