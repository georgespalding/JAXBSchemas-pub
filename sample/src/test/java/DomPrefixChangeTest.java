import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.math.BigDecimal;
import org.junit.Test;
import org.w3._2000._09.xmldsig_.ObjectType;
import org.w3._2000._09.xmldsig_.SignatureType;
import oasis.names.tc.saml._2_0.assertion.NameIDType;
import oasis.names.tc.saml._2_0.protocol.AuthnRequestType;
import oasis.names.tc.saml._2_0.protocol.ObjectFactory;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 27/08/14
 * Time: 22:02
 * To change this template use File | Settings | File Templates.
 */
public class DomPrefixChangeTest {
    @Test
    public void testChangeDomPrefix() throws JAXBException {
        AuthnRequestType req = new AuthnRequestType()
                .withIssuer(new NameIDType()
                        .withFormat("skojformatet")
                        .withValue("hemskt mycket hej"))
                .withSignature(new SignatureType()
                .withId("sigid").withObject(new ObjectType()
                        .withEncoding("joke")
                        .withContent(BigDecimal.TEN)));

        JAXBContext ctx = JAXBContext.newInstance(new ObjectFactory().getClass());
        Marshaller m =ctx.createMarshaller();
        m.marshal(req,System.out);

    }
}
