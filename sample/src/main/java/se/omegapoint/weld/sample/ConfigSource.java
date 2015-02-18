package se.omegapoint.weld.sample;

import se.omegapoint.cdi.config.MergeType;
import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 16/09/14
 * Time: 22:25
 * To change this template use File | Settings | File Templates.
 */
@Qualifier
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface ConfigSource {
    @Nonbinding public String [] sources();
    @Nonbinding public MergeType merge() default MergeType.All;
}
