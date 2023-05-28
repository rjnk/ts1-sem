package cz.zcu.kiv.matyasj.dp.utils.dates.correct;

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
 * Objects of this class represent utilities which care about date/time/dateAndTime operation
 * in whole application. BaseDateUtility are mainly used for parsing date objects to string and
 * string to date objects. BaseDateUtility use by default SimpleDateFormat.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Component
public class BaseDateUtility implements DateUtility {
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();
    /** Prepared date and time format */
    private DateFormat sdfDateAndTime;
    /** Prepared date format */
    private DateFormat sdfDate;
    /** Prepared time format */
    private DateFormat sdfTime;

    /**
     * Default BaseDateUtility constructor.
     *
     * @param propertyLoader Property loader for getting date formats from system properties.
     */
    @Autowired
    public BaseDateUtility(PropertyLoader propertyLoader) {
        sdfDateAndTime = new SimpleDateFormat(propertyLoader.getProperty("dateAndTimeFormat"));
        sdfDate = new SimpleDateFormat(propertyLoader.getProperty("dateFormat"));
        sdfTime = new SimpleDateFormat(propertyLoader.getProperty("timeFormat"));

        sdfDateAndTime.setLenient(false);
        sdfDate.setLenient(false);
        sdfTime.setLenient(false);
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
     * This method returns Date object from string representation given in
     * method parameter.
     *
     * @param stringDate String representation of date
     * @return Date object
     */
    @Override
    public Date stringToDate(String stringDate) {
        try {
            switch (stringDate.length()) {
                case 16:  // yyyy-MM-dd HH:mm
                    log.info("Parsing string representation of date to Date object");
                    return sdfDateAndTime.parse(stringDate);
                case 10:  // yyyy-MM-dd
                    log.info("Parsing string representation of date to Date object");
                    return sdfDate.parse(stringDate);
                case 5:  // HH:mm
                    log.info("Parsing string representation of date to Date object");
                    return sdfTime.parse(stringDate);
            }
        } catch (ParseException e) {
            log.error("Error during date parsing!", e);
        }
        return null;
    }
}
