package de.funke.utilities;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import de.funke.enumeration.Publication;
import de.funke.listener.TestEnvironmentListener;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

import java.util.Arrays;

public class Base {
    static final Logger logger = Logger.getLogger(Base.class);

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
       System.setProperty("webdriver.chrome.driver", "/Users/cwandja/projects/Testautomation/src/test/resources/chromedriver2");
       Configuration.browser = System.getProperty("selenide.browser", "chrome");
       WebDriver.Window window = WebDriverRunner.getWebDriver().manage().window();
       window.maximize();
       Dimension size = window.getSize();
       System.out.println("Display size = " + size.getWidth() + "x" + size.getHeight());
    }

    @BeforeTest(alwaysRun = true)
    public void beforeTest() {

    }

}
