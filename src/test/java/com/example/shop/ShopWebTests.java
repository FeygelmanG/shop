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
import org.testng.annotations.BeforeClass;

import java.util.Objects;

import static com.codeborne.selenide.Selenide.*;
import static com.example.shop.Configuration.buildFactory;
import static com.example.shop.Configuration.createNewSession;
import static io.qameta.allure.Allure.step;

public class ShopWebTests extends BaseTest {
    @BeforeClass
    public static void beforeClass() {
    }

    @BeforeEach
    public void setSelenide() {
        open("http://localhost:4000");
    }

    @Test
    @DisplayName("Удаление магазина")
    public void shouldDeleteShops() {

        SessionFactory factory = buildFactory();
        Session session = createNewSession(Objects.requireNonNull(factory));
        String shopId;
        var shopsList = session.createNativeQuery("SELECT * FROM shops ORDER BY shop_id DESC", ShopPojo.class).list();
        Assertions.assertTrue(shopsList.size() > 0);
        ShopPojo testShop = shopsList.get(0);
        shopId = testShop.getShopId().toString();
        session.close();

        step("Ввести " + shopId + " в поле Id для 'Delete a shop'", () -> inputDeleteId(shopId));

        step("Проверить, что в списке магазинов не отображается созданный магазин с id " + shopId, () -> {
            ElementsCollection itemsSearch = $$x("//tbody[@id=\"response\"]");
            itemsSearch.shouldBe(CollectionCondition.size(0));
        });
    }

    public void inputDeleteId(String textId) {
        SelenideElement deleteField = $x("//input[@id=\"id\"]");
        deleteField.sendKeys(textId);
        SelenideElement magnifier = $x("//button[@onclick=\"deleteShop();\"]");
        magnifier.click();
    }

}
