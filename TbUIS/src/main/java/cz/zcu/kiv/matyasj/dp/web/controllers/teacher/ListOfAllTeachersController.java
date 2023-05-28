package cz.zcu.kiv.matyasj.dp.web.controllers.teacher;

import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.service.TeacherService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Objects of this class represent controllers which serve user requests related to all teachers list.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Controller
@RequestMapping("/teacher-view/listOfAllTeachers")
public class ListOfAllTeachersController {
    /** Service object providing functions for manipulation with data related to teachers.*/
    @Autowired
    TeacherService teacherService;
    /** Shared system logger */
    protected Logger log = LogManager.getLogger();

    /**
     * This method serves user GET requests to show list of all teachers in system
     *
     * @param model Model to be sent to view
     *
     * @return ModelAndView object
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showAllTeachersList(Model model){
        log.info("Request for retrieving list of all teachers view.");

        List<Teacher> listOfAllTeachers = teacherService.getAllTeachers();

        ModelAndView retModel = new ModelAndView("/WEB-INF/pages/teacher-view.jsp");

        retModel.addObject("listOfAllTeachers", listOfAllTeachers);
        retModel.addObject("view", "listOfAllTeachers");

        return  retModel;
    }
}