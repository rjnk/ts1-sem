package cz.zcu.kiv.matyasj.dp.utils.init;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * This interface defines methods of the utility components which care about application initialization.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public interface Initializer {
    /**
     * This method makes database initialization. Method try to make import data from default file.
     * If default init import file is not present, method try to call static initialization.
     *
     * @return true if init action was successfully completed, false otherwise
     */
    @EventListener(ContextRefreshedEvent.class)
    boolean init();

    /**
     * This method makes database reinitialization.
     *
     * @return true if reinit action was successfully completed, false otherwise
     */
    boolean reinit();
}
