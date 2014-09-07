package se.omegapoint.xml.jaxb.util;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JAXBHelper<E> {

    public static enum MarshallOption{
        Validate,
        Fragment,
        Format;
    }

	public static XMLInputFactory xif=XMLInputFactory.newFactory();

    static {
        xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
        xif.setProperty(XMLInputFactory.IS_VALIDATING, true);
    }

    private final ValidationEventHandler defaultValidationHandler = new DefaultValidationEventHandler();

    public static Schema createSchema(String... schemaResourcePaths) throws SAXException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // För att få validering mot orginalschemat att fungera måste package-info.java raderas från den genererade koden
        // OM det är så att schemat saknar namespace
        Source [] sources = new Source[schemaResourcePaths.length];
        int i=0;
        for(String schemaResourcePath:schemaResourcePaths){
            URL resource=Thread.currentThread().getContextClassLoader().getResource(schemaResourcePath);
            if(resource==null){
                throw new NullPointerException("XML Schema resource '"+schemaResourcePath+"' was not found in classpath.");
            }
            sources[i++] = new StreamSource(resource.toExternalForm());
        }
        return sf.newSchema(sources);
    }

    public static Annotation getAnnotation(AnnotatedElement ae, Class annotationClass){
        //Assert arguments
        if(!annotationClass.isAnnotation()){
            throw new IllegalArgumentException("Supplied class '"+annotationClass.getName()+"' is not an annotation!");
        }

        for(Annotation annotation: ae.getAnnotations()){
            if(annotation.annotationType() == annotationClass){
                // TODO Jdk 8 supports multiple annotations of same type...
                return annotation;
            }
        }
        return null;
    }

    public static boolean hasAnnotation(AnnotatedElement ae, Class annotationClass){
        return null!=getAnnotation(ae, annotationClass);
    }

    public static String getNamespace(Class<?> classT){
        Annotation ann = JAXBHelper.getAnnotation(classT, XmlType.class);
        if(ann!=null){
            String ns = ((XmlType)ann).namespace();
            if(ns!=null && ns.isEmpty()){
                return ns;
            }
        }

        ann = JAXBHelper.getAnnotation(classT.getPackage(), XmlSchema.class);
        if(ann!=null){
            String ns = ((XmlSchema)ann).namespace();
            if(ns!=null && ns.isEmpty()){
                return ns;
            }
        }

        return "##default";
    }

    // Note! Subclasses will be another category!!!
    private Logger log =  Logger.getLogger(getClass().getName());

    public final Class<E> rootElementClass;
    public final JAXBContext jaxbCtx;
    public final Schema xmlSchema;
    public final boolean validateByDefault;

    public JAXBHelper(Class<E> rootElementClass, Schema schema, boolean validate,Class ... objectFactoryClasses) throws JAXBException, SAXException {
        for(Class each:objectFactoryClasses){
            if("ObjectFactory".equals(each.getSimpleName()) && !hasAnnotation(each, XmlRegistry.class)) {
                throw new IllegalArgumentException("Supplied Class '"+each.getName()+"' is not an @XmlRegistry. (Should be an ObjectFactory).");
            }
        }

        this.rootElementClass = rootElementClass;
        this.jaxbCtx = JAXBContext.newInstance(objectFactoryClasses);
        this.xmlSchema = schema;
        this.validateByDefault = validate;
    }


    public TypedUnmarshaller<E> unmarshaller() throws JAXBException {
        return unmarshaller(rootElementClass, validateByDefault? defaultValidationHandler: null);
    }

    public TypedUnmarshaller<E> unmarshaller(boolean validate) throws JAXBException {
        return unmarshaller(rootElementClass, validate? defaultValidationHandler: null);
    }

    public TypedUnmarshaller<E> unmarshaller(ValidationEventHandler validator) throws JAXBException {
        return unmarshaller(rootElementClass, validator);
    }

    public <T> TypedUnmarshaller<T> unmarshaller(Class<T> t) throws JAXBException {
        return unmarshaller(t, validateByDefault? defaultValidationHandler: null);
    }

    public <T> TypedUnmarshaller<T> unmarshaller(Class<T> t, boolean validate) throws JAXBException {
        return unmarshaller(t, validate? defaultValidationHandler: null);
    }

    public <T> TypedUnmarshaller<T> unmarshaller(Class<T> t, ValidationEventHandler validator) throws JAXBException {
        Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
        if(null!=validator){
            unmarshaller.setEventHandler(validator);
            unmarshaller.setSchema(xmlSchema);
        }
        return new TypedUnmarshaller(unmarshaller, t);
    }


    /**
     * Shortcut method for marshalling objects of one type, with no posibility to reuse internal marshaller for other types.
     * @param element
     * @param options
     * @return
     */
    // perhaps a bit faster than marshallFragment?
    public MarshallingPromise marshall(E element, MarshallOption... options) throws JAXBException {
        return new MarshallingPromise(createMarshaller(options), element);
    }

    /**
     * Shortcut method for marshalling objects of one type, with no posibility to reuse internal marshaller for other types.
     * @param element
     * @param options
     * @return
     */
    public <T> MarshallingPromise marshallFragment(T element, MarshallOption... options) throws JAXBException {
        Class<?> classT = element.getClass();
        Marshaller marshaller = createMarshaller(options);
        if(element instanceof JAXBElement
                || JAXBHelper.hasAnnotation(element.getClass(), XmlRootElement.class)){
            // Good to go.
            return new MarshallingPromise(marshaller, element);
        } else {
            // Wrap it up in a (possibly dummy) JAXBElement
            String nsURI = getNamespace(element.getClass());
            JAXBElement<T> wrapper = new JAXBElement<>(new QName(nsURI, getClass().getSimpleName()), (Class<T>)classT, element);
            return new MarshallingPromise(marshaller, wrapper);
        }
    }

    /**
     * This gives you a reusable
     * @param options
     * @return
     * @throws JAXBException
     */
    public TypedMarshaller<E> marshaller(MarshallOption... options) throws JAXBException {
        return marshaller(rootElementClass, options);
    }

    /**
     * Use this to marshall bits of xml originating from the associated schemas that in themselves are not a complete document.
     * @param <T> The type to be marshalled, can be a JAXBElement, or a Type
     * @param options options for the marshaller. Note: setting MarshallOption.Validate will fail if the element is not a top-level element in the schema
     * @return
     */
    public <T> TypedMarshaller<T> marshaller(Class<T> type, MarshallOption... options) throws JAXBException {
        return new TypedMarshaller<>(
                createMarshaller(options),
                type);
    }

	public Marshaller createMarshaller(MarshallOption... options) throws JAXBException {
		Marshaller m=jaxbCtx.createMarshaller();
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		for(MarshallOption option:options){
            switch(option){
			    case Validate:
                    m.setEventHandler(defaultValidationHandler);
                    m.setSchema(xmlSchema);
                    break;
                case Fragment:
                    m.setProperty(Marshaller.JAXB_FRAGMENT, true);
                    break;
                case Format:
                    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    break;
            }
		}

		return m;
	}

}
