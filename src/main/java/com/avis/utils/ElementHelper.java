package com.avis.utils;

import com.avis.constants.AvisConstants;
import com.avis.reporting.extentReports.Logger;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class ElementHelper {

    private WebDriver driver;
    private String elementName="default";
    private String pageName="default";
    private By by;
    private WebElement webElement;
    JavascriptExecutor js;

    public ElementHelper(WebDriver driver) {
        this.driver = driver;
    }
    public ElementHelper(WebElement webElement, String pageName, String elementName) {
        this.webElement = webElement;
        this.pageName = pageName;
        this.elementName = elementName;
        this.js = (JavascriptExecutor) driver;
    }
    public ElementHelper(WebElement webElement, String pageName) {
        this.webElement = webElement;
        this.pageName = pageName;
        this.js = (JavascriptExecutor) driver;
    }

    public String getElementColorCode(WebElement webElement) {
        String colorCode = webElement.getCssValue("color");
        return Color.fromString(colorCode).asHex();
    }
    public void click(){
        Logger.logInfo("Click [" + webElement + "] on [" + pageName + "]");
        webElement.click();
    }
    public void sendKeys(CharSequence... keysToSend) {
        Logger.logInfo(
                "Enter text [" + Arrays.toString(keysToSend) + "] in [" + elementName + "] on [" + pageName + "]");
        webElement.sendKeys(keysToSend);
    }
    public void submit() {
        Logger.logInfo("Click [" + elementName + "] on [" + pageName + "] to submit");
        webElement.submit();
    }
    public String getText() {
        return webElement.getText();
    }
    public boolean isEnabled() {
        Logger.logInfo("Assert [" + elementName + " is enabled on [" + pageName + "]");
        return webElement.isEnabled();
    }

    public boolean isDisplayed() {
        Logger.logInfo("Assert [" + elementName + " is displayed on [" + pageName + "]");
        return webElement.isDisplayed();
    }

    public void clear() {
        Logger.logInfo("Clear text in [" + elementName + "] on [" + pageName + "]");
        webElement.clear();
    }


    public void clearTextUsingRepeatedBackspace(WebElement webElement, int repeatCount) {
        String backspace = Keys.BACK_SPACE.toString();
        webElement.sendKeys(StringUtils.repeat(backspace, repeatCount));
    }

    public void selectValueFromDropDown(WebElement dropDownElement, String dropDownValue) {
        Select dropDown = new Select(dropDownElement);
        dropDown.selectByVisibleText(dropDownValue);
    }

    public String getValueFromDropDown(WebElement dropDownElement, int optionIndex) {
        Select dropDown = new Select(dropDownElement);
        List<WebElement> options = dropDown.getOptions();
        return options.get(optionIndex).getText();
    }

    public boolean isElementDisplayed(WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, AvisConstants.DEFAULT_EXPLICIT_TIMEOUT);
            wait.until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            return false;
        }
    }

    public boolean isImageDisplayed(WebElement element) {
        try {
            Logger.logInfo("Assert [" + elementName + " image is displayed on [" + pageName + "]");
            WebDriverWait wait = new WebDriverWait(driver, AvisConstants.DEFAULT_EXPLICIT_TIMEOUT);
            wait.until(ExpectedConditions.visibilityOf(element));
            return (Boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", element);
        } catch (NoSuchElementException | TimeoutException e) {
            return false;
        }
    }

    /**
     * Wait for the element to disappear from the ui
     *
     * @return boolean false if element is not available in the page else will return true
     */
    public boolean isElementDisappeared(WebElement element) {

        try {
            Logger.logInfo("Assert [" + elementName + "image is disppeared from [" + pageName + "]");
            WebDriverWait wait = new WebDriverWait(driver, AvisConstants.DEFAULT_EXPLICIT_TIMEOUT);
            wait.until(ExpectedConditions.invisibilityOf(element));
            return element.isDisplayed();
        } catch (TimeoutException | StaleElementReferenceException | NoSuchElementException e) {
            return false;
        }
    }

    public List<String> getAllOptionsFromDropDown(WebElement dropDownElement) {
        Select dropDown = new Select(dropDownElement);
        List<WebElement> allOptions = dropDown.getOptions();

        // Excluding the first option from the list
        allOptions.remove(dropDown.getFirstSelectedOption());

        List<String> allDropDownValues = new ArrayList<>();
        for (WebElement ele : allOptions) {
            allDropDownValues.add(ele.getText());
        }
        return allDropDownValues;
    }

    public List<String> getAllElementsText(List<WebElement> webElements) {
        List<String> elementsText = new ArrayList<>();

        for (WebElement we : webElements) {
            elementsText.add(we.getText());
        }
        return elementsText;
    }

    public void mouseHover(WebElement webElement) {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).build().perform();
    }
}
