import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceFeature;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import org.junit.Test;
import com.moodykettle.webservice.helloworld.HelloWorld;
import com.moodykettle.webservice.helloworld.HelloWorldService;
import com.sun.xml.internal.ws.developer.JAXWSProperties;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 01/09/14
 * Time: 21:49
 * To change this template use File | Settings | File Templates.
 */
public class JaxWSTest {

    @Test
    public void testWs() throws IOException {
        HelloWorldService hws = new HelloWorldService();
        WebServiceFeature wsf;
        HelloWorld hw = hws.getHelloWorld();

        Map<String, Object> rq = ((BindingProvider)hw).getRequestContext();
        rq.put(JAXWSProperties.CONNECT_TIMEOUT, 9000);
        URL url=new URL(null, "http://www.dn.se", new Handler(Handler.sun,"http","https"));
        rq.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        //rq.put(JAXWSProperties.SSL_SOCKET_FACTORY, );
        //rq.put(JAXWSProperties.HTTP_EXCHANGE, new HttpExchange());

        System.err.println("!:" + hw.sayHello("Bill"));
    }
}
