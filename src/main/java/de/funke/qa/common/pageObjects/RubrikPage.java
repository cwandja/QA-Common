package de.funke.qa.common.pageObjects;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import de.funke.qa.common.BasePage;
import de.funke.qa.common.utilities.Helper;
import de.funke.qa.common.data.Article;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;

public class RubrikPage extends BasePage {
    static final Logger logger = Logger.getLogger(RubrikPage.class);
    public static String ARTICLE_TAGNAME = "article";

    public Header header = Selenide.page(Header.class);

    public Footer footer = Selenide.page(Footer.class);

    public Main main = Selenide.page(Main.class);

    public RubrikPage goToRubrikPage(String rubrikPage) {
        if (StringUtils.isNotEmpty(rubrikPage))
            return open(rubrikPage, RubrikPage.class);
        return open(Helper.INTEGRATIONTEST_RUBRIK, RubrikPage.class);
    }

    public RubrikPage goToTestRubrikPage() {
        return goToRubrikPage(null);
    }

    public class ArticleEntry {
        private SelenideElement entry;

        private ArticleEntry(SelenideElement entry) {
            this.entry = entry;
        }

        public String title() {
            return link().getAttribute("title");
        }


        public SelenideElement link() {
            return entry.find(By.tagName("a"));
        }

        public String id() {
            return entry.getAttribute("data-article-id");
        }

        public Article asArticle() {
            return new Article(id(), title(), link().getAttribute("href"));
        }

    }

    public List<ArticleEntry> articles() {
        return $$(By.tagName(ARTICLE_TAGNAME)).stream().map(ArticleEntry::new).collect(Collectors.toList());
    }

    public Article getFirstArticle() {
        return new ArticleEntry($(By.tagName(ARTICLE_TAGNAME))).asArticle();

    }

    public List<Article> getArticles() {
        List<ArticleEntry> articleEntries = articles();
        List<Article> articles = new ArrayList<>();
        Assert.assertFalse(articleEntries.isEmpty());
        for (ArticleEntry entry : articleEntries) {
            articles.add(entry.asArticle());
        }
        return articles;
    }

    public Article getArticleByTitle(String title) {
        List<ArticleEntry> list = articles();
        Assert.assertFalse(list.isEmpty());
        if (StringUtils.isNotBlank(title)) {
            for (ArticleEntry entry : list) {
                if (entry.title().equals(title)) {
                    return entry.asArticle();
                }
            }
        }
        return null;
    }
}
