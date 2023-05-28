package cz.cvut.rejnek.sem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.zcu.kiv.matyasj.dp.utils.dates.DateUtility;
import cz.zcu.kiv.matyasj.dp.utils.dates.correct.BaseDateUtility;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class DateUtilityTest {
    private DateUtility utility;
    private PropertyLoader propertyLoader;

    @Before
    public void setUp() {
        propertyLoader = mock(PropertyLoader.class);
        when(propertyLoader.getProperty("dateAndTimeFormat")).thenReturn("yyyy-MM-dd HH:mm");
        when(propertyLoader.getProperty("dateFormat")).thenReturn("yyyy-MM-dd");
        when(propertyLoader.getProperty("timeFormat")).thenReturn("HH:mm:ss");
        utility = new BaseDateUtility(propertyLoader);
    }

    @Test
    public void stringToDate_normalDate_string() {
        Calendar cal = Calendar.getInstance();
        cal.set(2020, Calendar.JANUARY, 22, 14, 15,0);

        assertEquals("2020-01-22 14:15", utility.dateToString(cal.getTime()));
    }

    @Test
    public void stringToDate_full_date() throws ParseException {
        String stringDate = "2023-05-23 10:30";
        Date expectedDate = new SimpleDateFormat(propertyLoader.getProperty("dateAndTimeFormat")).parse(stringDate);
        Date result = utility.stringToDate(stringDate);

        assertEquals(expectedDate, result);
    }

    // tento test odhalil bug/feature v produkcni verzi
    // originalni verze si mesic prepocita
    @Test
    public void stringToDate_invalidMonth_null() {
        String stringDate = "2023-40-05 10:30";
        assertNull(utility.stringToDate(stringDate));
    }

    // tento test odhalil pad v produkcni verzi
    @Test
    public void stringToDate_hugeNumber_null() {
        String stringDate = "9999-99-99 99:99";
        assertNull(utility.stringToDate(stringDate));
    }

    @Test
    public void stringToDate_invalidFormat_null() {
        String stringDate = "2023.05.23";
        assertNull(utility.stringToDate(stringDate));
    }

    @Test
    public void stringToDate_notADate_null() {
        String stringDate = "ahoj :))";
        assertNull(utility.stringToDate(stringDate));
    }
}
