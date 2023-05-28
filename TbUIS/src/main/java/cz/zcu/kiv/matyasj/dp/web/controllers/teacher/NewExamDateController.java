package cz.zcu.kiv.matyasj.dp.web.controllers.teacher;

import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import cz.zcu.kiv.matyasj.dp.service.TeacherService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Locale;

/**
 * Objects of this class represent controllers which serve user requests related to Teacher's new examination dates.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
 */
@Controller
@RequestMapping("/teacher-view/newExamDates")
public class NewExamDateController {
    /** Service object providing functions for manipulation with data related to teachers.*/
    @Autowired
    TeacherService teacherService;
    /** Object for resolving messages, with support for the parameterization and internationalization of such messages.*/
    @Autowired
    MessageSource messageSource;
    /** Shared system logger */
    protected Logger log = LogManager.getLogger();

    /**
     * This method serves user GET requests to show new ExamDate form
     *
     * @param model Model to be sent to view
     * @return ModelAndView object
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showNewExamDateForm(Model model) {
        log.info("Request for retrieving new exam date form view.");
        List<Subject> taughtSubjectsForExamList = teacherService.getSubjectsForCreateExaminations((Teacher) teacherService.getCurrentUser());

        ModelAndView retModel = new ModelAndView("/WEB-INF/pages/teacher-view.jsp");
        retModel.addObject("taughtSubjectList", taughtSubjectsForExamList);
        retModel.addObject("view", "newExamDates");

        return retModel;
    }

    /**
     * This method serves user GET requests to show new ExamDate form with pre-selected subject
     *
     * @param model     Model to be sent to view
     * @param subjectId id of pre-selected subject
     * @return ModelAndView object
     */
    @GetMapping("/{subjectId}")
    public ModelAndView showNewExamDateFormWithSubject(Model model, @PathVariable Long subjectId) {
        log.info("Request for retrieving new ExamDate form with preselected subject with id " + subjectId + " for view.");
        ModelAndView retModel = showNewExamDateForm(model);

        Object taughtSubjectList = retModel.getModel().get("taughtSubjectList");

        if (taughtSubjectList instanceof List) {
            if (taughtSubjectList != null) {
                for (Subject s : (List<Subject>) taughtSubjectList) {
                    if (s.getId().longValue() == subjectId.longValue()) {
                        retModel.addObject("selectedSubject", s);
                        break;
                    }
                }
            }
        }

        return retModel;
    }

    /**
     * This method serves user POST requests to create new ExamDate
     *
     * @param locale          Locale object
     * @param model           Model to be sent to view
     * @param dateOfTest      date of new ExamDate
     * @param subjectId       id of pre-selected subject
     * @param maxParticipants number of max participants on new ExamDate
     * @return ModelAndView object
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView saveNewExamDate(Locale locale, Model model, @RequestParam("date-of-test") String dateOfTest, @RequestParam("subject") Long subjectId, @RequestParam("maxParticipants") String maxParticipants) {
        User currentUser = teacherService.getCurrentUser();
        log.info("Request from user with id " + currentUser.getId() + " for saving new exam date with subject id " + subjectId + ", date " + dateOfTest + " and maximal number of participants " + maxParticipants + ".");

        boolean success = teacherService.createNewExaminationTerm((Teacher) currentUser, subjectId, dateOfTest, maxParticipants);
        if (success) {
            log.info("Request for saving new exam date with subject id " + subjectId + ", date " + dateOfTest + " and maximal number of participants " + maxParticipants + " was successful.");
            model.addAttribute("successMessage", messageSource.getMessage("tea.newExamDates.successMessage", null, locale));
        } else {
            log.error("Request for saving new exam date with subject id " + subjectId + ", date " + dateOfTest + " and maximal number of participants " + maxParticipants + " failed.");
            model.addAttribute("errorMessage", messageSource.getMessage("tea.newExamDates.errorMessage", null, locale));
        }

        return showNewExamDateForm(model);
    }
}
