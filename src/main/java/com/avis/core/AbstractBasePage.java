package com.avis.core;

import com.avis.utils.CommonUtils;
import com.avis.utils.ElementHelper;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;

@Log4j2
public abstract class AbstractBasePage {


    protected WebDriver driver;
    protected WebDriverWait wait;
    protected ElementHelper helper;
    public static final String CURRENT_DIR = System.getProperty("user.dir");
    private long DEFAULT_TIMEOUT=90;
    public static String timeout_value="500";

    public AbstractBasePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10);
        helper = new ElementHelper(driver);
        PageFactory.initElements(driver, this);
        isOnPage();
    }

    protected WebElement waitForVisibilityOfElement(WebElement element) {
        for (int i = 0; i < 2; i++) {
            try {
                wait.until(ExpectedConditions.visibilityOf(element));
                return element;
            } catch (Exception e) {
                if (i == 0) {
                    CommonUtils.threadSleep(1000);
                } else {
                    throw new RuntimeException(element + "Element is not found");
                }
            }
        }
        return element;
    }

    protected WebElement waitForVisibilityOfElement(WebElement element, int timeout) {
        return new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOf(element));
    }

    protected void switchToLatestWindow() {
        String parentWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!parentWindow.equals(window)) {
                driver.switchTo().window(window);
            }
        }
    }

    /**
     * Javascript Methods - clickUsingJS, clearTextUsingJS
     */
    protected void clickUsingJS(WebElement webElement) {
        log.info("Click using javascript");
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", webElement);
    }

    protected void clearTextUsingJS(WebElement webElement) {
        log.info("Clear text using javascript");
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].value = '';", webElement);
    }

    /**
     * Navigation Methods - pageRefresh, navigateBack, navigateForward
     */
    public void pageRefresh() {
        log.info("Page Refresh");
        driver.navigate().refresh();
    }

    public void navigateBack() {
        log.info("Navigate Back");
        driver.navigate().back();
    }

    public void navigateForward() {
        log.info("Navigate Forward");
        driver.navigate().forward();
    }


    /**
     * Every class should implement its own isOnPage which verifies the
     * screen is loaded.
     */
    public abstract void isOnPage();
}
