package cz.zcu.kiv.matyasj.dp.web.controllers.teacher;

import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.GradeType;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
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
 * Objects of this class represent controllers which serve user requests related to Teacher's new evaluation.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Controller
@RequestMapping("/teacher-view/setEvaluation")
public class NewGradeController {
    /** Service object providing functions for manipulation with data related to students.*/
    @Autowired
    TeacherService teacherService;
    /** Object for resolving messages, with support for the parameterization and internationalization of such messages.*/
    @Autowired
    MessageSource messageSource;
    /** Shared system logger */
    protected Logger log = LogManager.getLogger();

    /**
     * This method serves user GET requests to show new Grade (evaluation) form
     *
     * @param model Model to be sent to view
     * @return ModelAndView object
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showNewGradeForm(Model model) {
        log.info("Request for retrieving new evaluation form view.");
        List<ExaminationDate> examinationDateList = teacherService.getMyExaminationDatesWithoutGraduateParticipants((Teacher) teacherService.getCurrentUser());
        List<GradeType> gradeTypes = teacherService.getAllGradeTypes();

        ModelAndView retModel = new ModelAndView("/WEB-INF/pages/teacher-view.jsp");
        retModel.addObject("examinationDateList", examinationDateList);
        retModel.addObject("gradeTypes", gradeTypes);
        retModel.addObject("view", "setEvaluation");

        return retModel;
    }

    /**
     * This method serves user GET requests to show new ExamDate form with pre-selected Exam Date and student
     *
     * @param model      Model to be sent to view
     * @param examDateId Id of pre-selected exam date
     * @param studentId  Id of pre-selected student
     * @return ModelAndView object
     */
    @GetMapping("/{examDateId}/{studentId}")
    public ModelAndView showNewGradeFormByExamTermAndStudent(Model model, @PathVariable Long examDateId, @PathVariable Long studentId) {
        log.info("Request for retrieving new evaluation form with preselected exam date with id " + examDateId + " and student with id " + studentId + " view.");

        List<GradeType> gradeTypes = teacherService.getAllGradeTypes();
        ExaminationDate examinationDate = teacherService.getExaminationTerm(examDateId);
        Student owner = teacherService.getStudentFromExaminationTerm(examinationDate, studentId);

        ModelAndView retModel = new ModelAndView("/WEB-INF/pages/teacher-view.jsp");

        retModel.addObject("gradeTypes", gradeTypes);
        retModel.addObject("owner", owner);
        retModel.addObject("examinationDate", examinationDate);
        retModel.addObject("view", "setEvaluation");

        return retModel;
    }

    /**
     * This method serves user GET requests to show new ExamDate form with pre-selected Exam Date and student
     *
     * @param locale      Locale object
     * @param model       Model to be sent to view
     * @param gradeTypeId id of GradeType
     * @param ownerId     id of graduated student
     * @param examTermId  id of ExamDate where new evaluation has been created
     * @return ModelAndView object
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView saveNewGrade(Locale locale, Model model, @RequestParam("grade") long gradeTypeId, @RequestParam("ownerId") long ownerId, @RequestParam("examTermId") long examTermId) {
        Teacher currentTeacher = (Teacher) teacherService.getCurrentUser();
        log.info("Request from user with id " + currentTeacher.getId() + " for saving new grade for exam term with id " + examTermId + " for student with id " + ownerId + ".");
        ExaminationDate examinationDate = teacherService.getExaminationTerm(examTermId);

        boolean success = teacherService.createNewGrade(currentTeacher, ownerId, gradeTypeId, examinationDate.getSubject().getId(), examinationDate.getId());
        if (success) {
            model.addAttribute("successMessage", messageSource.getMessage("tea.setEvaluation.successMessage", null, locale));
            log.info("Request for new grade created by teacher " + currentTeacher.getFirstName() + " " + currentTeacher.getLastName() + " was successful.");
        } else {
            model.addAttribute("errorMessage", messageSource.getMessage("tea.setEvaluation.errorMessage", null, locale));
            log.error("Request for new grade created by teacher " + currentTeacher.getFirstName() + " " + currentTeacher.getLastName() + " failed.");
        }

        return showNewGradeFormBySelected(examinationDate);
    }

    private ModelAndView showNewGradeFormBySelected(ExaminationDate examinationDate) {
        log.info("Request for retrieving new grade form by selected date " + examinationDate + ".");

        List<GradeType> gradeTypes = teacherService.getAllGradeTypes();
        List<ExaminationDate> examinationDateList = teacherService.getMyExaminationDatesWithoutGraduateParticipants((Teacher) teacherService.getCurrentUser());

        ModelAndView retModel = new ModelAndView("/WEB-INF/pages/teacher-view.jsp");
        retModel.addObject("examinationDateList", examinationDateList);
        retModel.addObject("gradeTypes", gradeTypes);
        retModel.addObject("view", "setEvaluation");
        retModel.addObject("examinationDateSelected", examinationDate);

        return retModel;
    }
}
