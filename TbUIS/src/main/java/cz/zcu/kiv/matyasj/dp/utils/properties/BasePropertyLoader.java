package cz.zcu.kiv.matyasj.dp.utils.properties;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Objects of this class represent base property loader components used for
 * reading system property file (application.properties) and returning property
 * values.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Component("propertyLoader")
public class BasePropertyLoader implements PropertyLoader{
    /** java.util.Properties object for reading properties file in default format */
    private Properties prop;
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    private static final String mainPropertyFileName = "application.properties";

    /**
     * Default BasePropertyLoader constructor
     */
    public BasePropertyLoader() {
        this(mainPropertyFileName);
    }

    /**
     * BasePropertyLoader constructor with possibility to set property file by name.
     *
     * @param propertyFileName String representation of property file name
     */
    public BasePropertyLoader(String propertyFileName) {
        this.prop = new Properties();

        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream(propertyFileName);
            prop.load(input);
        } catch (IOException e) {
            log.error("Property file not found", e);
        }
    }

    /**
     * This method return property value (string) from system property file
     * by property name given in method parameter.
     *
     * @param propertyName Name of property
     * @return property value in String
     */
    @Override
    public String getProperty(String propertyName) {
        return prop.getProperty(propertyName);
    }

    /**
     * This method sets property value (string) in system property file
     * by property name with property value given in method parameter.
     *
     * @param propertyName  Name of property
     * @param propertyValue Value of property
     */
    @Override
    public void setProperty(String propertyName, String propertyValue) {
        prop.setProperty(propertyName, propertyValue);
    }

}
