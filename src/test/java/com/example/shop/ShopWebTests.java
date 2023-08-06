package com.example.shop;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.example.shop.models.ShopPojo;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
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
    private static final Faker faker = new Faker();

    @BeforeEach
    public void setSelenide() {
        open("http://localhost:4000");
    }

    @Test
    @DisplayName("WebTests.Обновление списока текущих магазинов")
    public void shouldRefreshShops() {
        String shopName = faker.funnyName().name();
        Boolean shopPublic = faker.random().nextBoolean();

        step("Проверить, что в списке 'Already created shops' не отображается магазин с name " + shopName, () -> {
            ElementsCollection itemsSearch = $$x("//td[contains(text(),'" + shopName + "')]");
            itemsSearch.shouldBe(CollectionCondition.size(0));
        });


        step("Добавить тестовый магазин по API", () -> {
            RequestSpecification requestSpec;
            RestAssured.baseURI = "http://localhost:4000";
            requestSpec = RestAssured.given();
            JSONObject object = new JSONObject();
            object.put("shopName", shopName);
            object.put("shopPublic", shopPublic);

            requestSpec.body(object.toString());
            requestSpec.header("content-type", "application/json");
            Response response = requestSpec.post("/shops/add");
            response.then()
                    .statusCode(200);
        });

        step("Проверить, что в списке 'Already created shops' отображается магазин с name " + shopName, () -> {
            SelenideElement magnifier = $x("//button[@onclick=\"location.reload(); scrollIntoView()\"]");
            magnifier.click();
            ElementsCollection itemsSearch = $$x("//td[contains(text(),'" + shopName + "')]");
            itemsSearch.shouldBe(CollectionCondition.sizeGreaterThan(0));
        });
    }

    @Test
    @DisplayName("WebTests.Навигация по странице в хэдере сайта")
    public void shouldNavigation() {

        step("Проверить, что в  работает навигация по 'Create shop' в хэдере сайта", () -> {
            SelenideElement itemsNav1 = $x("//*[@id=\"links\"]/a[1]");
            itemsNav1.click();
            SelenideElement createField = $x("//*[@id=\"create\"]/div/h2");
            createField.shouldBe(Condition.visible);
        });

        step("Проверить, что в  работает навигация по 'All shop' в хэдере сайта", () -> {
            SelenideElement itemsNav1 = $x("//*[@id=\"links\"]/a[2]");
            itemsNav1.click();
            SelenideElement createField = $x("//*[@id=\"shops_div\"]/h2");
            createField.shouldBe(Condition.visible);
        });

        step("Проверить, что в  работает навигация по 'Delete shop' в хэдере сайта", () -> {
            SelenideElement itemsNav1 = $x("//*[@id=\"links\"]/a[3]");
            itemsNav1.click();
            SelenideElement createField = $x("//*[@id=\"delete\"]/div/h2");
            createField.shouldBe(Condition.visible);
        });
    }

    @Test
    @DisplayName("WebTests.Кнопки в футере для перехода на страницы соц сетей")
    public void shouldSLan() {

        step("Проверить, что в футере есть кнопка для перехода на страницу \"https://web.telegram.org/\"", () -> {
            SelenideElement itemTelegram = $x("/html/body/footer/div/a[1]");
            itemTelegram.shouldBe(Condition.visible);
        });

        step("Проверить, что в футере есть кнопка для перехода на страницу \"https://m.vk.com/\"", () -> {
            SelenideElement itemTelegram = $x("/html/body/footer/div/a[2]");
            itemTelegram.shouldBe(Condition.visible);
        });
    }

    @Test
    @DisplayName("WebTests.Добавление тестового магазина")
    public void shouldAddShops() {
        String shopName = faker.funnyName().name();

        step("Ввести " + shopName + " в поле 'Name' для 'Create a shop' и нажать кнопку 'Create shop'",
                () -> inputNewShop(shopName));

        step("Проверить, что в списке магазинов отображается магазин с name " + shopName, () -> {
            ElementsCollection itemsSearch = $$x("//td[contains(text(),'" + shopName + "')]");
            itemsSearch.shouldBe(CollectionCondition.sizeGreaterThan(0));
        });
    }

    @Test
    @DisplayName("WebTests.Негативная проверка добавления тестового магазина с именем, начинающимся " +
            "с маленькой буквы")
    public void shouldNotAddNameShopWithLowFirstLetter() {
        String shopName = faker.funnyName().name().toLowerCase();

        step("Ввести название нового магазина с маленькой буквы для 'Create a shop' и нажать кнопку 'Create shop'"
                , () -> inputNewShop(shopName));

        step("Проверить, появляется ошибка", () -> {
            ElementsCollection itemsSearch = $$x("//div[@id='name_validation']" +
                    "[contains(@style,'visibility: visible;')]");
            itemsSearch.shouldBe(CollectionCondition.size(1));
        });
    }

    @Test
    @DisplayName("WebTests.Негативная проверка добавления тестового магазина с именем из 6 символов")
    public void shouldNotAddNameShopWith6Letters() {
        String shopName = faker.funnyName().name().substring(1, 6);

        step("Ввести название нового магазина с именем из 6 символов для 'Create a shop' и нажать кнопку " +
                "'Create shop'", () -> inputNewShop(shopName));

        step("Проверить, появляется ошибка", () -> {
            ElementsCollection itemsSearch = $$x("//div[@id='name_validation']" +
                    "[contains(@style,'visibility: visible;')]");
            itemsSearch.shouldBe(CollectionCondition.size(1));
        });
    }

    public void inputNewShop(String textId) {
        SelenideElement createField = $x("//input[@id=\"name\"]");
        createField.sendKeys(textId);
        SelenideElement magnifier = $x("//button[@onclick=\"addShop()\"]");
        magnifier.click();
    }

    @Test
    @DisplayName("WebTests.Удаление последнего из добавленных магазинов по Id")
    public void shouldDeleteShops() {

        SessionFactory factory = buildFactory();
        Session session = createNewSession(Objects.requireNonNull(factory));
        String shopId;
        var shopsList = session.createNativeQuery("SELECT * FROM shops ORDER BY shop_id DESC",
                ShopPojo.class).list();
        Assertions.assertTrue(shopsList.size() > 0);
        ShopPojo testShop = shopsList.get(0);
        shopId = testShop.getShopId().toString();
        session.close();

        step("Ввести " + shopId + " в поле Id для 'Delete a shop' и нажать кнопку 'Delete shop'",
                () -> inputDeleteId(shopId));

        step("Проверить, что в списке магазинов не отображается магазин с id " + shopId, () -> {
            ElementsCollection itemsSearch = $$x("//td[contains(text(),'" + shopId + "')]");
            itemsSearch.shouldBe(CollectionCondition.size(0));
        });
    }

    @Test
    @DisplayName("WebTests.Негативная проверка удаления магазина без указания Id")
    public void shouldNotDeleteShopsWithoutId() {

        String shopId = "";

        step("Оставить пустое значение в поле Id для 'Delete a shop' и нажать кнопку 'Delete shop'",
                () -> inputDeleteId(shopId));

        step("Проверить, появляется ошибка", () -> {
            ElementsCollection itemsSearch = $$x("//div[@id='id_validation']" +
                    "[contains(@style,'visibility: visible;')]");
            itemsSearch.shouldBe(CollectionCondition.size(1));
        });
    }

    public void inputDeleteId(String textId) {
        SelenideElement deleteField = $x("//input[@id=\"id\"]");
        deleteField.sendKeys(textId);
        SelenideElement magnifier = $x("//button[@onclick=\"deleteShop();\"]");
        magnifier.click();
    }

}
