package se.omegapoint.xml.jaxb.util;

import javax.xml.bind.JAXBException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.xml.sax.SAXException;
import sample.schema.one.ARootElementWithNamespace;
import sample.schema.one.ARootElementWithoutNamespace;
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
                ARootElementWithoutNamespace.class);
    }

    @Test
    public void testMarshallAnElementWithNamespace() throws JAXBException {
        Assert.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ns2:aRootElementWithNamespace xmlns:ns2=\"http://omegapoint.se/arootelementwithnamespace\"/>",
                jaxbHelper.marshall(new ARootElementWithNamespace()).getString());
    }

    @Test
    public void testMarshallFragmentAnElementWithNamespace() throws JAXBException {
        Assert.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ns2:aRootElementWithNamespace xmlns:ns2=\"http://omegapoint.se/arootelementwithnamespace\"/>",
                jaxbHelper.marshallFragment(new ARootElementWithNamespace()).getString());
    }

    @Test
    public void testMarshallAnElementWithoutNamespace() throws JAXBException {
        Assert.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><aRootElementWithoutNamespace xmlns:ns2=\"http://omegapoint.se/arootelementwithnamespace\"/>",
                jaxbHelper.marshallFragment(new ARootElementWithoutNamespace()).getString());
    }

}
