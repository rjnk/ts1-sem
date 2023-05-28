package cz.zcu.kiv.matyasj.dp.web.controllers;

import cz.zcu.kiv.matyasj.dp.service.PorterService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLConnection;
import java.util.Locale;

/**
 * Objects of this class represent controllers which serve import/export user requests.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Controller
public class ImportExportController {
    /** Service for data porting (import/export) from and to database. */
    @Autowired
    PorterService porterService;
    /** Object for resolving messages, with support for the parameterization and internationalization of such messages.*/
    @Autowired
    MessageSource messageSource;
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    /**
     * This method serves user GET requests for getting import-export view.
     *
     * @param model Model to be sent to view
     * @return model
     */
    @GetMapping("/import-export")
    public ModelAndView getImportExport(ModelAndView model) {
        log.info("Request for import-export view");
        model.setViewName("/import-export.jsp");
        return model;
    }

    /**
     * This method serves user POST request for importing data.
     *
     * @param session HttpSession object
     * @param locale  system Locale object
     * @param file    MultipartFile file with data to import
     * @return ModelAndView object
     */
    @RequestMapping(value = {"/importdata"}, method = RequestMethod.POST)
    public ModelAndView importData(HttpSession session, Locale locale, @RequestParam("importFile") MultipartFile file) {
        ModelAndView retModel = new ModelAndView();

        log.info("Request for importing of data into database");

        if (file.isEmpty()) {
            log.error("File for data import into database is empty");
            retModel.addObject("errorMessage", "File is empty");
            return getImportExport(retModel);
        }

        File fileToImport = new File(System.getProperty("java.io.tmpdir"), file.getOriginalFilename());

        try {
            file.transferTo(fileToImport);
        } catch (IOException e) {
            retModel.addObject("errorMessage", "File has not been successfully imported (fail of transformation of file)!");
        }
        boolean success = porterService.importData(fileToImport);
        if (success) {
            retModel.addObject("successMessage", messageSource.getMessage("importExportPage.import.successMessage", null, locale).replace("?filename_placeholder?", fileToImport.getName()));
            // invalidate session for logout user
            session.invalidate();
        } else {
            retModel.addObject("errorMessage", messageSource.getMessage("importExportPage.import.errorMessage", null, locale));
        }

        return getImportExport(retModel);
    }

    /**
     * This method serves user GET request for exporting application data in file to be sent to view.
     *
     * @param response      HTTP servlet response
     * @param exportFormat  Export format
     * @throws IOException
     */
    @GetMapping("/exportdata")
    public void exportData(HttpServletResponse response, @RequestParam("exportFormat") String exportFormat) throws IOException {
        File file = porterService.exportData(exportFormat);

        log.info("Request for exporting data into database");

        if (file == null || !file.exists()) {
            log.error("The file with data has NOT been successfully exported/created");
            return;
        }

        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        response.setContentLength((int) file.length());

        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }
}