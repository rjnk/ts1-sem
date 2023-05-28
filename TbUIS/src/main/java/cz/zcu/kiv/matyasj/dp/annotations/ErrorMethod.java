package cz.zcu.kiv.matyasj.dp.annotations;

/**
 * UIS annotation for labeling methods with error functionality.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public @interface ErrorMethod {
    /** Short description of error functionality in methods */
    String errorMessage();
}
