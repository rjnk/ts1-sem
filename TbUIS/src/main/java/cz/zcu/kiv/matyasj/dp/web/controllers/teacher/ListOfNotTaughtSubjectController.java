package cz.zcu.kiv.matyasj.dp.web.controllers.teacher;

import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.service.TeacherService;
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
 * Objects of this class represent controllers which serve user requests related to Teacher's not taught subjects.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Controller
@RequestMapping("/teacher-view/otherSubjects")
public class ListOfNotTaughtSubjectController {
    /** Service object providing functions for manipulation with data related to teachers.*/
    @Autowired
    TeacherService teacherService;
    /** Object for resolving messages, with support for the parameterization and internationalization of such messages.*/
    @Autowired
    MessageSource messageSource;
    /** Application property loader */
    @Autowired
    protected PropertyLoader propertyLoader;
    /** Shared system logger */
    protected Logger log = LogManager.getLogger();

    /**
     * This method serves user GET requests to show list of all not taught subjects
     *
     * @param model Model to be sent to view
     * @return ModelAndView object
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showNotTaughtSubjectList(Model model) {
        log.info("Request for retrieving list of not taught subjects view.");
        List<Subject> listOfSubjects = teacherService.getNonTaughtSubjectsList((Teacher) teacherService.getCurrentUser());

        int maxTeacherCount = Integer.parseInt(propertyLoader.getProperty("subjectMaxTeachers"));
        boolean participationAllowed = teacherService.getNumberOfTaughtSubjects() < Integer.parseInt(propertyLoader.getProperty("teacherMaxSubjects")) ;

        ModelAndView retModel = new ModelAndView("/WEB-INF/pages/teacher-view.jsp");
        retModel.addObject("subjectList", listOfSubjects);
        retModel.addObject("view", "otherSubjects");
        retModel.addObject("maxTeacherCount", maxTeacherCount);
        retModel.addObject("participationAllowed", participationAllowed);

        return retModel;
    }

    /**
     * This method serves user POST requests to set new taught subject
     *
     * @param model     Model to be sent to view
     * @param locale    Locale object
     * @param subjectId Id of subject to set
     * @return ModelAndView object
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView setTaughtSubject(Locale locale, Model model, @RequestParam("subjectId") Long subjectId) {
        Long teacherId = teacherService.getCurrentUser().getId();
        log.info("Request for setting new taught subject(id[" + subjectId + "]) for teacher (id[" + teacherId + "]).");

        boolean success = teacherService.setMySubject((Teacher) teacherService.getCurrentUser(), subjectId);
        if (success) {
            log.info("Request for setting new taught subject(id["+subjectId+"]) for teacher (id["+teacherId + "]) was successful.");
            model.addAttribute("successMessage", messageSource.getMessage("tea.otherSubjects.successMessage", null, locale));
        } else {
            log.error("Request for setting new taught subject(id["+subjectId+"]) for teacher (id["+teacherId + "]) was successful.");
            model.addAttribute("errorMessage", messageSource.getMessage("tea.otherSubjects.errorMessage", null, locale));
        }

        return showNotTaughtSubjectList(model);
    }
}
