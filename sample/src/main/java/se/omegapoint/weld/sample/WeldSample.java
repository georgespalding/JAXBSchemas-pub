package se.omegapoint.weld.sample;

import java.util.logging.Logger;
import java.util.logging.Level;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 16/09/14
 * Time: 21:29
 * To change this template use File | Settings | File Templates.
 */
public class WeldSample {
    public static final Logger log = Logger.getLogger(WeldSample.class.getName());
    public static void main(String[] args) {

        Weld weld = new Weld();

        WeldContainer container = weld.initialize();

        MyWeldBean b = container.instance().select(MyWeldBean.class).get();
        if (log.isLoggable(Level.INFO)) {
            log.log(Level.INFO, "MyWeldBean StringProp "+b.getStringProp()+" IntProp: "+b.getIntProp());
        }

        try {
            b.ooops();
        } catch (Exception e) {
            ;
        }

        weld.shutdown();
    }
}
