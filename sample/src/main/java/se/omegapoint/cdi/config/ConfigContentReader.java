package se.omegapoint.cdi.config;

import se.omegapoint.cdi.resource.ConfigResourceType;
import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 23/09/14
 * Time: 01:08
 * To change this template use File | Settings | File Templates.
 */
@Qualifier
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface ConfigContentReader {
    public ConfigResourceType value();
}
