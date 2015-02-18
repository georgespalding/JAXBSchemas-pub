import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 26/08/14
 * Time: 06:08
 * To change this template use File | Settings | File Templates.
 */
public class XmlDocFragmentTest {
    @Test
    public void testUnmar() throws XMLStreamException, JAXBException {
        XMLInputFactory xif = XMLInputFactory.newFactory();
        StreamSource xml = new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("input.xml"));
        XMLStreamReader xsr = xif.createXMLStreamReader(xml);
        xsr.nextTag();
        do {
            System.out.println(xsr.getPrefix()+":{"+xsr.getNamespaceURI()+"}"+xsr.getLocalName());
            xsr.nextTag();
        }while(!(xsr.getLocalName().equals("findCustomerResponse") && "http://service.jaxws.blog/".equals(xsr.getNamespaceURI())));
        xsr.nextTag();

        JAXBContext jc = JAXBContext.newInstance(Customer.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        JAXBElement<Customer> jb = unmarshaller.unmarshal(xsr, Customer.class);
        xsr.close();

        Customer customer = jb.getValue();
        System.out.println(customer.id);
        System.out.println(customer.firstName);
        System.out.println(customer.lastName);
    }
}
