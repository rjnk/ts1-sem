package cz.zcu.kiv.matyasj.dp.web.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * RestoreDbController test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class RestoreDbControllerTest {
    protected MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * This method tests RestoreDbController serving user request - GET /restoreDB
     */
    @Test
    public void restoreDB() throws Exception {
        log.info("Testing DB restore.");
        mockMvc.perform(get("/restoreDB"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/?successMessage=*"));
    }
}