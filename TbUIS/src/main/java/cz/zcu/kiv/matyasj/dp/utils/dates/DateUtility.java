package cz.zcu.kiv.matyasj.dp.utils.dates;

import java.util.Date;

/**
 * This utility care about date and time in all application.
 * This interface defines methods for dealing with dates in the whole application.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public interface DateUtility {
    /**
     * This method returns date string representation of given Date object.
     *
     * @param date Date object
     * @return String representation of given Date object
     */
    String dateToString(Date date);

    /**
     * This method returns Date object from string representation given in
     * method parameter.
     *
     * @param stringDate String representation of date
     * @return Date object
     */
    Date stringToDate(String stringDate);
}
