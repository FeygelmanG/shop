package com.example.shop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.example.shop.ShopHandler.checkFirstLetter;
import static com.example.shop.ShopHandler.checkLength;


public class ShopHandlerTests {
    @ParameterizedTest
    @ValueSource(strings = {"Sporttovari", "InnopolisMarket", "ArskiiMed"})
    @DisplayName("Положительная роверка, что имя магазина может содержать любые символы, но обязано начинаться с " +
            "большой буквы")
    public void shouldFirstLetter(String nameShop) {

        Assertions.assertTrue(checkFirstLetter(nameShop));
    }

    @DisplayName("Негативная проверка, что имя магазина может содержать любые символы, но обязано начинаться с " +
            "большой буквы")
    @ParameterizedTest
    @ValueSource(strings = {"sporttovari", "innopolisMarket", "arskiiMed"})
    public void shouldNotFirstLetter(String nameShop) {

        Assertions.assertFalse(checkFirstLetter(nameShop));
    }

    @DisplayName("Позитивная проверка, что имя магазина должно быть больше 6 символов")
    @ParameterizedTest
    @ValueSource(strings = {"Sporttovari", "InnopolisMarket", "ArskiiMed"})
    public void shouldLengthShop(String nameShop) {

        Assertions.assertTrue(checkLength(nameShop, 7));
    }

    @DisplayName("Негативная проверка, что имя магазина должно быть больше 6 символов")
    @ParameterizedTest
    @ValueSource(strings = {"Sport", "Inno", "Arsk"})
    public void shouldNotLengthShop(String nameShop) {

        Assertions.assertFalse(checkLength(nameShop, 7));
    }
}
