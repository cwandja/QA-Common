package de.funke.qa.common.pageObjects;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class Header {
    public ElementsCollection menuRubrikList() {
        ElementsCollection rubrikList = $$("#nav-main ul.nav-main--major li.nav-main__item--level1");
        rubrikList.shouldBe(CollectionCondition.sizeGreaterThan(0));
        return rubrikList;
    }

    public List<String> rubrikPaths() {
        List<String> rubrikPaths = new ArrayList<String>();
        for (SelenideElement rubrik : menuRubrikList()) {
            String url = rubrik.find(By.tagName("a")).getAttribute("href");
            rubrikPaths.add(url);
        }
        return rubrikPaths;
    }

    public SelenideElement header(){

        return $("#article__header__heading");
    }
}
