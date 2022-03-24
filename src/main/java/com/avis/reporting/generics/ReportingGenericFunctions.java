package com.avis.reporting.generics;

import com.avis.reporting.extentReports.ExtentManager;
import org.openqa.selenium.WebDriver;

public class ReportingGenericFunctions {
    public static void initExtentReporter(boolean removeRetriedTests, boolean addScreenshotsToReport, String reportName, String reportTitle) {
        ExtentManager.createInstance(reportTitle, removeRetriedTests, addScreenshotsToReport, reportName);

    }
}
