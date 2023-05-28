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
public class E01DateUtility implements DateUtility {
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
     * Default E01DateUtility constructor.
     *
     * @param propertyLoader Property loader for getting date formats from system properties.
     */
    @Autowired
    public E01DateUtility(PropertyLoader propertyLoader) {
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
     * This method returns Date object from string representation given in
     * method parameter + 1 Day.
     *
     * @param stringDate String representation of date
     * @return Date object
     */
    @Override
    @ErrorMethod(errorMessage = "This method returns Date object from string representation given in method parameter + 1 Day.")
    public Date stringToDate(String stringDate) {
        Date date;
        try {
            switch (stringDate.length()) {
                case 16:  // yyyy-MM-dd HH:mm
                    date = sdfDateAndTime.parse(stringDate);
                    date = new Date(date.getTime() + 86400000);
                    log.error(propertyLoader.getProperty("log.E01DateUtility.stringToDate"));
                    return date;
                case 10:  // yyyy-MM-dd
                    date = sdfDate.parse(stringDate);
                    date = new Date(date.getTime() + 86400000);
                    log.error(propertyLoader.getProperty("log.E01DateUtility.stringToDate"));
                    return date;
                case 5:  // HH:mm
                    date = sdfTime.parse(stringDate);
                    date = new Date(date.getTime() + 86400000);
                    log.error(propertyLoader.getProperty("log.E01DateUtility.stringToDate"));
                    return date;
            }
        } catch (ParseException e) {
            log.error("Error during date parsing!", e);
        }
        return null;
    }
}
