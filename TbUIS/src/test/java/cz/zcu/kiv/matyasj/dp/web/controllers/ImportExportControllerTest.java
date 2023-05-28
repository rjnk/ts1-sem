package cz.zcu.kiv.matyasj.dp.web.controllers;

import cz.zcu.kiv.matyasj.dp.service.PorterService;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ImportExportController test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class ImportExportControllerTest extends BaseControllerTest{
    @Autowired
    private PorterService porterService;

    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    @Before
    public void setUp(){
        super.setUp();
    }

    @After
    public void tearDown(){
        super.tearDown();
    }

    /**
     * This method tests ImportExportController serving user request - GET /import-export
     */
    @Test
    public void getImportExport() throws Exception {
        log.info("Testing Import/Export screen accessibility.");
        mockMvc.perform(get("/import-export"))
                .andExpect(status().isOk())
                .andExpect(view().name("/import-export.jsp"));
    }

    /**
     * This method tests ImportExportController serving user request - file upload /importdata
     */
    @Test
    public void importData() throws Exception {
        log.info("Testing data import with XML file.");
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/importdata").file(getFileToImport("xml")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(view().name("/import-export.jsp"));
    }

    /**
     * This method tests ImportExportController serving user request - file upload /importdata (empty file)
     */
    @Test
    public void importDataEmptyFile() throws Exception {
        log.info("Testing data import with empty file.");
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/importdata").file(getFileEmptyToImport()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("/import-export.jsp"));
    }

    /**
     * This method tests ImportExportController serving user request - file upload /importdata (non existent file)
     */
    @Test
    public void importDataNonExistentFile() throws Exception {
        log.info("Testing data import with not existing file.");
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/importdata").file(getFileNonExistentToImport()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("/import-export.jsp"));
    }

    /**
     * This method tests ImportExportController serving user request - file download /exportdata
     */
    @Test
    public void exportData() throws Exception {
        log.info("Testing correctly formatted data export.");
        mockMvc.perform(get("/exportdata").param("exportFormat", "xml"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/xml"));
    }

    /**
     * This method tests ImportExportController serving user request - file download /exportdata (file in bad format)
     */
    @Test
    public void exportDataBadFormat() throws Exception {
        log.info("Testing incorrectly formatted data export.");
        mockMvc.perform(get("/exportdata").param("exportFormat", "bad_format"))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    }

    /**
     * This method creates and returns (mocked) Multipart file for data importing.
     *
     * @param format format of file xml/json
     * @return Mocked MultipartFile
     */
    private MockMultipartFile getFileToImport(String format) throws IOException {
        log.info("Creating mocked XML file to import.");

        File file = porterService.exportData(format);

        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("importFile",
                file.getName(), "application/xml", IOUtils.toByteArray(input));


        return multipartFile;
    }

    /**
     * This method creates and returns empty (mocked) Multipart file for data importing.
     * @return Mocked empty MultipartFile
     */
    private MockMultipartFile getFileEmptyToImport() throws IOException {
        log.info("Creating mocked empty file to import.");

        File file = new File(System.getProperty("java.io.tmpdir")+File.separator +"testFile.xml");

        file.createNewFile();

        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("importFile",
                file.getName(), "application/xml", IOUtils.toByteArray(input));


        file.delete();
        return multipartFile;
    }

    /**
     * This method creates and returns non existent (mocked) Multipart file for data importing.
     * @return non existent Mocked MultipartFile
     */
    private MockMultipartFile getFileNonExistentToImport() throws IOException {
        log.info("Creating mocked not existing file to import.");

        File file = new File(System.getProperty("java.io.tmpdir")+File.separator +"testFile.xml");


        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("importFile",
                file.getName(), "application/xml", IOUtils.toByteArray(input));


        return multipartFile;
    }
}