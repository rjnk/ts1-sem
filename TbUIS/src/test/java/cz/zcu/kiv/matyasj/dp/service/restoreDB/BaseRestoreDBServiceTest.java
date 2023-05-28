package cz.zcu.kiv.matyasj.dp.service.restoreDB;

import cz.zcu.kiv.matyasj.dp.dao.DatabaseDao;
import cz.zcu.kiv.matyasj.dp.dao.UserDao;
import cz.zcu.kiv.matyasj.dp.domain.BaseEntity;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import cz.zcu.kiv.matyasj.dp.service.RestoreDBService;
import cz.zcu.kiv.matyasj.dp.utils.init.Initializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.*;

/**
 * RestoreDBService test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class BaseRestoreDBServiceTest {
    @Autowired
    DatabaseDao databaseDao;
    @Autowired
    UserDao userDao;
    @Autowired
    RestoreDBService restoreDBService;
    @Autowired
    Initializer initializer;

    /**
     * Shared system logger
     */
    private final Logger log = LogManager.getLogger();

    @Before
    public void setUp() throws Exception {
        if (databaseDao.eraseDatabase()) initializer.reinit();
    }

    /**
     * This method tests RestoreDBService function - Restoring database
     */
    @Test
    public void restoreDBBaseTest() {
        log.info("Testing DB restoration.");

        List<BaseEntity> entitiesBefore = databaseDao.databaseDump();
        restoreDBService.restoreDB();
        List<BaseEntity> entitiesAfter = databaseDao.databaseDump();

        assertEquals(entitiesBefore.size(), entitiesAfter.size());
    }

    /**
     * This method tests RestoreDBService function - Restoring database (with database removing before restore action)
     */
    @Test
    public void restoreDBRemoveDB() {
        log.info("Testing DB removal and restoration.");

        List<BaseEntity> entitiesBefore = databaseDao.databaseDump();
        databaseDao.eraseDatabase();

        restoreDBService.restoreDB();
        List<BaseEntity> entitiesAfter = databaseDao.databaseDump();

        assertEquals(entitiesBefore.size(), entitiesAfter.size());
    }

    /**
     * This method tests RestoreDBService function - Restoring database (with database change - add new user)
     */
    @Test
    public void restoreDBWithDBChange() {
        log.info("Testing DB restoration with DB change.");

        List<BaseEntity> entitiesBefore = databaseDao.databaseDump();
        //public Student(String firstName, String lastName, String username, String password, String email){
        // DB change - add new user into database
        User a = new Student("John", "Doe", "jDoe", "pass", "mail@edu.com");
        a = userDao.save(a);

        restoreDBService.restoreDB();
        List<BaseEntity> entitiesAfter = databaseDao.databaseDump();
        assertEquals(entitiesBefore.size(), entitiesAfter.size());

        a = userDao.findOne(a.getId());
        assertNull(a);
    }
}