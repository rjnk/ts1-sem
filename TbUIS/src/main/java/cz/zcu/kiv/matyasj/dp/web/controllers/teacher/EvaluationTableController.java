package cz.zcu.kiv.matyasj.dp.web.controllers.teacher;

import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Grade;
import cz.zcu.kiv.matyasj.dp.domain.university.GradeType;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import cz.zcu.kiv.matyasj.dp.service.TeacherService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Objects of this class represent controllers which serve user requests related to Teacher's evaluation table.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Controller
@RequestMapping("/teacher-view/evaluationTable")
public class EvaluationTableController {
    private static final String FILTER_SUBJECT_ATTRIBUTE_IN_SESSION = "filterSubjectId";
    private static final String FILTER_INCLUDES_STUDENTS_ATTRIBUTE_IN_SESSION = "filterIncludeGradedStudents";

    /** Service object providing functions for manipulation with data related to teachers.*/
    @Autowired
    TeacherService teacherService;
    /** Object for resolving messages, with support for the parameterization and internationalization of such messages.*/
    @Autowired
    MessageSource messageSource;
    /** Shared system logger */
    protected Logger log = LogManager.getLogger();

    /**
     * This method serves user GET requests to getting view with the evaluation table.
     *
     * @param model                   Model to be sent to view
     * @param session                 HttpSession object
     * @param filteredSubject         filtered subject id
     * @param includingGradedStudents Boolean value if include graded students
     * @return ModelAndView object
     */
    @GetMapping
    public ModelAndView showEvaluationTable(ModelAndView model, HttpSession session, @RequestParam("filterSubjectId") Optional<Long> filteredSubject, @RequestParam("filterIncludeGradedSubjects") Optional<String> includingGradedStudents) {
        log.info("Request for retrieving evaluation table view.");
        Long subjectId = -1L;
        boolean isIncludingGradedStudentsChecked = false;

        model.setViewName("/WEB-INF/pages/teacher-view.jsp");

        Teacher currentUser = (Teacher) teacherService.getCurrentUser();

        /* Set filter values */
        filteredSubject.ifPresent(aLong -> {
            session.setAttribute(FILTER_SUBJECT_ATTRIBUTE_IN_SESSION, aLong);
            session.setAttribute(FILTER_INCLUDES_STUDENTS_ATTRIBUTE_IN_SESSION, includingGradedStudents.isPresent());
        });

        if (session.getAttribute(FILTER_SUBJECT_ATTRIBUTE_IN_SESSION) != null) {
            subjectId = (Long) session.getAttribute(FILTER_SUBJECT_ATTRIBUTE_IN_SESSION);
        }
        if (session.getAttribute(FILTER_INCLUDES_STUDENTS_ATTRIBUTE_IN_SESSION) != null) {
            isIncludingGradedStudentsChecked = (boolean) session.getAttribute(FILTER_INCLUDES_STUDENTS_ATTRIBUTE_IN_SESSION);
        }

        List<Subject> subjectList = teacherService.getTaughtSubjectsList(currentUser);
        List<GradeType> gradeTypes = teacherService.getAllGradeTypes();
        List<ExaminationDate> listOfExamTerms;

        if (isIncludingGradedStudentsChecked) {
            listOfExamTerms = teacherService.getAllExaminationTermsByTeacherAndSubject((Teacher) teacherService.getCurrentUser(), subjectId);
        } else {
            listOfExamTerms = teacherService.getMyExaminationTermsWithoutGradedParticipantsBySubject((Teacher) teacherService.getCurrentUser(), subjectId);
        }

        List<Grade> gradeList = teacherService.getGradesForSubject(subjectId);

        model.addObject("selectedSubjectId", subjectId);
        model.addObject("subjectList", subjectList);
        model.addObject("gradeTypes", gradeTypes);
        model.addObject("listOfExamTerms", listOfExamTerms);
        model.addObject("gradeList", gradeList);
        model.addObject("isIncludingGradedStudentsChecked", isIncludingGradedStudentsChecked);
        model.addObject("view", "evaluationTable");

        return model;
    }

    /**
     * This method serves user POST requests to creating of new grade (evaluation).
     *
     * @param model             Model to be sent to view
     * @param session           HttpSession object
     * @param studentId         student id
     * @param gradeTypeId       Boolean value if include graduated students
     * @param subjectId         subject id
     * @param examinationDateId examination date id
     * @return ModelAndView object
     */
    @PostMapping("/createNewGrade")
    public ModelAndView createNewGrade(Locale locale, ModelAndView model, HttpSession session, @RequestParam("studentId") Long studentId, @RequestParam("gradeTypeId") Long gradeTypeId, @RequestParam("subjectId") Long subjectId, @RequestParam("examinationDateId") Long examinationDateId) {
        User teacher = teacherService.getCurrentUser();
        log.info("User with id " + teacher.getId() + " requested creating of grade.");

        boolean success = teacherService.createNewGrade((Teacher) teacher, studentId, gradeTypeId, subjectId, examinationDateId);
        if (success) {
            log.info("Request for creating of grade type: " + gradeTypeId + " for subject with id: " + subjectId + " for student with id: " + studentId + "  was successful.");
            model.addObject("successMessage", messageSource.getMessage("tea.evaluationTable.successMessage.created", null, locale));
        } else {
            log.error("Request for creating of grade type: " + gradeTypeId + " for subject with id: " + subjectId + " for student with id: " + studentId + "  failed.");
            model.addObject("errorMessage", messageSource.getMessage("tea.evaluationTable.errorMessage.created", null, locale));
        }
        return showEvaluationTable(model, session, Optional.empty(), Optional.empty());
    }

    /**
     * This method serves user POST requests to updating of existent grade (evaluation).
     *
     * @param model          Model to be sent to view
     * @param session        HttpSession object
     * @param newGradeTypeId updated grade type
     * @param gradeId        id of existent grade
     * @return ModelAndView object
     */
    @PostMapping("/updateGrade")
    public ModelAndView updateGrade(Locale locale, ModelAndView model, HttpSession session, @RequestParam("gradeTypeId") Long newGradeTypeId, @RequestParam("gradeId") Long gradeId) {
        Long userId = teacherService.getCurrentUser().getId();

        log.info("User with id " + userId + " requested for updating grade with id " + gradeId + ".");

        boolean success = teacherService.updateGrade(userId, gradeId, newGradeTypeId);
        if (success) {
            log.info("Request for updating grade of id: " + gradeId + " with updated grade type: " + newGradeTypeId + " was successful.");
            model.addObject("successMessage", messageSource.getMessage("tea.evaluationTable.successMessage.updated", null, locale));
        } else {
            log.error("Request for updating grade of id: " + gradeId + " with updated grade type: " + newGradeTypeId + " failed.");
            model.addObject("errorMessage", messageSource.getMessage("tea.evaluationTable.errorMessage.updated", null, locale));
        }

        return showEvaluationTable(model, session, Optional.empty(), Optional.empty());
    }
}
