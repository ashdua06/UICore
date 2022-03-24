package com.avis.reporting.listeners;


import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.avis.constants.TestNGParams;
import com.avis.reporting.dataObjects.TCLevelInfoDTO;
import com.avis.reporting.enums.TCStatus;
import com.avis.reporting.extentReports.ExtentManager;
import com.avis.reporting.extentReports.Logger;
import org.testng.Reporter;
import java.util.HashMap;
import java.util.Map;

public class ReportingCommonListener {

    public synchronized void logFinishSuite() {
        addExecutionDetails_Extent(TestNGParams.systemInfo);
        addConsoleLogsToReport();
    }

    public synchronized void logStartTest(TCLevelInfoDTO tcLevelInfoDTO) {
        ExtentManager.createTest(getMethodNameWithParams(tcLevelInfoDTO), getTestDescription(tcLevelInfoDTO));
     // Add parameter will use when you want to print all parameters in extent report
           addParametersInReport(tcLevelInfoDTO);
    }


    public synchronized void logFailedTest(TCLevelInfoDTO tcLevelInfoDTO) {
        logStatusToConsole(tcLevelInfoDTO);
        assignCategoryToTest(tcLevelInfoDTO);
        assignExceptionToTest(tcLevelInfoDTO);
        ExtentManager.addScreenShotToReport(tcLevelInfoDTO);
        addExtentLabelToTest(tcLevelInfoDTO);
        ExtentManager.flush();
    }

    public synchronized void logSkippedTest(TCLevelInfoDTO tcLevelInfoDTO) {
        if (ExtentManager.isRemoveRetriedTests()) {
            removeRetriedTest(tcLevelInfoDTO);
        } else {
            logStatusToConsole(tcLevelInfoDTO);
            assignCategoryToTest(tcLevelInfoDTO);
            assignExceptionToTest(tcLevelInfoDTO);
            ExtentManager.addScreenShotToReport(tcLevelInfoDTO);
            addExtentLabelToTest(tcLevelInfoDTO);
            ExtentManager.flush();
        }
    }

    public synchronized void logSuccessTest(TCLevelInfoDTO tcLevelInfoDTO) {
        logStatusToConsole(tcLevelInfoDTO);
        assignCategoryToTest(tcLevelInfoDTO);
        addExtentLabelToTest(tcLevelInfoDTO);
        ExtentManager.flush();
    }


    private void addExecutionDetails_Extent(Map<String, String> executionProperties) {
        for (String key : executionProperties.keySet()) {
            ExtentManager.addSystemInfo(key, executionProperties.get(key));
        }
    }

    protected synchronized void addConsoleLogsToReport() {
        for (String s : Reporter.getOutput()) {
            ExtentManager.setTestRunnerOutput(s + System.lineSeparator());
        }
    }


//    private synchronized void addParametersInReport(TCLevelInfoDTO tcLevelInfoDTO) {
//        if (tcLevelInfoDTO.getParameters() != null && tcLevelInfoDTO.getParameters().length > 0 && tcLevelInfoDTO.getParameters()[0] instanceof HashMap) {
//            Logger.logPass(MarkupHelper.createTable(getParameterArray((HashMap<String, String>) tcLevelInfoDTO.getParameters()[0])));
//        }
//    }

    private synchronized void addParametersInReport(TCLevelInfoDTO tcLevelInfoDTO) {
        if (tcLevelInfoDTO.getParameters() != null && tcLevelInfoDTO.getParameters().length > 0) {
            Logger.logInfo(MarkupHelper.createTable(getParameterArray(tcLevelInfoDTO.getParameters())));
        }
    }

    private static synchronized String[][] getParameterArray(Object[] hm) {
        String[][] parameters = new String[hm.length][1];
        int row = 0;
        int column = 0;
        for (Object str : hm) {
            if(str instanceof String) {
                parameters[row][column] = "<b>" + str + "</b>";
                row++;
            }
        }
        return parameters;
    }

    private static synchronized String getParameterArrayAsString(Object[] hm) {
        String parameters="";
        for (Object str : hm) {
            if(str instanceof String) {
                parameters = parameters+"<b>" + str + "</b><br>";
            }
        }
        return parameters;
    }

    protected synchronized String getMethodNameWithParams(TCLevelInfoDTO tcLevelInfoDTO) {
        String methodName = tcLevelInfoDTO.getMethodName();
//        if (tcLevelInfoDTO.getParameters() != null && tcLevelInfoDTO.getParameters().length > 0) {
//            methodName = methodName + " [" + tcLevelInfoDTO.getParameters()[0].toString() + "]";
//        }

        return methodName;
    }

    private synchronized String getTestDescription(TCLevelInfoDTO tcLevelInfoDTO) {
        String description = tcLevelInfoDTO.getDescription();
        String nextLineCharacter = "<br>";
        if (tcLevelInfoDTO.getParameters() != null && tcLevelInfoDTO.getParameters().length > 0) {
            description = description + nextLineCharacter + getParameterArrayAsString(tcLevelInfoDTO.getParameters());
        }
        return description;
    }

    private synchronized void logStatusToConsole(TCLevelInfoDTO tcLevelInfoDTO) {
        String status = "";
        if (tcLevelInfoDTO.getTcStatus() == TCStatus.SUCCESS)
            status = "Pass";

        else if (tcLevelInfoDTO.getTcStatus() == TCStatus.FAILURE)
            status = "Fail";

        else if (tcLevelInfoDTO.getTcStatus() == TCStatus.SKIP)
            status = "Skip";


        System.out.println(tcLevelInfoDTO.getMethodName() + " = [" + status + "]" + System.lineSeparator());
        Logger.logInfoInLogger(tcLevelInfoDTO.getMethodName() + " = [" + status + "]<br>");
    }

    private synchronized void assignCategoryToTest(TCLevelInfoDTO tcLevelInfoDTO) {
        if (tcLevelInfoDTO.getParameters() != null && tcLevelInfoDTO.getParameters().length > 0) {
            if(tcLevelInfoDTO.getTcReportingCategory()!=null) {
             //   ExtentManager.getTest().get().assignCategory(tcLevelInfoDTO.getTcReportingCategory(), tcLevelInfoDTO.getParameters()[0].toString());
                ExtentManager.getTest().get().assignCategory(tcLevelInfoDTO.getTcReportingCategory(), tcLevelInfoDTO.getPackageName());

            }else {
                ExtentManager.getTest().get().assignCategory(tcLevelInfoDTO.getClassName(),  tcLevelInfoDTO.getPackageName());
            }
        } else {
            if(tcLevelInfoDTO.getTcReportingCategory()!=null) {
                ExtentManager.getTest().get().assignCategory(tcLevelInfoDTO.getTcReportingCategory(),tcLevelInfoDTO.getPackageName());
            }else{
                ExtentManager.getTest().get().assignCategory(tcLevelInfoDTO.getClassName());
            }
        }
    }

    private synchronized void assignExceptionToTest(TCLevelInfoDTO tcLevelInfoDTO) {
        if (tcLevelInfoDTO.getException() != null) {
            Logger.logFail(tcLevelInfoDTO.getException());
        }
    }

    private synchronized void addExtentLabelToTest(TCLevelInfoDTO tcLevelInfoDTO) {
        if (tcLevelInfoDTO.getTcStatus() == TCStatus.SUCCESS)
            ExtentManager.getTest().get().pass(MarkupHelper.createLabel("Test Passed", ExtentColor.GREEN));

        else if (tcLevelInfoDTO.getTcStatus() == TCStatus.FAILURE) {
                ExtentManager.getTest().get().fail(MarkupHelper.createLabel("Test Failed", ExtentColor.RED));
        } else
            ExtentManager.getTest().get().skip(MarkupHelper.createLabel("Test Skipped", ExtentColor.ORANGE));

    }

    public synchronized void removeRetriedTest(TCLevelInfoDTO tcLevelInfoDTO) {
        logRetryStatusToConsole(tcLevelInfoDTO);
        deleteCurrentTestFromReport();
        ExtentManager.flush();
    }

    private synchronized void logRetryStatusToConsole(TCLevelInfoDTO tcLevelInfoDTO) {
        Logger.logInfoInLogger("-------- Retry = " + tcLevelInfoDTO.getMethodName() + "--------<br>");
        System.out.println("-------- Retry = " + tcLevelInfoDTO.getMethodName() + "--------" + System.lineSeparator());
    }


    protected synchronized void deleteCurrentTestFromReport() {
        ExtentManager.deleteCurrentTest();
    }

}