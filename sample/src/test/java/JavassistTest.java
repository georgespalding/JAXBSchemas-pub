import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.Test;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 27/08/14
 * Time: 05:56
 * To change this template use File | Settings | File Templates.
 */
public class JavassistTest
        //extends com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper
        implements MethodHandler
{

    @Test
    public void testIt() throws JAXBException {

        Customer cust = new Customer();
        cust.firstName="hej";
        cust.lastName="hopp";

        JAXBContext jc = JAXBContext.newInstance(Customer.class);
        Marshaller marshaller = jc.createMarshaller();
        install(marshaller);
        marshaller.marshal(cust, System.out);
    }

    public void install(Marshaller m){
        try{
            ProxyFactory factory = new ProxyFactory();
            factory.setSuperclass(Class.forName("com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper"));

            Object proxy = factory.create(new Class<?>[0], new Object[0], this);
            m.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper",proxy);
        } catch (ClassNotFoundException|PropertyException|InvocationTargetException|NoSuchMethodException|InstantiationException|IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        System.out.println("invoke(self,"+thisMethod+","+proceed+","+ Arrays.toString(args)+")");
        try{
            Object result = getClass().getMethod(thisMethod.getName(),thisMethod.getParameterTypes()).invoke(this,args);
            System.out.println("result: "+result);
            return result;
        }catch (NoSuchMethodException nme) {
            Object result =  proceed.invoke(this, args);
            System.out.println("invoke(...): proceeded:"+Arrays.toString((Object[])result));
            return result;
        }
    }

    //@Override
    public String getPreferredPrefix(String namespaceUri, String suggesion, boolean required) {
        System.out.println("getPreferredPrefix("+namespaceUri+","+suggesion+","+required+")");
        if("strangeprotocol://that.is.unknown.io/george".equals(namespaceUri)){
            return "oompalompa";
        }else{
            return suggesion;
        }
    }

    //@Override
    public String[] getPreDeclaredNamespaceUris() {
        System.out.println("getPreDeclaredNamespaceUris");
        return new String[]{"strangeprotocol://that.is.unknown.io/george"};
    }

    //@Override
    public String[] getPreDeclaredNamespaceUris2() {
        System.out.println("getPreDeclaredNamespaceUris2");
        return getPreDeclaredNamespaceUris();
    }

    //@Override
    public String[] getContextualNamespaceDeclsNOTUSED() {
        System.out.println("getContextualNamespaceDecls");
        return getPreDeclaredNamespaceUris();
    }

}
