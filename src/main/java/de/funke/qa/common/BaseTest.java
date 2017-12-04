package de.funke.qa.common;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import de.funke.qa.common.data.Article;
import de.funke.qa.common.enumeration.ArticleEnum;
import de.funke.qa.common.enumeration.Stage;
import de.funke.qa.common.pageObjects.RubrikPage;
import de.funke.qa.common.utilities.Helper;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;

public class BaseTest {
    public Stage stage = Stage.valueOf(System.getProperty(Helper.STAGE, Stage.PROD.toString()).toUpperCase());
    public List<Article> testArticles = new ArrayList<>();
    public Article nachrichtArticle = null;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        String pathToCommonProjectDir = System.getProperty("path.to.common.project.dir", "/Users/cwandja/projects/Common");
        System.setProperty("webdriver.chrome.driver", pathToCommonProjectDir + "/src/main/resources/chromedriver2");
        Configuration.browser = System.getProperty("selenide.browser", "chrome");
        WebDriver.Window window = WebDriverRunner.getWebDriver().manage().window();
        window.maximize();
        Dimension size = window.getSize();
        System.out.println("Display size = " + size.getWidth() + "x" + size.getHeight());

    }

    public List<Article> getTestArticles() {
        if (testArticles.isEmpty()) {
            String currentBaseUrl = Configuration.baseUrl;
            Configuration.baseUrl = Helper.getDefaultBaseUrl(stage);
            RubrikPage testRubrikPage = new RubrikPage();
            testRubrikPage.goToTestRubrikPage();
            testArticles = testRubrikPage.getArticles();
            Configuration.baseUrl = currentBaseUrl;
        }
        return testArticles;
    }

    public Article getNachrichtArticle() {
        if (nachrichtArticle == null) {
            nachrichtArticle = Helper.getArticle(getTestArticles(), ArticleEnum.NACHRICHT);
        }
        return nachrichtArticle;
    }

}
