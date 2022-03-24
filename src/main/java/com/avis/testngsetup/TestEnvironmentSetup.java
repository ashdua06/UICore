package com.avis.testngsetup;


public class TestEnvironmentSetup {

    //Values for now are regression, sanity, smoke
    public static final String TEST_TYPE = System.getProperty("TestingType", null);
    //Values can be methods, system, package, group, class
    public static final String SUITE_TYPE = System.getProperty("suiteType", "system");
    public static final int THREAD_COUNT = Integer.parseInt(System.getProperty("ThreadCount", "1"));


    public static void main(String[] args) throws Exception {
        CreateTestNGXml.createXml(TestEnvironmentSetup.SUITE_TYPE, TestEnvironmentSetup.TEST_TYPE, TestEnvironmentSetup.THREAD_COUNT, args[1], args[0]);
    }
}

