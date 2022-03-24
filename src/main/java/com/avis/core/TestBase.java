package com.avis.core;

import com.avis.constants.AvisConstants;
import com.avis.constants.TestNGParams;
import com.avis.reporting.extentReports.ExtentManager;
import com.avis.reporting.generics.ReportingGenericFunctions;
import com.avis.reporting.listeners.AnnotationTransformer;
import com.avis.reporting.listeners.ReportingTestngListener;
import io.qameta.allure.Allure;
import lombok.extern.log4j.Log4j2;
import org.junit.Rule;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.Network;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Method;


@Log4j2
@Listeners({ ReportingTestngListener.class, AnnotationTransformer.class})
public class TestBase {

    protected static final String DEFAULT_PROVIDER = "defaultConfig";
    private final ThreadLocal<BrowserInstance> appInstance = new ThreadLocal<>();

    @BeforeSuite(alwaysRun = true)
    public void startTest(XmlTest xmlTest, ITestContext context) {
        TestNGParams.getInstance().setTestNgParameters(xmlTest,context);
        initExtentReporter();
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void beforeMethodTestBase(@Optional("config") String browser, Method method) {
        appInstance.set(new BrowserInstance(browser));
        log.info("beforeMethodTestBase() called");
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethodTestBase(ITestResult result) throws IOException {
        log.info("afterMethodTestBase() called");
        if (result.getStatus() == ITestResult.FAILURE) {
            Allure.addAttachment(result.getMethod().getMethodName(), new ByteArrayInputStream(((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES)));
        }
        getDriver().quit();
        getBrowserInstance().stopWebDriverContainers();
    }


    private BrowserInstance getBrowserInstance() {
        return appInstance.get();
    }

    public void launchUrl(String url) {
        getBrowserInstance().start(url);
    }

    public void launchUrlWithContainer(String url) {
        getBrowserInstance().startWebdriverContainer(url);
    }


    public WebDriver getDriver() {
        return getBrowserInstance().getDriver();
    }

    public synchronized void initExtentReporter() {
        ReportingGenericFunctions.initExtentReporter(AvisConstants.FLAG_REMOVE_RETRIEDTESTS, AvisConstants.FLAG_UPDATE_SCREENSHOTS, AvisConstants.FILE_NAME_REPORT, AvisConstants.REPORT_TITLE);
    }


}
