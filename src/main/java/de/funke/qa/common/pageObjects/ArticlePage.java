package de.funke.qa.common.pageObjects;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import de.funke.qa.common.BasePage;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.*;

public class ArticlePage extends BasePage{
    public Header header = Selenide.page(Header.class);

    public Footer footer = Selenide.page(Footer.class);

    public Main main = Selenide.page(Main.class);

    public SelenideElement page404Element(){
        return $("#page__404__heading");
    }

    public ArticlePage goToArticlePage(String id){
        ArticlePage articlePage = open(id, ArticlePage.class);
        if(page404Element().isDisplayed())
        Assert.assertFalse(true);
        return articlePage;
    }

}
