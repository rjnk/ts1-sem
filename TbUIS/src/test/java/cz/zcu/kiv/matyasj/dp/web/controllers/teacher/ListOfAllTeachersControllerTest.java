package cz.zcu.kiv.matyasj.dp.web.controllers.teacher;

import cz.zcu.kiv.matyasj.dp.web.controllers.BaseControllerTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ListOfAllTeachersController test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class ListOfAllTeachersControllerTest extends BaseControllerTest{

    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    @Before
    public void setUp() {
        super.setUp();
    }

    @After
    public void tearDown(){
        super.tearDown();
    }

    /**
     * This method tests ListOfAllTeachersController serving user request - GET /teacher-view/listOfAllTeachers
     */
    @Test
    public void showAllTeachersList() throws Exception {
        log.info("Testing list of all teacher accessibility.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(get("/teacher-view/listOfAllTeachers"))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("listOfAllTeachers"))
                .andExpect(model().attribute("view", "listOfAllTeachers"));
    }
}