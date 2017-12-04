package de.funke.qa.common.pageObjects;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;
public class Main {
    public SelenideElement titleElement() {
        return $("#article__header__heading");
    }

    public SelenideElement socialBarElement() {
        return $("#socialbar");
    }
}
