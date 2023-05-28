package cz.zcu.kiv.matyasj.dp.utils.properties;

/**
 * This interface defines method for getting system property attributes from system property file.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public interface PropertyLoader {
    /**
     * This method return property value (string) from system property file
     * by property name given in method parameter.
     *
     * @param propertyName Name of property
     * @return property value in String
     */
    String getProperty(String propertyName);

    /**
     * This method sets property value (string) in system property file
     * by property name with property value given in method parameter.
     *
     * @param propertyName  Name of property
     * @param propertyValue Value of property
     */
    void setProperty(String propertyName, String propertyValue);

}
