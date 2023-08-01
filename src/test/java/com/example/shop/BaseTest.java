package com.example.shop;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Objects;

import static com.codeborne.selenide.WebDriverRunner.setWebDriver;

public class BaseTest {

    @BeforeAll
    public static void setDriver() throws MalformedURLException {
        String isRemote = System.getenv("IS_REMOTE");
        if (Objects.equals(isRemote, "true")) {
            Configuration.timeout = 6000;
            Configuration.browser = "chrome";
            Configuration.browserSize = "1920x1080";
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setCapability("enableVNC:", true);
            WebDriver driver = new RemoteWebDriver(URI.create("http://localhost:4444/wd/hub").toURL(), chromeOptions);
            setWebDriver(driver);
        } else {
            Configuration.browserCapabilities = new ChromeOptions().addArguments("--remote-allow-origins=*");
            Configuration.browser = "Chrome";
        }
    }
}

