package com.avis.constants;

import lombok.Getter;
import lombok.Setter;
import org.testng.ITestContext;
import org.testng.xml.XmlTest;

import java.util.HashMap;
import java.util.Map;

public class TestNGParams {
    public static String Viewport;
    public static String environment;
    public static String platform;
    public static String Brand;
    public static String domain;
    public static String language;
    public static String incognito;
    public static String browser;
    public static String UILiteFlag;
    public static Map<String, String> XMLParamsMap;
    public static Map<String ,String > systemInfo;
    private static TestNGParams instance;
    private TestNGParams(){
    }

    public static synchronized TestNGParams getInstance() {
        if (instance == null) {
            instance = new TestNGParams();
        }
        return instance;
    }

    public void setTestNgParameters(XmlTest xmlTest, ITestContext context) {
        XMLParamsMap = xmlTest.getAllParameters();
        // Assigning all the XML parameters to the Base Class Global variables
        Brand=XMLParamsMap.get("Brand") != null?XMLParamsMap.get("Brand"):EnvConfig.Brand;
        platform=XMLParamsMap.get("platform") != null?XMLParamsMap.get("platform"):EnvConfig.platform;
        environment=XMLParamsMap.get("environment") != null?XMLParamsMap.get("environment"):EnvConfig.environment;
        domain=XMLParamsMap.get("domain") != null?XMLParamsMap.get("domain"):EnvConfig.domain;
        language=XMLParamsMap.get("language") != null?XMLParamsMap.get("language"):EnvConfig.language;
        Viewport=XMLParamsMap.get("Viewport") != null?XMLParamsMap.get("Viewport"):EnvConfig.Viewport;
        incognito=XMLParamsMap.get("incognito") != null?XMLParamsMap.get("incognito"):EnvConfig.incognito;
        browser=XMLParamsMap.get("browser") != null?XMLParamsMap.get("browser"):EnvConfig.browser;
        UILiteFlag=XMLParamsMap.get("UILiteFlag") != null?XMLParamsMap.get("UILiteFlag"):EnvConfig.UILiteFlag;
        setSystemInfo();
    }

    public void setSystemInfo(){
        systemInfo=new HashMap<>();
        systemInfo.put("Browser",browser);
        systemInfo.put("Brand",Brand);
        systemInfo.put("Platform",platform);
        systemInfo.put("Environment",environment);
        systemInfo.put("Domain",domain);
        systemInfo.put("Language",language);
        systemInfo.put("Viewport",Viewport);
        systemInfo.put("Incognito",incognito);
        systemInfo.put("UILiteFlag",UILiteFlag);



    }
}
