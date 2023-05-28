package cz.zcu.kiv.matyasj.dp.web.controllers;

import cz.zcu.kiv.matyasj.dp.dao.UserDao;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


/**
 * Objects of this class represent controllers which serve user requests for login.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Controller
@RequestMapping("login")
public class LoginController {
    /** DAO object for manipulation with user data in database */
    @Autowired
    UserDao userDao;
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showLoginForm(Model model) {
        log.info("Request for login view");
        return new ModelAndView("login.jsp");
    }

}
