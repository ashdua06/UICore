package com.avis.reporting.extentReports;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import com.avis.constants.AvisConstants;
import com.avis.constants.TestNGParams;
import com.avis.reporting.dataObjects.TCLevelInfoDTO;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class ExtentManager {
    static Date date = new Date();
    static String timestamp = new SimpleDateFormat("dd_MM_yyyy_hh_mm").format(date);
    private static ExtentReports extent = null;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static boolean removeRetriedTests;
    private static boolean addScreenshotsToReport;
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<LinkedList<String>> logTracking = new ThreadLocal<>();
    private static ThreadLocal<LinkedList<Markup>> extentTestBefore = new ThreadLocal<>();
    private static String extentReportLocation;
    private static final String OUTPUT_FOLDER_SCREENSHOTS = "screenshots/";

    public ExtentManager() {
    }

    public static ThreadLocal<LinkedList<Markup>> getExtentTestBefore() {
        return extentTestBefore;
    }

    public static void setExtentTestBefore(Markup log) {
        if (extentTestBefore.get() == null) {
            LinkedList<Markup> logList = new LinkedList<>();
            getExtentTestBefore().set(logList);
        }
        getExtentTestBefore().get().add(log);

    }

    public synchronized static ThreadLocal<ExtentTest> getTest() {
        return test;
    }

    public synchronized static ThreadLocal<LinkedList<String>> getLog() {
        return logTracking;
    }

    public synchronized static Logger getLogger() {
        return logger;
    }

    public synchronized static void setTest(ExtentTest test) {
        getTest().set(test);
    }

    public synchronized static void setLog() {
        LinkedList<String> logs = new LinkedList<>();
        getLog().set(logs);
    }

    public static String getExtentReportLocation() {
        return extentReportLocation;
    }

    public static void setExtentReportLocation(String extentReportLocation) {
        ExtentManager.extentReportLocation = extentReportLocation;
    }
    public static String reportPath() {

        String path = AvisConstants.REPORT_BASE_PATH + TestNGParams.Brand + "_" + TestNGParams.domain + "_" + TestNGParams.Viewport + "_"
                + TestNGParams.environment + "_Automation_Report_" + timestamp + ".html";

        return path;
    }

    public synchronized static ExtentReports createInstance(String documentTitle, boolean removeRetriedTests, boolean addScreenshotsToReport, String reportName) {
        setRemoveRetriedTests(removeRetriedTests);
        setAddScreenshotsToReport(addScreenshotsToReport);
        setExtentReportLocation(reportPath());
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(getExtentReportLocation());
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setTimelineEnabled(true);
        sparkReporter.config().setReportName(documentTitle);
        sparkReporter.config().setTimelineEnabled(true);
        sparkReporter.config().setDocumentTitle(documentTitle);
        sparkReporter.config().setEncoding("utf-8");
        sparkReporter.config().setTimeStampFormat("MM/dd/yyyy, hh:mm:ss a '('zzz')'");
        sparkReporter.viewConfigurer()
                .viewOrder()
                .as(new ViewName[]{ViewName.DASHBOARD, ViewName.TEST, ViewName.AUTHOR, ViewName.CATEGORY, ViewName.EXCEPTION, ViewName.LOG})
                .apply();
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        return extent;
    }

    private static synchronized void writeLog4j() {
        if (logTracking.get() != null) {
            for (String message : logTracking.get()) {
                logger.info(message);
            }
        }
    }

    public synchronized static void deleteCurrentTest() {
        try {
            extent.removeTest(getTest().get());
        } catch (Exception e) {
            System.out.println("XXXXX Unable to Delete Extent Test XXXXX");
        }
    }

    public synchronized static void flush() {
        extent.flush();
        writeLog4j();
        if (getTest().get() != null) {
            getTest().remove();
        }
        if (getLog().get() != null) {
            getLog().remove();
        }
    }

    public synchronized static void setPassPercentage(String value) {
        extent.setSystemInfo("Pass %", MarkupHelper.createLabel(value, ExtentColor.GREEN).getMarkup());
        flush();
    }

    public synchronized static void addSystemInfo(String key, String value) {
        extent.setSystemInfo(key, value);
        flush();
    }

    public synchronized static void setTestRunnerOutput(String log) {
        extent.addTestRunnerOutput(log);
    }

    public synchronized static void createTest(String testName, String description) {
        setTest(extent.createTest(testName, description));
        setLog();
        logTracking.get().add("************" + testName + "************");
        if (getExtentTestBefore().get() != null && getExtentTestBefore().get().size() > 0) {
            for (Markup log : getExtentTestBefore().get()) {
                getTest().get().log(Status.INFO, log);
            }
            getExtentTestBefore().remove();
        }
    }

    public synchronized static boolean isRemoveRetriedTests() {
        return removeRetriedTests;
    }

    public synchronized static void setRemoveRetriedTests(boolean removeRetriedTests) {
        ExtentManager.removeRetriedTests = removeRetriedTests;
    }

    public synchronized static boolean isAddScreenshotsToReport() {
        return addScreenshotsToReport;
    }

    public synchronized static void setAddScreenshotsToReport(boolean addScreenshotsToReport) {
        ExtentManager.addScreenshotsToReport = addScreenshotsToReport;
    }

    public synchronized static WebDriver getDriver() {
        return ExtentManager.driver.get();
    }

    public synchronized static void setDriver(WebDriver driver) {
        ExtentManager.driver.set(driver);
    }

    public synchronized static void addGroupNamesSystemInfo(String key, String value) {
        ExtentManager.addSystemInfo(key, getFomattedGroupNames(value));
        flush();
    }

    private synchronized static String getFomattedGroupNames(String groupNames) {
        String finalStr = "";
        for (String str : groupNames.split(",")) {
            if (str.contains(".")) {
                finalStr = finalStr + str.split("\\.")[1] + "<br>";
            } else {
                finalStr = finalStr + str + "<br>";
            }
        }
        return finalStr;
    }

    private synchronized static String takeScreenshot(WebDriver driver, String methodName) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("MMM_dd_yyyy_HH_mm_ss_SSS");
        Date date = new Date();
        String dateName = dateFormat.format(date);
        String filePath="";
        String filePathExtent = OUTPUT_FOLDER_SCREENSHOTS + methodName + "_" + dateName + ".png";

        filePath = AvisConstants.REPORT_BASE_PATH + filePathExtent;

        File f = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(f, new File(filePath));
        System.out.println("root screenshot path is "+filePath+" root screenshot exists"+new File(filePath).exists());
        return filePathExtent;
    }

    public synchronized static void addScreenShotToReport(TCLevelInfoDTO tcLevelInfoDTO) {
        if (ExtentManager.isAddScreenshotsToReport()) {
            try {
                String screenshotPath="";

                screenshotPath= Paths.get(AvisConstants.REPORT_BASE_PATH+takeScreenshot(ExtentManager.getDriver(), tcLevelInfoDTO.getMethodName())).toAbsolutePath().toString();

                System.out.println("Scrrenshot path is "+screenshotPath);
                ExtentManager.getTest().get().addScreenCaptureFromPath(screenshotPath);
                System.out.println("screenshot added successfully");
            } catch (Exception e) {
                System.out.println("XXXXX Unable to Capture Screenshot XXXXX");
                e.printStackTrace();
            }
        }
    }

}
