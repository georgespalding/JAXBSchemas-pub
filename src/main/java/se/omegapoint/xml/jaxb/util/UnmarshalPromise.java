package se.omegapoint.xml.jaxb.util;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
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
public class UnmarshalPromise<E> {
    private final Unmarshaller unmarshaller;
    private final Class<E> type;

    public UnmarshalPromise(Unmarshaller unmarshaller, Class<E> type) {
        this.unmarshaller = unmarshaller;
        this.type = type;
    }

    public E from(File f) throws JAXBException {
        return type.cast(unmarshaller.unmarshal(f));
    }

    public E from(InputStream is) throws JAXBException {
        return type.cast(unmarshaller.unmarshal(is));
    }

    public E from(Reader reader) throws JAXBException {
        return type.cast(unmarshaller.unmarshal(reader));
    }

    public E from(URL url) throws JAXBException {
        return type.cast(unmarshaller.unmarshal(url));
    }

    public E from(InputSource source) throws JAXBException {
        return type.cast(unmarshaller.unmarshal(source));
    }

    public E from(Node node) throws JAXBException {
        return type.cast(unmarshaller.unmarshal(node));
    }

    public <T> UnmarshalPromise<T> unmarshal(Class<T> type){
        return new UnmarshalPromise<>(unmarshaller, type);
    }
}
