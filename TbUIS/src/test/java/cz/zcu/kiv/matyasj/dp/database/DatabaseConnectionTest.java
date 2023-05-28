package cz.zcu.kiv.matyasj.dp.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;

/**
 * DatabaseConnectionTest test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public class DatabaseConnectionTest {
    @PersistenceContext
    protected EntityManager entityManager;

    /**
     * Shared system logger
     */
    private final Logger log = LogManager.getLogger();

    /**
     * SetUp method prepare entityManager instance for UIS DB
     */
    @Before
    public void setUp() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("cz.zcu.kiv.matyasj.dp");
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * This method tests UIS - DB connection
     */
    @Test
    public void databaseConnectionTest() {
        log.info("Testing DB connection.");
        Assert.assertNotNull(entityManager);
        List users = entityManager.createQuery("SELECT u FROM User u").getResultList();
        assertNotNull(users);
    }
}