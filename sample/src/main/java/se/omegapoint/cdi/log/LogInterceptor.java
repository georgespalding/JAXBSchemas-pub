package se.omegapoint.cdi.log;

import se.omegapoint.cdi.log.Auditable;
import se.omegapoint.cdi.log.LogLevel;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.Arrays;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 16/09/14
 * Time: 22:53
 * To change this template use File | Settings | File Templates.
 */
@Interceptor @Auditable
public class LogInterceptor {
    @Inject
    private Logger log;

    @AroundInvoke
    public Object manageTransaction(InvocationContext ctx) throws Exception {
        Auditable cl = ctx.getMethod().getAnnotation(Auditable.class);
        if(cl == null){
            cl = ctx.getTarget().getClass().getAnnotation(Auditable.class);
        }
        String category=cl.category();
        if(category.isEmpty()){
            category = ctx.getTarget().getClass().getName();
        }
        final LogLevel level=cl.level();
        final boolean doLog = log.isLoggable(level.level);

        final StringBuilder msg = new StringBuilder("CALL(");
        int i = 0;
        for (; i < ctx.getParameters().length; i++) {
            msg.append(", {").append(i).append("}");
        }
        msg.append(");");

        try{
            final Object ret = ctx.proceed();
            if(ctx.getMethod().getReturnType()!=Void.class){
                msg.append(" RETURNED: {").append(i++).append("}");
                log.logp(level.level, category, ctx.getMethod().getName(), msg.toString(), expand(ctx.getParameters(), ret));
            } else {
                log.logp(level.level, category, ctx.getMethod().getName(), msg.toString(), ctx.getParameters());
            }
            return ret;
        } catch (Exception e) {
            msg.append(" THREW");
            LogRecord lr = new LogRecord(level.level, msg.toString());
            lr.setSourceClassName(category);
            lr.setSourceMethodName(ctx.getMethod().getName());
            lr.setParameters(ctx.getParameters());
            lr.setThrown(e);
            log.log(lr);
            throw e;
        }
    }

    public Object[] expand(Object [] original,Object extra){
        final int len = original.length+1;
        Object [] larger = Arrays.copyOf(original,len);
        larger[original.length]=extra;
        return larger;
    }
}
