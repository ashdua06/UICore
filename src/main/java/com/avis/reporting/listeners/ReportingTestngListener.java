package com.avis.reporting.listeners;

import com.avis.constants.TestNGParams;
import com.avis.reporting.dataObjects.TCLevelInfoDTO;
import com.avis.reporting.extentReports.ExtentManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.util.HashMap;
import java.util.Set;

public class ReportingTestngListener extends ReportingCommonListener implements ITestListener {

    private static ThreadLocal<TCLevelInfoDTO> tcCompletionDTOThreadLocal = new ThreadLocal<>();
    private static ITestStatusListener testStatusListener = null;


    @Override
    public synchronized void onFinish(ITestContext context) {
        removeRetriedTestsFromTestNG(context);
        logFinishSuite();
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        TCLevelInfoDTO tcLevelInfoDTO = new TCLevelInfoDTO();
        String className = getSimpleClassName(result);
        String methodName = getSimpleMethodName(result);
        String packageName = getPackageNameFromTestMethod(result);
        //String description = getSimpleDescription(result);
        String description =getPackageNameFromTestMethod(result)+"_"+getSimpleClassName(result)+"_"+getSimpleMethodName(result);
        String dpsInfo = getCategoryName(result);
        tcCompletionDTOThreadLocal.set(tcLevelInfoDTO);
        tcCompletionDTOThreadLocal.get().withTcReportingCategory(dpsInfo);
        tcCompletionDTOThreadLocal.get().withDescription(description);
        tcCompletionDTOThreadLocal.get().withClassName(className).withMethodName(methodName).withPackageName(packageName);
        tcCompletionDTOThreadLocal.get().withParameters(result.getParameters());
        logStartTest(tcCompletionDTOThreadLocal.get());
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        if (testStatusListener != null)
            testStatusListener.performOnSuccess(result);
        tcCompletionDTOThreadLocal.get().withParameters(result.getParameters()!=null?result.getParameters():new Object[]{getSimpleMethodName(result)});
        setTCCompletionDTO(result);
        logSuccessTest(tcCompletionDTOThreadLocal.get());
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        if (testStatusListener != null)
            testStatusListener.performOnFailure(result);
        tcCompletionDTOThreadLocal.get().withParameters(result.getParameters()!=null?result.getParameters():new Object[]{getSimpleMethodName(result)});
        setTCCompletionDTO(result);
            logFailedTest(tcCompletionDTOThreadLocal.get());
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        tcCompletionDTOThreadLocal.get().withParameters(result.getParameters());
        setTCCompletionDTO(result);
        if (!ExtentManager.isRemoveRetriedTests()) {
            if (testStatusListener != null)
                testStatusListener.performOnSkip(result);
        }
        logSkippedTest(tcCompletionDTOThreadLocal.get());
    }

    private synchronized String getPackageNameFromTestMethod(ITestResult result) {
        String strList[] = result.getTestClass().getRealClass().getPackage().getName().split("\\.");
        return strList[strList.length - 1];
    }

    private synchronized String getSimpleClassName(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName();
    }

    private synchronized String getSimpleMethodName(ITestResult result) {
        return result.getName();
    }


    private synchronized String getSimpleDescription(ITestResult result) {
        ITestNGMethod testNGMethod=result.getMethod();
        Object[] parameter=result.getParameters();
        String description="";
        if(parameter!=null&&parameter[0] instanceof String )
            description=parameter[0].toString();
        return description;
    }


    private synchronized void removeRetriedTestsFromTestNG(ITestContext context) {
        Set<ITestResult> skippedTests = context.getSkippedTests().getAllResults();
        for (ITestResult temp : skippedTests) {
            ITestNGMethod method = temp.getMethod();

            if (context.getFailedTests().getResults(method).size() > 0) {
                skippedTests.remove(temp);
            } else {
                if (context.getPassedTests().getResults(method).size() > 0) {
                    skippedTests.remove(temp);
                }
            }
        }
    }

    public synchronized void setTCCompletionDTO(ITestResult result) {
        tcCompletionDTOThreadLocal.get().withTcReportingCategory(getCategoryName(result));
        tcCompletionDTOThreadLocal.get().withTcStatus(result.getStatus());
        tcCompletionDTOThreadLocal.get().withException(result.getThrowable());
    }

    private synchronized String getCategoryName(ITestResult result) {
        return "TOTAL";
    }



}
