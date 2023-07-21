package com.example.shop;

import com.example.shop.models.ShopDto;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.parsing.Parser.JSON;
import static org.hamcrest.Matchers.equalTo;


public class ShopControllerTests {
    private final Faker faker = new Faker();
    RequestSpecification requestSpec;
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

    //4. Должен быть запрос для поиска, который ищет конкретный магазин по айди.
    //Выводит результат, если айди существует, в виде айди, названия и настройки
    //приватности магазина
    @Test
    public void shouldGetShopByIdTest() {
        requestSpec
                .get("/shops/{shopId}", 5753)
                .then()
                .spec(responseShopDto)
                .body("shopName", equalTo("TestShop"));
    }

    //3. Должен быть запрос для вывода всех существующих магазинов, который выводит айди, название и настройку
// приватности магазина(публичный/не публичный)
    @Test
    public void shouldGetAllShop() {
        Response response = requestSpec.get("/shops/all");
        response.then().statusCode(200);
    }

    //1. Должна быть возможность создавать магазины:
//    a. Один запрос = один новый магазин
//    b. Имя магазина должно быть больше 6 символов
//    c. Имя магазина может содержать любые символы, но обязано начинаться
//    с большой буквы
//    d. Длина имени ограничена 256 символами
//    e. При нарушении вышеперечисленных требований выводится
//    соответствующая ошибка (к текстам ошибок требований нет, но ошибка
//    должна быть обработана, то есть пользователю понятно, что пошло не
//    так, а не просто 500 или 400, не важно на бэке или на фронте)
    @Test
    public void shouldAddRandomShop() {
        Response response = requestSpec.post("/shops/add");
        response.then().statusCode(200);
    }

    //  1. Должна быть возможность создавать магазины:
//    a. Один запрос = один новый магазин
    @Test
    public void shouldAddShop() {
        //        JSONObject shop = new JSONObject()
//                .put("shopName", "Shop Name")
//                .put("shopPublic", false);
//
//        ShopDto shopRes = requestSpec
//                .body(shop.toString())
//                .contentType("application/json")
//                .post("/shops/add")
//                .as(new TypeRef<>() {});
//
//        assertThat(shopRes.getShopId().equals());

        String shopName = faker.funnyName().name();
        JSONObject object = new JSONObject();
        object.put("shopName", shopName);
        object.put("shopPublic", faker.random().nextBoolean());

        requestSpec.body(object.toString());
        requestSpec.header("content-type", "application/json");
        System.out.println(object);
        Response response = requestSpec.post("/shops/add");
//        System.out.println(response.then().extract().response().as(String.class));
        response.then()
                .statusCode(200);
//                .assertThat()
//                .body("shopName", equalTo(shopName));

    }
//2. Должна быть возможность удалять магазины:
//    a. Один запрос = один удаленный магазин
//    b. При попытке удалить магазин без указания айди на фронтенде
//    появляется соответствующая ошибка, на бэкенде не должно быть
    @Test
    public void shouldDeletePet() {

//        JSONObject shop = new JSONObject()
//                .put("id", 8054L)
//                .put("shopName", "Ben Down")
//                .put("shopPublic", "false");

//        ShopDto createdShop = generateShop(shop);

        requestSpec
                .delete("/shops/delete/{id}", 8054L)
                .then()
                .statusCode(204);
    }
//    private ShopDto generateShop(Object o) {
//        return request
//                .body(o.toString())
//                .contentType("application/json")
//                .post("/shop")
//                .as(new TypeRef<>() {});
//    }
}
