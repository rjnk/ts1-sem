package cz.zcu.kiv.matyasj.dp.dao.jpa;

import cz.zcu.kiv.matyasj.dp.dao.GradeDao;
import cz.zcu.kiv.matyasj.dp.dao.GradeTypeDao;
import cz.zcu.kiv.matyasj.dp.domain.university.GradeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

/**
 * GradeTypeDao test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class GradeTypeDaoCriteriaTest {
    @Autowired
    GradeTypeDao gradeTypeDao;
    @Autowired
    GradeDao gradeDao;

    /**
     * Shared system logger
     */
    private final Logger log = LogManager.getLogger();

    /**
     * This method tests GradeTypeDao function - Getting all grade types from database
     */
    @Test
    @Transactional
    @Rollback(false)
    public void getAllGradeTypes() {
        log.info("Testing all grade types retrieving.");

        int beforeGradeTypes = gradeTypeDao.findAll().size();
        GradeType g1 = new GradeType();
        g1.setName("GradeType 1");

        GradeType g2 = new GradeType();
        g2.setName("GradeType 1");

        g1 = gradeTypeDao.save(g1);
        g2 = gradeTypeDao.save(g2);

        List<GradeType> gradeTypeList = gradeTypeDao.getAllGradeTypes();

        gradeTypeDao.delete(g1.getId());
        gradeTypeDao.delete(g2.getId());

        assertNotNull(gradeTypeList);
        assertTrue(gradeTypeList instanceof List);
        assertEquals(beforeGradeTypes + 2, gradeTypeList.size());
    }
}