package com.example.shop;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testng.annotations.BeforeClass;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

public class ShopWebTests extends BaseTest{
    @BeforeClass
    public static void beforeClass() {
    }    @BeforeEach
    public void setSelenide() {
        open("http://localhost:4000");
    }

    @Test
    @DisplayName("Удаление магазина")
    public void shouldSearchProductsFivePosition() {

        step("Ввести \"8974\" в поле Id для 'Delete a shop'", () -> inputDeleteId("8974"));

        step("Проверить, что в списке магазинов не отображается магазин с id \"8974\"", () -> {
            ElementsCollection itemsSearch = $$x("//tbody[@id=\"response\"]");
            itemsSearch.shouldBe(CollectionCondition.size(5));
        });
    }
    public void inputDeleteId(String textId) {
        SelenideElement deleteField = $x("//input[@id=\"id\"]");
        deleteField.sendKeys(textId);
        SelenideElement magnifier = $x("//button[@onclick=\"deleteShop();\"]");
        magnifier.click();
    }

}
