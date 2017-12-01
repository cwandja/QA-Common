package de.funke.qa.common;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$$;

public class BasePage extends Config{
    static final Logger logger = Logger.getLogger(BasePage.class);
    public ElementsCollection links() {
        ElementsCollection links = $$(By.tagName("a"));
        links.shouldBe(CollectionCondition.sizeGreaterThan(0));
        return links;
    }

}
