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
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.net.URL;
import java.util.logging.Logger;

public class JAXBHelper<E> {

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
            if(ns!=null && !ns.isEmpty()){
                return ns;
            }
        }

        ann = JAXBHelper.getAnnotation(classT.getPackage(), XmlSchema.class);
        if(ann!=null){
            String ns = ((XmlSchema)ann).namespace();
            if(ns!=null && !ns.isEmpty()){
                return ns;
            }
        }

        return "";
    }

    public static <T> Object toMarshallableObject(T element){
        final Class<?> classT = element.getClass();
        if(element instanceof JAXBElement
                || JAXBHelper.hasAnnotation(element.getClass(), XmlRootElement.class)){
            // Good to go.
            return element;
        } else {
            // Wrap it up in a (possibly dummy) JAXBElement
            final String nsURI = getNamespace(element.getClass());
            return new JAXBElement<>(new QName(nsURI, element.getClass().getSimpleName()), (Class<T>)classT, element);
        }
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


    public UnmarshalPromise<E> unmarshal() throws JAXBException {
        return unmarshal(rootElementClass, validateByDefault ? defaultValidationHandler : null);
    }

    public UnmarshalPromise<E> unmarshal(boolean validate) throws JAXBException {
        return unmarshal(rootElementClass, validate ? defaultValidationHandler : null);
    }

    public UnmarshalPromise<E> unmarshal(ValidationEventHandler validator) throws JAXBException {
        return unmarshal(rootElementClass, validator);
    }

    public <T> UnmarshalPromise<T> unmarshal(Class<T> t, boolean validate) throws JAXBException {
        return unmarshal(t, validate? defaultValidationHandler: null);
    }

    public <T> UnmarshalPromise<T> unmarshal(Class<T> t, ValidationEventHandler validator) throws JAXBException {
        Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
        if(null!=validator){
            unmarshaller.setEventHandler(validator);
            unmarshaller.setSchema(xmlSchema);
        }

        return new UnmarshalPromise(unmarshaller, t);
    }

    /**
     * Shortcut method for marshalling objects of one type, with no possibility to reuse internal marshaller for other types.
     * @param element
     * @param options
     * @return
     */
    // perhaps a bit faster than marshallFragment?
    public MarshalPromise marshal(E element, MarshallOption... options) throws JAXBException {
        return new MarshalPromise(createMarshaller(options), element);
    }

    /**
     * Shortcut method for marshalling objects of one type, with no posibility to reuse internal marshaller for other types.
     * @param element
     * @param options
     * @return
     */
    public <T> MarshalPromise marshalFragment(T element, MarshallOption... options) throws JAXBException {
        return new MarshalPromise(
                createMarshaller(options),
                toMarshallableObject(element));
    }

	public Marshaller createMarshaller(MarshallOption... options) throws JAXBException {
		Marshaller m=jaxbCtx.createMarshaller();
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        boolean validate = validateByDefault;
		for(MarshallOption option:options){
            switch(option){
			    case Validate:
                    validate = true;
                    break;
                case NoValidate:
                    validate = false;
                    break;
                case Fragment:
                    m.setProperty(Marshaller.JAXB_FRAGMENT, true);
                    break;
                case Format:
                    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    break;
            }
		}

        if(validate){
            m.setEventHandler(defaultValidationHandler);
            m.setSchema(xmlSchema);
        }

		return m;
	}

}
