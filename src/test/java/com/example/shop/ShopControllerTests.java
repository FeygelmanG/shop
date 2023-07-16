package com.example.shop;

import com.example.shop.models.ShopDto;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.equalTo;


public class ShopControllerTests {
    private final Faker faker = new Faker();
    private RequestSpecification request;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8081";
    }

    @BeforeEach
    public void setClient() {
        request = RestAssured.given();
    }

    @Test
    public void shouldAddRandomShop() {
        Response response = request.post("/shop/add");

        response.then().statusCode(200);
    }
    @Test
    public void shouldGetShopByIdTest() {
        final ShopDto book = new ShopDto(faker.random().nextLong(5000),faker.commerce().productName(), faker.random().nextBoolean());

        Response response = request.get("/shop/{index}", 0);

        response.then().statusCode(200);
        response.then().body("shopName", equalTo(book.getShopName()),
                "shopPublic", equalTo(book.getClass()));
    }

    @Test
    public void shouldGetAllShop() {
        final ShopDto shop = new ShopDto(faker.random().nextLong(5000),faker.commerce().productName(), faker.random().nextBoolean());

        Response response = request.get("/shop/all", 0);

        response.then().statusCode(200);
    }

//    @Test
//    public void shouldDeletePet() {
//
//        JSONObject shop = new JSONObject()
//                .put("id", 3000L)
//                .put("shopName", "PetShop")
//                .put("shopPublic", "true");
//
//        ShopDto createdShop = generateShop(shop);
//
//        request
//                .delete("/shop/{id}", createdShop.getShopId())
//                .then()
//                .statusCode(200);
//    }
//    private ShopDto generateShop(Object o) {
//        return request
//                .body(o.toString())
//                .contentType("application/json")
//                .post("/shop")
//                .as(new TypeRef<>() {});
//    }

}
