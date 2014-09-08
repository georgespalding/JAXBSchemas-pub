package se.omegapoint.xml.jaxb.util;

import javax.xml.bind.JAXBException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;
import sample.schema.two.*;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 07/09/14
 * Time: 23:11
 * To change this template use File | Settings | File Templates.
 */
public class SchemaTwoTest {
    private static JAXBHelper<ARootElementWithoutNs> jaxbHelper;

    @BeforeClass
    public static void createJAXBHelper() throws SAXException, JAXBException {
        jaxbHelper = new JAXBHelper(
                ARootElementWithoutNs.class,
                JAXBHelper.createSchema(
                        "META-INF/schema/two/schema1.xsd",
                        "META-INF/schema/two/schema2.xsd",
                        "META-INF/schema/two/schema3.xsd"
                        ),
                true,
                ObjectFactory.class,
                ARootElementWithNs.class,
                ARootElementWithoutNs.class,
                AnElementWithNs.class,
                AnElementWithoutNs.class);
    }

    @Test
    public void testMarshallFragment_ARootElementWithNs() throws JAXBException {
        Assert.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<aRootElementWithNs" +
                        " xmlns:ns2=\"http://omegapoint.se/packagens\"" +
                        " xmlns=\"http://omegapoint.se/arootelementwithns\"/>\n",
                jaxbHelper.marshalFragment(
                        new ARootElementWithNs(),
                        MarshallOption.Format,
                        MarshallOption.Validate).getString());
    }

    @Test
    public void testMarshall_ARootElementWitouthNs() throws JAXBException {
        Assert.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<ns2:aRootElementWithoutNs" +
                        " xmlns:ns2=\"http://omegapoint.se/packagens\"" +
                        " xmlns=\"http://omegapoint.se/arootelementwithns\"/>\n",
                jaxbHelper.marshal(new ARootElementWithoutNs(),
                        MarshallOption.Format,
                        MarshallOption.Validate).getString());
    }

    @Test
    public void testMarshallFragment_AnElementWithNs() throws JAXBException {
        Assert.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<ns3:AnElementWithNs xmlns:ns2=\"http://omegapoint.se/packagens\"" +
                        " xmlns=\"http://omegapoint.se/arootelementwithns\"" +
                        " xmlns:ns3=\"http://omegapoint.se/anelementwithns\"/>\n",
                jaxbHelper.marshalFragment(new AnElementWithNs(),
                        MarshallOption.Format,
                        MarshallOption.NoValidate).getString());
    }

    @Test
    public void testMarshallFragment_AnElementWithoutNs() throws JAXBException {
        Assert.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<ns3:AnElementWithoutNs" +
                        " xmlns:ns2=\"http://omegapoint.se/packagens\"" +
                        " xmlns=\"http://omegapoint.se/arootelementwithns\"" +
                        " xmlns:ns3=\"##default\"/>\n",
                jaxbHelper.marshalFragment(new AnElementWithoutNs(),
                        MarshallOption.Format,
                        MarshallOption.NoValidate).getString());
    }

}
