package se.omegapoint.xml.jaxb.util;

import javax.xml.bind.JAXBException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.xml.sax.SAXException;
import sample.schema.one.ARootElementWithNamespace;
import sample.schema.one.ARootElementWithoutNamespace;
import sample.schema.one.AnElementWithNamespace;
import sample.schema.one.AnElementWithoutNamespace;
import sample.schema.one.ObjectFactory;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 07/09/14
 * Time: 23:11
 * To change this template use File | Settings | File Templates.
 */
public class SchemaOneTest {
    private static JAXBHelper<ARootElementWithNamespace> jaxbHelper;

    @BeforeClass
    public static void createJAXBHelper() throws SAXException, JAXBException {
        jaxbHelper = new JAXBHelper(
                ARootElementWithNamespace.class,
                JAXBHelper.createSchema(
                        "META-INF/schema/one/schema1.xsd",
                        "META-INF/schema/one/schema2.xsd",
                        "META-INF/schema/one/schema3.xsd"),
                true,
                ObjectFactory.class,
                ARootElementWithNamespace.class,
                ARootElementWithoutNamespace.class,
                AnElementWithNamespace.class,
                AnElementWithoutNamespace.class);
    }

    @Test
    public void testMarshallARootElementWithNamespace() throws JAXBException {
        Assert.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ns2:aRootElementWithNamespace xmlns:ns2=\"http://omegapoint.se/arootelementwithnamespace\"/>",
                jaxbHelper.marshal(new ARootElementWithNamespace()).getString());
    }

    @Test
    public void testMarshallFragmentARootElementWithNamespace() throws JAXBException {
        Assert.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ns2:aRootElementWithNamespace xmlns:ns2=\"http://omegapoint.se/arootelementwithnamespace\"/>",
                jaxbHelper.marshalFragment(new ARootElementWithNamespace()).getString());
    }

    @Test
    public void testMarshallARootElementWithoutNamespace() throws JAXBException {
        Assert.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><aRootElementWithoutNamespace xmlns:ns2=\"http://omegapoint.se/arootelementwithnamespace\"/>",
                jaxbHelper.marshalFragment(new ARootElementWithoutNamespace()).getString());
    }

    @Test
    public void testMarshallAnElementWithoutNamespace() throws JAXBException {
        Assert.assertEquals(
                "<ns3:AnElementWithoutNamespace" +
                        " xmlns:ns2=\"http://omegapoint.se/arootelementwithnamespace\"" +
                        " xmlns:ns3=\"##default\"/>",
                jaxbHelper.marshalFragment(new AnElementWithoutNamespace(),
                        MarshallOption.NoValidate,
                        MarshallOption.Format,
                        MarshallOption.Fragment).getString());
    }

    @Test
    public void testMarshallAnElementWithNamespace() throws JAXBException {
        Assert.assertEquals(
                "<ns3:AnElementWithNamespace" +
                        " xmlns:ns2=\"http://omegapoint.se/arootelementwithnamespace\"" +
                        " xmlns:ns3=\"http://omegapoint.se/anelementwithnamespace\"/>",
                jaxbHelper.marshalFragment(new AnElementWithNamespace(),
                        MarshallOption.NoValidate,
                        MarshallOption.Format,
                        MarshallOption.Fragment).getString());
    }
}
