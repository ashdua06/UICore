package com.avis.utils;

import java.io.*;
import java.util.Properties;
import com.avis.exceptions.ObjectRepositaryException;
import org.openqa.selenium.By;


public class ObjectRepository {

    private static ObjectRepository instance;
    private Properties properties;
    private ObjectRepository(){
        properties=new Properties();
    }

    public static synchronized ObjectRepository getInstance() {
        if (instance == null) {
            instance = new ObjectRepository();
        }
        return instance;
    }

    public Properties loadProperties(String fileName) {
        InputStream input;
        try {
            input =getClass().getClassLoader().getResourceAsStream(fileName);
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return properties;
    }

    public By getValue(String key) {
        try{By locator=null;
        String locatorProperty=properties.getProperty(key).trim();
        String locatorType = locatorProperty.split(":")[0];
        String locatorValue = locatorProperty.substring(locatorProperty.indexOf(":") + 1);
        switch (locatorType) {
            case "Id":
                locator = By.id(locatorValue);
                break;
            case "Name":
                locator = By.name(locatorValue);
                break;
            case "CssSelector":
                locator = By.cssSelector(locatorValue);
                break;
            case "LinkText":
                locator = By.linkText(locatorValue);
                break;
            case "PartialLinkText":
                locator = By.partialLinkText(locatorValue);
                break;
            case "TagName":
                locator = By.tagName(locatorValue);
                break;
            case "Xpath":
                locator = By.xpath(locatorValue);
                break;
            case "Class":
                locator = By.className(locatorValue);
                break;
        }
        return locator;
    } catch (Exception e) {
        throw new ObjectRepositaryException("Element not found in Object Repository",e);
    }
    }

}
