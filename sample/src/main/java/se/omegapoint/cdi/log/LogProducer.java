package se.omegapoint.cdi.log;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 16/09/14
 * Time: 22:34
 * To change this template use File | Settings | File Templates.
 */
public class LogProducer {
    @Produces
    Logger createLogger(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

}
