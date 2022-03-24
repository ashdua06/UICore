package com.avis.core;


import com.avis.constants.TestNGParams;
import com.avis.reporting.extentReports.ExtentManager;
import com.avis.utils.PropertyUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import java.util.concurrent.TimeUnit;
@Log4j2
public class BrowserInstance {

    private String url;
    protected WebDriver webDriver;
    protected String browser;
    public BrowserWebDriverContainer<?> chrome;
    public BrowserWebDriverContainer<?> firefox;


    public BrowserInstance(String browser) {
        this.browser = browser;
    }

    private void initializeWebdriverContainer(BrowserWebDriverContainer<?>... rule){
        if (browser.equalsIgnoreCase("config")) {
            browser = getBrowser();
        }
        log.info("Initializing browser: " + browser);
        switch (browser.toLowerCase()) {
            case "chrome":
                initializeChromeContainer();
                break;
            case "firefox":
                initializeFirefoxContainer();
                  break;
            default:
                throw new RuntimeException("Invalid Browser Input. Valid Browsers are chrome, firefox, safari, edge");
        }
    }

    public void initializeChromeContainer(){
        chrome = new BrowserWebDriverContainer<>()
                .withCapabilities(new ChromeOptions().addArguments("--disable-dev-shm-usage"));
        chrome.start();
        webDriver = chrome.getWebDriver();
    }

    public void initializeFirefoxContainer(){
        firefox = new BrowserWebDriverContainer<>()
                .withCapabilities(new FirefoxOptions());
        firefox.start();
        webDriver = chrome.getWebDriver();
    }

    private void initializeDriver() {

        if (browser.equalsIgnoreCase("config")) {
            browser = getBrowser();
        }

        log.info("Initializing browser: " + browser);
        switch (browser.toLowerCase()) {
            case "chrome":
                initializeChrome();
                break;
            case "firefox":
                initializeFirefox();
                break;
            case "edge":
                initializeEdge();
                break;
            case "safari":
                initializeSafari();
                break;
            default:
                throw new RuntimeException("Invalid Browser Input. Valid Browsers are chrome, firefox, safari, edge");
        }
    }

    private void initializeChrome() {

        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();

        webDriver = new ChromeDriver(chromeOptions);
    }

    private void initializeFirefox() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        webDriver = new FirefoxDriver(firefoxOptions);
    }

    private void initializeEdge() {
        WebDriverManager.edgedriver().setup();
        webDriver = new EdgeDriver();
    }

    /**
     * INFO: Parallel tests is not possible locally on Safari due to restriction
     * https://developer.apple.com/documentation/webkit/about_webdriver_for_safari
     */
    private void initializeSafari() {
        if (getPlatform().toLowerCase().contains("mac")) {
            webDriver = new SafariDriver();
        } else {
            throw new RuntimeException("Safari is supported only on Mac Operating System.");
        }
    }

    /**
     * Configures the driver instance
     */
    protected void configureDriver() {
        log.info("Maximizing Browser and Setting Implicit Wait Timeout to :" + 30000L);
        webDriver.manage().deleteAllCookies();
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(30000L, TimeUnit.MILLISECONDS);
    }

    public void launchUrl(String url) {
        log.info("Launching URL: " + url);
        webDriver.get(url);
    }

    protected WebDriver getDriver() {
        return webDriver;
    }

    public void start(String url) {
        initializeDriver();
        configureDriver();
        launchUrl(url);
        ExtentManager.setDriver(webDriver);
    }

    public void startWebdriverContainer(String url){
        initializeWebdriverContainer();
        configureDriver();
        launchUrl(url);
        ExtentManager.setDriver(webDriver);
    }

    public String getBrowser(){
       return TestNGParams.browser;
    }
    public String getPlatform(){
            return TestNGParams.platform;
    }
    public void stopWebDriverContainers() {
        switch (browser.toLowerCase()) {
            case "chrome":
                chrome.stop();
                break;
            case "firefox":
                firefox.stop();
                break;
        }
    }
}

