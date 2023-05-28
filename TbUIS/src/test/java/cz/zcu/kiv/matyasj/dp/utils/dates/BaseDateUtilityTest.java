package cz.zcu.kiv.matyasj.dp.utils.dates;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * BaseDateUtility test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class BaseDateUtilityTest {
    @Autowired
    DateUtility dateUtility;

    /**
     * Shared system logger
     */
    private final Logger log = LogManager.getLogger();

    /**
     * This method tests BaseDateUtility function - Creating date object from correct string (date and time)
     */
    @Test
    public void stringToDateTest1() throws Exception {
        log.info("Testing string to date conversion.");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr = "2017-09-16 08:30";
        Date date = dateUtility.stringToDate(dateStr);
        Date testDate = dateFormat.parse(dateStr);

        assertEquals(testDate, date);
    }

    /**
     * This method tests BaseDateUtility function - Creating date object from correct string (date)
     */
    @Test
    public void stringToDateTest2() throws Exception {
        log.info("Testing string to date conversion.");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = "2000-02-28";
        Date date = dateUtility.stringToDate(dateStr);
        Date testDate = dateFormat.parse(dateStr);

        assertEquals(testDate, date);
    }

    /**
     * This method tests BaseDateUtility function - Creating date object from correct string (time)
     */
    @Test
    public void stringToDateTest3() throws Exception {
        log.info("Testing string to date conversion.");

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String dateStr = "17:59";
        Date date = dateUtility.stringToDate(dateStr);
        Date testDate = dateFormat.parse(dateStr);

        assertEquals(testDate, date);
    }

    /**
     * This method tests BaseDateUtility function - Creating string from date object (now)
     */
    @Test
    public void dateToStringTest4() {
        log.info("Testing date to string conversion.");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();

        String actualDate = dateUtility.dateToString(now);
        String exceptedDate = dateFormat.format(now);
        assertEquals(exceptedDate, actualDate);
    }

    /**
     * This method tests BaseDateUtility function - Creating string from date object (date and time)
     */
    @Test
    public void dateToStringTest5() throws Exception {
        log.info("Testing date to string conversion.");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date testDate = dateFormat.parse("2017-09-16 08:30");
        String actualDate = dateUtility.dateToString(testDate);
        String exceptedDate = dateFormat.format(testDate);
        assertEquals(exceptedDate, actualDate);
    }

    /**
     * This method tests BaseDateUtility function - Creating date object from incorrect string ("Bad string format").
     */
    @Test
    public void stringToDateTestBadStringFormat() {
        log.info("Testing bad formatted string to date conversion.");

        Date actualDate = dateUtility.stringToDate("Bad string format");

        assertNull(actualDate);
    }

    /**
     * This method tests BaseDateUtility function - Creating date object from incorrect string ("01234").
     */
    @Test
    public void stringToDateTestBadStringFormatParseError() {
        log.info("Testing bad formatted string to date conversion.");

        Date actualDate = dateUtility.stringToDate("01234");

        assertNull(actualDate);
    }

}