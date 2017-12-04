package de.funke.qa.common.pageObjects;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;
public class Main {
    public SelenideElement headerElt() {
        return $("div.article__header");
    }

    public SelenideElement titleElt() {
        return headerElt().find(By.cssSelector(".article__header__headline"));
    }

    public SelenideElement socialBarElt() {
        return $("#socialbar");
    }
}
