package se.omegapoint.xml.jaxb.util;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 06/09/14
 * Time: 23:56
 * To change this template use File | Settings | File Templates.
 */
public class TypedUnmarshaller<T> {
    private final Unmarshaller unmarshaller;
    private final Class<T> type;

    public TypedUnmarshaller(Unmarshaller unmarshaller, Class<T> type) {
        this.unmarshaller = unmarshaller;
        this.type = type;
    }

    public Unmarshaller getUnmarshaller() {
        return unmarshaller;
    }

    public Class<T> getType() {
        return type;
    }

    public T unmarshal(File f) throws JAXBException {
        return type.cast(unmarshaller.unmarshal(f));
    }

    public T unmarshal(InputStream is) throws JAXBException {
        return type.cast(unmarshaller.unmarshal(is));
    }

    public T unmarshal(Reader reader) throws JAXBException {
        return type.cast(unmarshaller.unmarshal(reader));
    }

    public T unmarshal(URL url) throws JAXBException {
        return type.cast(unmarshaller.unmarshal(url));
    }

    public T unmarshal(InputSource source) throws JAXBException {
        return type.cast(unmarshaller.unmarshal(source));
    }

    public T unmarshal(Node node) throws JAXBException {
        return type.cast(unmarshaller.unmarshal(node));
    }

    public JAXBElement<T> unmarshalElem(Node node) throws JAXBException {
        return (JAXBElement<T>) unmarshaller.unmarshal(node);
    }

    public T unmarshal(Source source) throws JAXBException {
        return type.cast(unmarshaller.unmarshal(source));
    }

    public JAXBElement<T> unmarshalElem(Source source) throws JAXBException {
        return (JAXBElement<T>) unmarshaller.unmarshal(source);
    }

    public T unmarshal(XMLStreamReader reader) throws JAXBException {
        return type.cast(unmarshaller.unmarshal(reader));
    }

    public JAXBElement<T> unmarshalElem(XMLStreamReader reader) throws JAXBException {
        return (JAXBElement<T>) unmarshaller.unmarshal(reader);
    }

    public T unmarshal(XMLEventReader reader) throws JAXBException {
        return type.cast(unmarshaller.unmarshal(reader));
    }

    public JAXBElement<T> unmarshalElem(XMLEventReader reader) throws JAXBException {
        return (JAXBElement<T>) unmarshaller.unmarshal(reader);
    }

}
