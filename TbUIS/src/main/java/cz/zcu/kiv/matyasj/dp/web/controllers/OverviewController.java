package cz.zcu.kiv.matyasj.dp.web.controllers;

import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
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

import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * Objects of this class represent controllers which serve user requests related to overview.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Controller
public class OverviewController {
    /** service object providing functions for manipulation with data related to students.*/
    @Autowired
    private StudentService studentService;
    /** Object for resolving messages, with support for the parameterization and internationalization of such messages.*/
    @Autowired
    MessageSource messageSource;
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    /**
     * This method serves user GET requests for getting overview data.
     *
     * @param model Model to be sent to view
     * @return ModelAndView object
     */
    @RequestMapping(value = {"/student-view", "/student-view/overview", "/teacher-view", "/teacher-view/overview"}, method = RequestMethod.GET)
    public ModelAndView showUserOverview(Model model, HttpSession session) {
        User currentUser = studentService.getCurrentUser();

        ModelAndView retModel = null;
        if (currentUser instanceof Student) {
            log.info("Request for overview of student with id " + currentUser.getId() + " for view.");
            retModel = new ModelAndView("/WEB-INF/pages/student-view.jsp");
            retModel.addObject("title", studentService.setTitle());
        } else if (currentUser instanceof Teacher) {
            log.info("Request for overview of teacher with id " + currentUser.getId() + " for view.");
            retModel = new ModelAndView("/WEB-INF/pages/teacher-view.jsp");
        }

        if (retModel != null) {
            retModel.addObject("view", "overview");
            retModel.addObject("currentUser", currentUser);
            session.setAttribute("changeOverview", studentService.changeOverviewToOtherExam());
        }

        return retModel;
    }

    /**
     * This method serves user POST request for user atributes update.
     *
     * @param locale    System locale object
     * @param model     Model to be sent to view
     * @param session   HttpSession object
     * @param firstName New user first name
     * @param lastName  New user last name
     * @param email     New user email
     * @return ModelAndView object
     */
    @RequestMapping(value = {"/student-view", "/student-view/overview", "/teacher-view", "/teacher-view/overview"}, method = RequestMethod.POST)
    public ModelAndView saveUserSettings(Locale locale, Model model, HttpSession session, @RequestParam("firstName") String firstName,
                                         @RequestParam("lastName") String lastName, @RequestParam("email") String email) {
        log.info("Request for updating user with following parameters: firstname=" + firstName + ", lastname=" + lastName + ", email=" + email + ".");
        boolean success = studentService.updateUser(firstName, lastName, email);
        if (success) {
            model.addAttribute("successMessage", messageSource.getMessage("user.overview.successMessage", null, locale));
            session.setAttribute("user_full_name", firstName + " " + lastName);
            log.info("User successfully updated");
        } else {
            model.addAttribute("errorMessage", messageSource.getMessage("user.overview.errorMessage", null, locale));
            log.error("User update failed");
        }

        return showUserOverview(model, session);
    }
}