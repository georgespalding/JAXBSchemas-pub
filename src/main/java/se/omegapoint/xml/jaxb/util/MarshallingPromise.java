package se.omegapoint.xml.jaxb.util;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 07/09/14
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public class MarshallingPromise {
    private final Marshaller marshaller;
    private final Object elem;

    public MarshallingPromise(Marshaller marshaller, Object elem) {
        this.marshaller = marshaller;
        this.elem = elem;
    }

    public void to(Result result) throws JAXBException {
        marshaller.marshal(elem, result);
    }

    public void to(OutputStream os) throws JAXBException {
        marshaller.marshal(elem, os);
    }

    public void to(File output) throws JAXBException {
        marshaller.marshal(elem, output);
    }

    public void to(Writer writer) throws JAXBException {
        marshaller.marshal(elem, writer);
    }

    public void to(ContentHandler handler) throws JAXBException {
        marshaller.marshal(elem, handler);
    }

    public void to(Node node) throws JAXBException {
        marshaller.marshal(elem, node);
    }

    public void to(XMLStreamWriter writer) throws JAXBException {
        marshaller.marshal(elem, writer);
    }

    public void to(XMLEventWriter writer) throws JAXBException {
        marshaller.marshal(elem, writer);
    }

    public String getString() throws JAXBException {
        StringWriter sw = new StringWriter();
        to(sw);
        return sw.toString();
    }

    public byte [] getByteArray() throws JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        to(baos);
        return baos.toByteArray();
    }
}
