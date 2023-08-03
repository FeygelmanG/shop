package com.example.shop;

import com.example.shop.models.ShopPojo;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.Objects;

import static com.example.shop.Configuration.buildFactory;
import static com.example.shop.Configuration.createNewSession;
import static io.qameta.allure.Allure.step;
import static org.hamcrest.Matchers.equalTo;


public class ShopControllerTests {
    private static final Faker faker = new Faker();
    static RequestSpecification requestSpec;
    ResponseSpecification responseShopDto;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:4000";
    }

    @BeforeEach
    public void setClient() {

        requestSpec = RestAssured.given();
        ResponseSpecBuilder specBuilder = new ResponseSpecBuilder()
                .expectStatusCode(200);
        responseShopDto = specBuilder.build();
    }

    @Test
    @DisplayName("ControllerTests.Добавление тестового магазина")
    public void shouldAddShop() {
        String shopName = faker.funnyName().name();
        JSONObject object = new JSONObject();
        object.put("shopName", shopName);
        object.put("shopPublic", faker.random().nextBoolean());

        requestSpec.body(object.toString());
        requestSpec.header("content-type", "application/json");
        Response response = requestSpec.post("/shops/add");
        response.then()
                .statusCode(200);
    }

    @Test
    @DisplayName("ControllerTests.Негативная проверка добавления тестового магазина с именем > 256 символов")
    public void shouldNotAddShopWithExtraLongName() {
        String shopName = "s".repeat(256) +
                faker.funnyName().name();
        JSONObject object = new JSONObject();
        object.put("shopName", shopName);
        object.put("shopPublic", faker.random().nextBoolean());

        requestSpec.body(object.toString());
        requestSpec.header("content-type", "application/json");
        Response response = requestSpec.post("/shops/add");
        response.then()
                .statusCode(400);
    }

    @Test
    @DisplayName("ControllerTests.Негативная проверка добавления тестового магазина с именем, начинающимся " +
            "с маленькой буквы")
    public void shouldNotBigFirstLetterNameShop() {
        String shopName = faker.funnyName().name().toLowerCase();
        JSONObject object = new JSONObject();
        object.put("shopName", shopName);
        object.put("shopPublic", faker.random().nextBoolean());

        requestSpec.body(object.toString());
        requestSpec.header("content-type", "application/json");
        Response response = requestSpec.post("/shops/add");
        response.then()
                .statusCode(400)
                .body(equalTo("Name should begin with a capital letter"));
    }

    @Test
    @DisplayName("ControllerTests.Негативная проверка добавления тестового магазина с именем < 6 символов")
    public void shouldNot6LettersShop() {
        String shopName = faker.funnyName().name();
        JSONObject object = new JSONObject();
        object.put("shopName", shopName.substring(1,5));
        object.put("shopPublic", faker.random().nextBoolean());

        requestSpec.body(object.toString());
        requestSpec.header("content-type", "application/json");
        Response response = requestSpec.post("/shops/add");
        response.then()
                .statusCode(400)
                .body(equalTo("Name should be more than 6 letters"));
    }

    @Test
    @DisplayName("ControllerTests.Получение всех магазинов")
    public void shouldGetAllShop() {
        Response response = requestSpec.get("/shops/all");
        response.then().statusCode(200);
    }

    @Test
    @DisplayName("ControllerTests.Получение магазина по id")
    public void shouldGetShopByIdTest() {
        String shopName = faker.funnyName().name();
        Boolean shopPublic = faker.random().nextBoolean();

        step("Создать тестовый магазин", () -> {
            JSONObject object = new JSONObject();
            object.put("shopName", shopName);
            object.put("shopPublic", shopPublic);

            requestSpec.body(object.toString());
            requestSpec.header("content-type", "application/json");
            Response response = requestSpec.post("/shops/add");
            response.then()
                    .statusCode(200);
        });
        step("Получить магазин по полученному id тестового магазина", () -> {
            SessionFactory factory = buildFactory();
            Session session = createNewSession(Objects.requireNonNull(factory));
            Long shopId = 0L;
            var shopsList = session.createNativeQuery("SELECT * FROM shops ORDER BY shop_id DESC", ShopPojo.class).list();
            Assertions.assertTrue(shopsList.size() > 0);
            int i = 0;
            String shopNameSelect = "";
            while ((i <=  shopsList.size()-1) & (!shopNameSelect.equals(shopName))) {
                ShopPojo testShop = shopsList.get(i);
                shopNameSelect = testShop.getShopName();
                shopId = testShop.getShopId();
                i++;
            }
            session.close();

            requestSpec
                    .get("/shops/{shopId}", shopId)
                    .then()
                    .spec(responseShopDto)
                    .body("shopName", equalTo(shopName), "shopPublic", equalTo(shopPublic));
        });
    }

    @Test
    @DisplayName("ControllerTests.Удаление тестового магазина")
    public void shouldDeleteShop() {
        String shopName = faker.funnyName().name();


        step("Создать тестовый магазин", () -> {
            JSONObject object = new JSONObject();
            object.put("shopName", shopName);
            object.put("shopPublic", faker.random().nextBoolean());

            requestSpec.body(object.toString());
            requestSpec.header("content-type", "application/json");
            Response response = requestSpec.post("/shops/add");
            response.then()
                    .statusCode(200);
        });

        step("Удалить магазин по полученному id тестового магазина", () -> {
            SessionFactory factory = buildFactory();
            Session session = createNewSession(Objects.requireNonNull(factory));
            Long shopId = 0L;
            var shopsList = session.createNativeQuery("SELECT * FROM shops ORDER BY shop_id DESC", ShopPojo.class).list();
            Assertions.assertTrue(shopsList.size() > 0);
            int i = shopsList.size() - 1;
            String shopNameSelect = "";
            while ((i >= 0) & (!shopNameSelect.equals(shopName))) {
                ShopPojo testShop = shopsList.get(i);
                shopNameSelect = testShop.getShopName();
                shopId = testShop.getShopId();
                i--;
            }
            session.close();

            requestSpec
                    .delete("/shops/delete/{id}", shopId)
                    .then()
                    .statusCode(204);
        });
    }
}
