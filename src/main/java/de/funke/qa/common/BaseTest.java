package de.funke.qa.common;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import de.funke.qa.common.data.Article;
import de.funke.qa.common.enumeration.Stage;
import de.funke.qa.common.pageObjects.RubrikPage;
import de.funke.qa.common.utilities.Helper;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;

public class BaseTest extends Config {
    static final Logger logger = Logger.getLogger(BaseTest.class);
    public List<Article> articles = new ArrayList<>();

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        String pathToCommonProjectDir = System.getProperty("path.to.common.project.dir", "/Users/cwandja/projects/Common");
        System.setProperty("webdriver.chrome.driver", pathToCommonProjectDir + "/src/main/resources/chromedriver2");
        Configuration.browser = System.getProperty("selenide.browser", "chrome");

        RubrikPage rubrikPage = new RubrikPage();
        rubrikPage.goToRubrikPage();
        articles = rubrikPage.getArticles();

        WebDriver.Window window = WebDriverRunner.getWebDriver().manage().window();
        window.maximize();
        Dimension size = window.getSize();
        System.out.println("Display size = " + size.getWidth() + "x" + size.getHeight());

    }


}
