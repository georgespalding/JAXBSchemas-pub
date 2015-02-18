package se.omegapoint.weld.sample;

import se.omegapoint.cdi.log.Auditable;
import se.omegapoint.cdi.log.LogLevel;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 16/09/14
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
@Auditable(level = LogLevel.Warning)
public class MyWeldBean {
    @Inject
    private Logger log;

    @Property("somekey") @Inject private String stringProp;
    @Property("someOtherKey") @Inject private Integer intProp;

    public String getStringProp() {
        return stringProp;
    }

    public void setStringProp(String stringProp) {
        this.stringProp = stringProp;
    }

    public Integer getIntProp() {
        return intProp;
    }

    public void setIntProp(Integer intProp) {
        this.intProp = intProp;
    }

    public void ooops() throws Exception {
        throw new Exception("Oops");
    }
}
