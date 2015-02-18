package se.omegapoint.cdi.config;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 23/09/14
 * Time: 00:04
 * To change this template use File | Settings | File Templates.
 */

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface ConfigContentType {
    // properties
    // propertiesxml
    // preferences
    // resourcebundle
    String value();
}
