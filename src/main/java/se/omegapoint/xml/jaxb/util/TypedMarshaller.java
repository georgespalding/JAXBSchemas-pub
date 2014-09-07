package se.omegapoint.xml.jaxb.util;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import java.lang.annotation.Annotation;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 07/09/14
 * Time: 20:58
 * To change this template use File | Settings | File Templates.
 */
public class TypedMarshaller<E> {
    private final Marshaller marshaller;

    public TypedMarshaller(Marshaller marshaller, Class<E> type) {
        this.marshaller = marshaller;
    }

    public Marshaller getMarshaller() {
        return marshaller;
    }

    public MarshallingPromise marshall(E element){
        return new MarshallingPromise(marshaller, element);
    }

    public <T> MarshallingPromise marshallFragment(T element) throws JAXBException {
        Class<?> classT = element.getClass();
        if(element instanceof JAXBElement
                || JAXBHelper.hasAnnotation(element.getClass(), XmlRootElement.class)){
            // Good to go.
            return new MarshallingPromise(marshaller, element);
        } else {
            // Wrap it up in a (possibly dummy) JAXBElement
            String nsURI = JAXBHelper.getNamespace(element.getClass());
            JAXBElement<T> wrapper = new JAXBElement<>(new QName(nsURI, getClass().getSimpleName()), (Class<T>)classT, element);
            return new MarshallingPromise(marshaller, wrapper);
        }
    }

}