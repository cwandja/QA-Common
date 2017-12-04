package de.funke.qa.common.pageObjects;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import de.funke.qa.common.BasePage;
import de.funke.qa.common.data.Article;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.*;

public class ArticlePage extends BasePage{
    public Header header = Selenide.page(Header.class);

    public Footer footer = Selenide.page(Footer.class);

    public Main main = Selenide.page(Main.class);

    public ArticlePage goToArticlePage(Article article){
        ArticlePage articlePage = open("/" + article.getId(), ArticlePage.class);
        Assert.assertFalse(page404Element().isDisplayed());
        Assert.assertTrue(main.titleElt().getText().contains(article.getTitle()));
        return articlePage;
    }

}
