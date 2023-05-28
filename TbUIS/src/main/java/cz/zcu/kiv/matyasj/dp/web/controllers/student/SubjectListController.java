package cz.zcu.kiv.matyasj.dp.web.controllers.student;

import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.service.StudentService;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;
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
 * Objects of this class represent controllers which serve user requests related to Student unenrolled subjects.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Controller
@RequestMapping("/student-view/otherSubjects")
public class SubjectListController {
    /** Service object providing functions for manipulation with data related to students.*/
    @Autowired
    StudentService studentService;
    /** Object for resolving messages, with support for the parameterization and internationalization of such messages.*/
    @Autowired
    MessageSource messageSource;
    /** Application property loader */
    @Autowired
    protected PropertyLoader propertyLoader;
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    /**
     * This method serves user GET requests to getting list of unenrolled subjects.
     *
     * @param model Model to be sent to view
     * @return ModelAndView object
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showOtherSubjectList(Model model) {
        log.info("Request for retrieving other subject view.");
        List<Subject> listOfSubjects = studentService.getOtherSubjectsList(studentService.getCurrentUser().getId());

        List<Subject> registeredSubjects = studentService.getStudiedSubjectsList(studentService.getCurrentUser().getId());
        boolean isRegisterAllowed = Integer.parseInt(propertyLoader.getProperty("studentMaxSubjects")) > registeredSubjects.size();

        ModelAndView retModel = new ModelAndView("/WEB-INF/pages/student-view.jsp");
        retModel.addObject("subjectList", listOfSubjects);
        retModel.addObject("view", "otherSubjects");
        retModel.addObject("hideTeacherColumn", studentService.hideTeacherColumn());
        retModel.addObject("isRegisterAllowed", isRegisterAllowed);

        return retModel;
    }

    /**
     * This method serves user POST requests for enroll single subject.
     *
     * @param locale    System locale object
     * @param model     Model to be sent to view
     * @param subjectId id of subject to enroll
     * @return ModelAndView object
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView enrollSubject(Locale locale, Model model, @RequestParam("subjectId") Long subjectId) {
        Long studentId = studentService.getCurrentUser().getId();
        log.info("Enrolling subject with id " + subjectId + " for user user with id " + studentId + ".");

        boolean success = studentService.setStudiedSubject(studentId, subjectId);
        if (success) {
            log.info("Request for enrolling subject with id " + subjectId + " was successful.");
            model.addAttribute("successMessage", messageSource.getMessage("stu.otherSubjects.successMessage", null, locale));
        } else {
            log.error("Request for enrolling subject with id " + subjectId + " failed.");
            model.addAttribute("errorMessage", messageSource.getMessage("stu.otherSubjects.errorMessage", null, locale));
        }

        return showOtherSubjectList(model);
    }
}