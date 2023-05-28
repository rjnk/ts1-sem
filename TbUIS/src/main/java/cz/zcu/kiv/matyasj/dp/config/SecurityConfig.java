package cz.zcu.kiv.matyasj.dp.config;

import cz.zcu.kiv.matyasj.dp.dao.UserDao;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.persistence.DiscriminatorValue;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Object of this class focuses on providing application security. It includes users authorization and authentication.
 * In this configuration class security rules are set:
 * <ul>
 * <li>Authorization rules - user access permissions</li>
 * <li>Authentication rules - user credentials</li>
 * <li>Success authentication event rules - Redirect logged in users to correct view.</li>
 * </ul>
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();
    /** User DAO object for getting users from database. */
    private final UserDao userDao;
    /** DataSource object for getting database connection. */
    private final DataSource dataSource;
    /** Password encoder used for password hashes validation. */
    private final PasswordEncoder passwordEncoder;

    /** Student role identification string constant */
    private static final String STUDENT_ROLE = Student.class.getAnnotation(DiscriminatorValue.class).value();
    /** Teacher role identification string constant */
    private static final String TEACHER_ROLE = Teacher.class.getAnnotation(DiscriminatorValue.class).value();

    /**
     * Base SecurityConfig constructor
     *
     * @param userDao         User DAO object for getting users from database
     * @param dataSource      DataSource object for getting database connection
     * @param passwordEncoder Password encoder used for password hashes validation
     */
    @Autowired
    public SecurityConfig(UserDao userDao, DataSource dataSource, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * User authentication configuration method. There is in-memory auth config created from all
     *
     * @param auth AuthenticationManagerBuilder object
     * @throws Exception General Exception during global security configuraiton
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        log.info("SECURITY JDBC authentication process is running");

        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(
                        "select username,password, enabled from users where username=?")
                .authoritiesByUsernameQuery(
                        "select username, CONCAT(\"ROLE_\", DTYPE) AS role from users where username=?")
                .passwordEncoder(passwordEncoder);
    }

    /**
     * Spring secure authorization configuration.
     *
     * @param http Http Spring Security object
     * @throws Exception General Exception during global authorize configuration
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("Authorization configuration process is running");
        http.authorizeRequests()
                .antMatchers("/student-view/**").access("hasRole('" + STUDENT_ROLE + "')")
                .antMatchers("/teacher-view/**").access("hasRole('" + TEACHER_ROLE + "')")
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(getAuthenticationSuccessHandler())
                .failureHandler(getAuthenticationFailureHandler())
                .and()
                .csrf().disable();
    }

    /**
     * This method create and return Authentication Handler which redirect students to students view and
     * teachers to teachers view (after successful login).
     *
     * @return Success Authentication Handler Object
     */
    private AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return (httpServletRequest, httpServletResponse, authentication) -> {
            // Get user from db by username
            String username = authentication.getName();
            User loggedInUser = userDao.findByUsername(username);

            // Save name (first name and last name) for display it in header.
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute("user_full_name", loggedInUser.getFirstName() + " " + loggedInUser.getLastName());
            log.info("Success login - " + loggedInUser.getUsername());

            // Check granted authority and choose target of redirection (student -> student-view, teacher -> teacher-view)
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                log.info("User login role: " + grantedAuthority.getAuthority());
                if (grantedAuthority.getAuthority().equals("ROLE_" + STUDENT_ROLE)) {
                    session.setAttribute("user_view", "student-view");
                    httpServletResponse.sendRedirect("student-view/overview");
                    break;
                } else if (grantedAuthority.getAuthority().equals("ROLE_" + TEACHER_ROLE)) {
                    session.setAttribute("user_view", "teacher-view");
                    httpServletResponse.sendRedirect("teacher-view/overview");
                    break;
                }
            }
        };
    }

    /**
     * This method create and return Authentication Handler which takes care of failed authentication.
     *
     * @return Failure Authentication Handler Object
     */
    private AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return (httpServletRequest, httpServletResponse, authentication) -> {
            log.info("Login error.");
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute("error", "Login error.");
            httpServletResponse.sendRedirect("login?error");
        };
    }
}
