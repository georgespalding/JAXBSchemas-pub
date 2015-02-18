package se.omegapoint.cdi.resource;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import org.jboss.weld.context.ApplicationContext;
import org.jboss.weld.environment.se.contexts.ThreadContext;
import org.jboss.weld.environment.se.contexts.ThreadScoped;
import org.jboss.weld.resources.spi.ResourceLoader;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 24/09/14
 * Time: 22:03
 * To change this template use File | Settings | File Templates.
 */
public class InputStreamProducer {
    @QualifiedResource("classloader*")
    public Iterable<InputStream> getResources(InjectionPoint ip, ClassLoader classLoader) throws IOException {
        List<InputStream> rs = new LinkedList<>();
        for(Annotation q:ip.getQualifiers()){
            if(QualifiedResource.class.equals(q)){
                QualifiedResource qr = (QualifiedResource) q;
                Enumeration<URL> en = classLoader.getResources(qr.name());
                while (en.hasMoreElements()) {
                    URL r =  en.nextElement();
                    rs.add(classLoader.getResourceAsStream(r.toString());
                }
            }
        }
        return rs;
    }

    ResourceLoader rl;
    @QualifiedResource("classloader")
    public Iterable<InputStream> getResources(InjectionPoint ip, ClassLoader classLoader){
        for(Annotation q:ip.getQualifiers()){
            if(QualifiedResource.class.equals(q)){
                QualifiedResource qr = (QualifiedResource) q;
                InputStream is = classLoader.getResourceAsStream(qr.name());
                if(is!=null){
                    return Collections.singleton(is);
                }
            }
        }
        return Collections.emptyList();
    }

    @Produces @ThreadScoped
    public ClassLoader getThreadContextClassloader(){
        return Thread.currentThread().getContextClassLoader();
    }

    @Produces @ApplicationScoped
    public ClassLoader getClassloader(){
        return Thread.currentThread().getContextClassLoader();
    }

}
