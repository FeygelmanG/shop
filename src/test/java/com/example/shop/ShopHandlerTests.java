package com.example.shop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static com.example.shop.ShopHandler.checkFirstLetter;
import static com.example.shop.ShopHandler.checkLength;


public class ShopHandlerTests {
//  1.c. Имя магазина может содержать любые символы, но обязано начинаться с большой буквы
//  Позитивный
    @ParameterizedTest
    @ValueSource(strings = {"Sporttovari", "InnopolisMarket", "ArskiiMed"})
    public void shouldFirstLetter(String nameShop) {

        Assertions.assertTrue(checkFirstLetter(nameShop));
    }

//  1.c. Имя магазина может содержать любые символы, но обязано начинаться с большой буквы
//  Негативный
    @ParameterizedTest
    @ValueSource(strings = {"sporttovari", "innopolisMarket", "arskiiMed"})
    public void shouldNotFirstLetter(String nameShop) {

        Assertions.assertFalse(checkFirstLetter(nameShop));
    }
//  1.b. Имя магазина должно быть больше 6 символов
//  Позитивный
    @ParameterizedTest
    @ValueSource(strings = {"Sporttovari", "InnopolisMarket", "ArskiiMed"})
    public void shouldLengthShop(String nameShop) {

        Assertions.assertTrue(checkLength(nameShop, 7));
    }
//  1.b. Имя магазина должно быть больше 6 символов
//  Негативный
    @ParameterizedTest
    @ValueSource(strings = {"Sport", "Inno", "Arsk"})
    public void shouldNotLengthShop(String nameShop) {

        Assertions.assertFalse(checkLength(nameShop, 7));
    }

}
