package com.avis.constants;

import com.avis.exceptions.EnvConfigFileException;
import com.avis.utils.PropertyUtils;

public class EnvConfig {
    public static final String browser;
    public static final String environment;
    public static final String platform;
    public static final String Viewport;
    public static final String Brand;
    public static final String domain;
    public static final String language;
    public static final String incognito;
    public static final String UILiteFlag;
    static {
        try {
            PropertyUtils.getInstance().loadProperties("env.properties");
            environment=PropertyUtils.getInstance().getValue("environment",AvisConstants.Default_environment);
            platform = PropertyUtils.getInstance().getValue("platform",AvisConstants.Default_platform);
            Viewport = PropertyUtils.getInstance().getValue("Viewport",AvisConstants.Default_Viewport);
            Brand = PropertyUtils.getInstance().getValue("Brand",AvisConstants.Default_Brand);
            domain = PropertyUtils.getInstance().getValue("domain",AvisConstants.Default_domain);
            language = PropertyUtils.getInstance().getValue("language",AvisConstants.Default_language);
            browser = PropertyUtils.getInstance().getValue("browser",AvisConstants.Default_browser);
            incognito = PropertyUtils.getInstance().getValue("incognito",AvisConstants.Default_incognito);
            UILiteFlag=PropertyUtils.getInstance().getValue("UILiteFlag",AvisConstants.Default_UILiteFlag);
        }
        catch (Exception e){
            throw new EnvConfigFileException("Error in loading env.property file, make sure it exist in src/main/resources",e);
        }
    }

}
