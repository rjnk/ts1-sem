package cz.zcu.kiv.matyasj.dp.utils.dates.error;

import cz.zcu.kiv.matyasj.dp.annotations.ErrorMethod;
import cz.zcu.kiv.matyasj.dp.utils.dates.DateUtility;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Objects of this class represent <b>ERROR</b> utilities which care about date/time/dateAndTime operation
 * in whole application. BaseDateUtility are mainly used for parsing date objects to string and
 * string to date objects. BaseDateUtility use by default SimpleDateFormat.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Component
public class E02DateUtility implements DateUtility {
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();
    /** Prepared date and time format */
    private DateFormat sdfDateAndTime;
    /** Prepared date format */
    private DateFormat sdfDate;
    /** Prepared time format */
    private DateFormat sdfTime;
    /** Application property loader */
    protected final PropertyLoader propertyLoader;

    /**
     * Default E02DateUtility constructor.
     *
     * @param propertyLoader Property loader for getting date formats from system properties.
     */
    @Autowired
    public E02DateUtility(PropertyLoader propertyLoader) {
        sdfDateAndTime = new SimpleDateFormat(propertyLoader.getProperty("dateAndTimeFormat"));
        sdfDate = new SimpleDateFormat(propertyLoader.getProperty("dateFormat"));
        sdfTime = new SimpleDateFormat(propertyLoader.getProperty("timeFormat"));
        this.propertyLoader = propertyLoader;
    }


    /**
     * This method returns date string representation of given Date object.
     *
     * @param date Date object
     * @return String representation of given Date object
     */
    @Override
    public String dateToString(Date date) {
        return sdfDateAndTime.format(date);
    }

    /**
     * DELIBERATE ERROR
     *
     * This method returns Date object from static string 2100-12-12 20:00, 2100-12-12 or 20:00
     * It depend on string date format in parameter
     *
     * @param stringDate String representation of date
     * @return Date object
     */
    @Override
    @ErrorMethod(errorMessage = "This method returns Date object from static string 2100-12-12 20:00, 2100-12-12 or 20:00")
    public Date stringToDate(String stringDate) {
        try {
            switch (stringDate.length()) {
                case 16:  // yyyy-MM-dd HH:mm
                    log.error(propertyLoader.getProperty("log.E02DateUtility.stringToDate"));
                    return sdfDateAndTime.parse("2100-12-12 20:00");
                case 10:  // yyyy-MM-dd
                    log.error(propertyLoader.getProperty("log.E02DateUtility.stringToDate"));
                    return sdfDate.parse("2100-12-12");
                case 5:  // HH:mm
                    log.error(propertyLoader.getProperty("log.E02DateUtility.stringToDate"));
                    return sdfTime.parse("20:00");
            }
        } catch (ParseException e) {
            log.error("Error during date parsing!", e);
        }
        return null;
    }
}
