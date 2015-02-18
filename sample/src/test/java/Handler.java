import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 01/09/14
 * Time: 22:32
 * To change this template use File | Settings | File Templates.
 */
public class Handler extends URLStreamHandler{
    public static final String sun="sun.net.www.protocol";

    final Map<String,HandlerHolder> handlers=new HashMap<>();

    private class HandlerHolder {
        private final URLStreamHandler urlStreamHandler;
        private final Method openUrlConnectionMethod;

        private HandlerHolder(URLStreamHandler urlStreamHandler, Method openUrlConnectionMethod) {
            this.openUrlConnectionMethod = openUrlConnectionMethod;
            this.urlStreamHandler = urlStreamHandler;
        }

        URLConnection openConnection(URL u, Proxy p) throws IOException {
            try {
                return (URLConnection)openUrlConnectionMethod.invoke(urlStreamHandler, u, p);
            } catch (IllegalAccessException e) {
                throw new IOException("Failed to openConnection("+u+","+p+")",e);
            } catch (InvocationTargetException e) {
                throw new IOException("Failed to openConnection("+u+","+p+")",e);
            }
        }
    }

    public Handler(String packageName,String... protos) throws IOException {

        for(String proto:protos){
            String handlerName = packageName+"."+proto+".Handler";
            try{
                Class<URLStreamHandler> handlerClass = (Class<URLStreamHandler>) Class.forName(handlerName);
                URLStreamHandler handler = handlerClass.newInstance();
                Method m = handlerClass.getDeclaredMethod("openConnection", URL.class, Proxy.class);
                m.setAccessible(true);
                handlers.put(proto, new HandlerHolder(handler, m));
            }catch(Exception e){
                throw new IOException("Failed to instantiate "+handlerName+" or access its openConnection method.", e);
            }
        }
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return openConnection(u,null);
    }

    @Override
    protected URLConnection openConnection(URL u, Proxy p) throws IOException {
        HandlerHolder hh = handlers.get(u.getProtocol());
        if(hh==null){
            throw new IOException("Unknown protocol '"+u.getProtocol()+"'. Was instance initialized correctly?");
        }

        return hh.openConnection(u, p);
    }


}
