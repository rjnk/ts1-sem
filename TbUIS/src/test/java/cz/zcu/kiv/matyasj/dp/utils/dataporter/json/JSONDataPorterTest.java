package cz.zcu.kiv.matyasj.dp.utils.dataporter.json;

import cz.zcu.kiv.matyasj.dp.dao.DatabaseDao;
import cz.zcu.kiv.matyasj.dp.dao.UserDao;
import cz.zcu.kiv.matyasj.dp.domain.BaseEntity;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

/**
 * JSONDataPorter test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class JSONDataPorterTest {
    @Autowired
    JSONDataPorter jsonDataPorter;

    @Autowired
    private DatabaseDao databaseDao;

    @Autowired
    private UserDao userDao;

    /**
     * Shared system logger
     */
    private final Logger log = LogManager.getLogger();

    private static final String TEST_FILE_JSON = "test_export_file.json";
    private static final String INIT_TEST_DATA_FILE_PATH = "src\\test\\java\\cz\\zcu\\kiv\\matyasj\\dp\\utils\\dataporter\\json\\";


    @Before
    public void setUp() {
        databaseDao.eraseDatabase();
        databaseDao.fillDatabase(jsonDataPorter.importData(new File(INIT_TEST_DATA_FILE_PATH + TEST_FILE_JSON)));

    }

    @After
    public void tearDown() {
        File testFile = new File(TEST_FILE_JSON);
        if (testFile.exists() && !testFile.isDirectory()) {
            try {
                log.info("Try to delete export test file[" + TEST_FILE_JSON + "]");
                Files.delete(Paths.get(TEST_FILE_JSON));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method tests JSONDataPorter function - importing data into empty database from test file
     */
    @Test
    public void deletingDbTest() {
        log.info("Testing DB deletion.");
        if (databaseDao.databaseDump().size() == 0) {
            jsonDataPorter.importData(new File(INIT_TEST_DATA_FILE_PATH + TEST_FILE_JSON));
        }
        databaseDao.eraseDatabase();

        assertEquals(0, databaseDao.databaseDump().size());
    }

    /**
     * This method tests JSONDataPorter function - exporting data from database into json file
     */
    @Test
    public void exportDataTest() throws Exception {
        log.info("Testing data export.");
        assertTrue(jsonDataPorter.exportData(TEST_FILE_JSON, databaseDao.databaseDump()));
    }

    /**
     * This method tests JSONDataPorter function - importing data into database from test json file
     */
    @Test
    public void importDataTest1() throws Exception {
        log.info("Testing data import.");
        List<BaseEntity> entities1, entities2;

        // Get entities from db before
        entities1 = databaseDao.databaseDump();

        jsonDataPorter.exportData(TEST_FILE_JSON, databaseDao.databaseDump());
        // Prevent delete all data from db
        databaseDao.eraseDatabase();
        entities2 = jsonDataPorter.importData(new File(TEST_FILE_JSON));
        databaseDao.fillDatabase(entities2);

        assertTrue(!entities2.isEmpty());
        assertTrue(entities1.size() == entities2.size());

        // Get entities from db after
        entities2 = databaseDao.databaseDump();
        assertEquals(entities1.size(), entities2.size());
    }

    /**
     * This method tests JSONDataPorter function - importing data into database from test json file (with new created user)
     */
    @Test
    public void importDataTestNewStudent() throws Exception {
        log.info("Testing data import with new created user.");
        User testStudent = new Student("John", "Doe", "testStudent", "password", "jDoe@mail.com");
        User testStudentAfter = null;

        testStudent = userDao.save(testStudent);

        jsonDataPorter.exportData(TEST_FILE_JSON, databaseDao.databaseDump());
        databaseDao.eraseDatabase();
        databaseDao.fillDatabase(jsonDataPorter.importData(new File(TEST_FILE_JSON)));

        testStudentAfter = userDao.findByUsername(testStudent.getUsername());

        assertNotNull(testStudentAfter);

        assertEquals(testStudent.getUsername(), testStudentAfter.getUsername());
        assertEquals(testStudent.getFirstName(), testStudentAfter.getFirstName());
        assertEquals(testStudent.getLastName(), testStudentAfter.getLastName());

    }

    /**
     * This method tests JSONDataPorter function - importing data into database from test json file (non existent test file).
     */
    @Test
    public void importDataNonExistentFile() throws Exception {
        log.info("Testing data import from not existing file.");
        List<BaseEntity> entities = jsonDataPorter.importData(new File("nonExistentFile.json"));
        assertNull(entities);
    }

    /**
     * This method tests JSONDataPorter function - importing data into database from test json file (empty test file).
     */
    @Test
    public void importEmptyExistentFile() throws Exception {
        log.info("Testing data import from empty file.");
        String emptyFileName = "emptyFile.json";
        File newFile = new File(emptyFileName);
        if (!newFile.exists()) {
            newFile.createNewFile();
        }
        assertNull(jsonDataPorter.importData(new File(emptyFileName)));

        // Deleting test file
        try {
            log.info("Try to delete export test file[" + emptyFileName + "]");
            Files.delete(Paths.get(emptyFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}