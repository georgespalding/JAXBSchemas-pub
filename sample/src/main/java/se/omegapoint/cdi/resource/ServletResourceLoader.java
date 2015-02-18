package se.omegapoint.cdi.resource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import org.jboss.weld.resources.spi.ResourceLoader;
import org.jboss.weld.resources.spi.ResourceLoadingException;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 27/09/14
 * Time: 00:48
 * To change this template use File | Settings | File Templates.
 */
@ApplicationScoped
public class ServletResourceLoader implements ResourceLoader{
    private final ServletContext sc;

    @Inject
    public ServletResourceLoader(ServletContext sc){
        this.sc=sc;
    }

    @Override
    public Class<?> classForName(String name) {
        try {
            return sc.getClassLoader().loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new ResourceLoadingException("class "+name+" not found.", e);
        }
    }

    @Override
    public URL getResource(String name) {
        try {
            return sc.getResource(name);
        } catch (MalformedURLException e) {
            throw new ResourceLoadingException("Resource "+name+" is not a valid name.", e);
        }
    }

    @Override
    public Collection<URL> getResources(String name) {
            LinkedList<URL> ret = new LinkedList<>();
            for(String each:sc.getResourcePaths(name)){
                try {
                    ret.addLast(new URL(each));
                } catch (MalformedURLException e) {
                    throw new ResourceLoadingException("Resource "+name+" translated to "+each+" is not a valid name.", e);
                }
            }
            return ret;
    }

    @Override
    public void cleanup() {
    }
}
