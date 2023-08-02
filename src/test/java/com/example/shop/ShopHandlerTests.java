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
    @DisplayName("UnitTests.Положительная проверка, что имя магазина может содержать любые символы, но обязано начинаться с " +
            "большой буквы")
    public void shouldBigFirstLetter(String nameShop) {

        Assertions.assertTrue(checkFirstLetter(nameShop));
    }

    @DisplayName("UnitTests.Негативная проверка, что имя магазина может содержать любые символы, но обязано начинаться с " +
            "большой буквы")
    @ParameterizedTest
    @ValueSource(strings = {"sporttovari", "innopolisMarket", "arskiiMed"})
    public void shouldNotBigFirstLetter(String nameShop) {

        Assertions.assertFalse(checkFirstLetter(nameShop));
    }

    @DisplayName("UnitTests.Позитивная проверка, что имя магазина должно быть больше 6 символов")
    @ParameterizedTest
    @ValueSource(strings = {"Sporttovari", "InnopolisMarket", "ArskiiMed"})
    public void should6LettersShop(String nameShop) {

        Assertions.assertTrue(checkLength(nameShop, 7));
    }

    @DisplayName("UnitTests.Негативная проверка, что имя магазина должно быть больше 6 символов")
    @ParameterizedTest
    @ValueSource(strings = {"Sport", "Inno", "Arsk"})
    public void shouldNot6LettersShop(String nameShop) {

        Assertions.assertFalse(checkLength(nameShop, 7));
    }
}
