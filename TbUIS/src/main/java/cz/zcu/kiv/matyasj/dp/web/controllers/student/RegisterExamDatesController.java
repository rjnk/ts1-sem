package cz.zcu.kiv.matyasj.dp.web.controllers.student;

import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.service.StudentService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Locale;

/**
 * Objects of this class represent controllers which serve user requests related to Student registered Examination Dates
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Controller
@RequestMapping("/student-view/myExamDates")
public class RegisterExamDatesController {
    /** Service object providing functions for manipulation with data related to students.*/
    @Autowired
    StudentService studentService;
    /** Object for resolving messages, with support for the parameterization and internationalization of such messages.*/
    @Autowired
    MessageSource messageSource;
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    /**
     * This method serves user GET requests for getting list of registered examination dates.
     *
     * @param model Model to be sent to view
     * @return ModelAndView object
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showRegisteredExamDateList(Model model) {
        log.info("Request for retrieving reqistered exam dates view.");

        List<ExaminationDate> examinationDateList = studentService.getStudentExaminationDatesList(studentService.getCurrentUser().getId());

        ModelAndView retModel = new ModelAndView("/WEB-INF/pages/student-view.jsp");
        retModel.addObject("view", "myExamDates");
        retModel.addObject("examinationDateList", examinationDateList);
        retModel.addObject("changeParticipantsButtonColor", studentService.changeParticipantsButtonColor());
        retModel.addObject("changeParticipantsNumber", studentService.changeNumberOfParticipants());

        return retModel;
    }

    /**
     * this method serves user POST request for unregistering Examination Date.
     *
     * @param locale     System locale object
     * @param model      Model to be sent to view
     * @param examDateId Id of Examination Date to unregister
     * @return ModelAndView object
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView unregisterExamDate(Locale locale, Model model, @RequestParam("examDateId") Long examDateId) {
        Long userId = studentService.getCurrentUser().getId();
        log.info("Request for unregistering exam date with id " + examDateId + " for user with id " + userId + ".");

        boolean success = studentService.unsetExaminationDate(userId, examDateId);

        if (success) {
            log.info("Request for unregistering exam date with id " + examDateId + " was successful");
            model.addAttribute("successMessage", messageSource.getMessage("stu.myExamDates.successMessage", null, locale));
        } else {
            log.error("Request for unregistering exam date with id " + examDateId + " failed");
            model.addAttribute("errorMessage", messageSource.getMessage("stu.myExamDates.errorMessage", null, locale));
        }
        return showRegisteredExamDateList(model);
    }
}