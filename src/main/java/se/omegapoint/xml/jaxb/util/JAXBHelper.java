package se.omegapoint.xml.jaxb.util;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.XmlSchema;
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
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JAXBHelper<OF,E> {
	
	public static XMLInputFactory xif=XMLInputFactory.newFactory();
	
    static {
        xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
        xif.setProperty(XMLInputFactory.IS_VALIDATING, true);
    }

    // Note! Subclasses will be another category!!!
    private Logger log =  Logger.getLogger(getClass().getName());

    public final OF objectFactory;
    public final Class<E> rootElementClass;
    public final JAXBContext jaxbCtx;
    public final Schema xmlSchema;
    private final ValidationEventHandler defaultValidationHandler=new DefaultValidationEventHandler();

    public JAXBHelper(OF objectFactory,Class<E> rootElementClass, String... schemaResourcePaths) throws JAXBException, SAXException {
        this.objectFactory = objectFactory;
        this.rootElementClass = rootElementClass;

        jaxbCtx = JAXBContext.newInstance(objectFactory.getClass());
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        // För att få validering mot orginalschemat att fungera måste package-info.java raderas från den genererade koden
        Source [] sources = new Source[schemaResourcePaths.length];
        int i=0;
        for(String schemaResourcePath:schemaResourcePaths){
            URL resource=getClass().getClassLoader().getResource(schemaResourcePath);
            if(resource==null){
                throw new NullPointerException("XML Schema resource '"+schemaResourcePath+"' was not found in classpath.");
            }
            sources[i++] = new StreamSource(resource.toExternalForm());
        }
        xmlSchema = sf.newSchema(sources);
    }


    public E unmarshal(InputStream in) throws JAXBException, XMLStreamException {
        return unmarshal(in, rootElementClass, defaultValidationHandler);
    }

    public <T> T unmarshal(InputStream in, Class<T> elementClass, ValidationEventHandler validationEventHandler) throws JAXBException, XMLStreamException {
        return unmarshal(xif.createXMLStreamReader(in), elementClass, validationEventHandler);
    }

    public E unmarshal(byte [] incoming) throws JAXBException, XMLStreamException {
        return unmarshal(incoming, rootElementClass, defaultValidationHandler);
    }

    public E unmarshal(byte [] incoming, ValidationEventHandler validationEventHandler) throws JAXBException, XMLStreamException {
        return unmarshal(incoming, rootElementClass, validationEventHandler);
    }

    public <T> T unmarshal(byte [] incoming, Class<T> elementClass, boolean validate) throws JAXBException, XMLStreamException {
        return unmarshal(incoming,elementClass, validate?defaultValidationHandler:null);
    }

    public <T> T unmarshal(byte [] incoming, Class<T> elementClass, ValidationEventHandler validationEventHandler) throws JAXBException, XMLStreamException {
        return unmarshal(new ByteArrayInputStream(incoming),elementClass, validationEventHandler);
    }

    public E unmarshal(String incoming) throws JAXBException, XMLStreamException {
        return unmarshal(incoming, rootElementClass, defaultValidationHandler);
    }

    public E unmarshal(String incoming,ValidationEventHandler validationEventHandler) throws JAXBException, XMLStreamException {
        return unmarshal(incoming, rootElementClass, validationEventHandler);
    }

    public <T> T unmarshal(String incoming, Class<T> elementClass, boolean validate) throws JAXBException, XMLStreamException {
        return unmarshal(incoming,elementClass, validate?defaultValidationHandler:null);
    }


    public <T> T unmarshal(String incoming, Class<T> elementClass, ValidationEventHandler validationEventHandler) throws JAXBException, XMLStreamException {
        return unmarshal(xif.createXMLStreamReader(new StringReader(incoming)),elementClass, validationEventHandler);
    }


    public <T> T unmarshal(XMLStreamReader rawReader, Class<T> elementClass ,boolean validate) throws JAXBException {
        return unmarshal(rawReader, elementClass, validate?defaultValidationHandler:null);
    }


    public E unmarshal(XMLStreamReader rawReader) throws JAXBException, XMLStreamException {
        return unmarshal(rawReader, rootElementClass, defaultValidationHandler);
    }


    public <T> T unmarshal(XMLStreamReader rawReader, Class<T> elementClass ,ValidationEventHandler validationEventHandler) throws JAXBException {
        long start=System.currentTimeMillis();
        Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
        if(validationEventHandler != null){
            unmarshaller.setEventHandler(validationEventHandler);
            unmarshaller.setSchema(xmlSchema);
        }

        T result = unmarshaller.unmarshal(rawReader, elementClass).getValue();
        if(log.isLoggable(Level.CONFIG)){
            log.log(Level.CONFIG,"Unmarshalled '"+elementClass.getSimpleName()+"' from XMLStreamReader in "+(System.currentTimeMillis()-start)+"ms");
        }
        return result;
    }

    public Object unmarshalRaw(String incoming, boolean validate) throws JAXBException, XMLStreamException {
        return unmarshalRaw(xif.createXMLStreamReader(new StringReader(incoming)), validate);
    }

    public Object unmarshalRaw(XMLStreamReader rawReader, boolean validate) throws JAXBException {
        long start=System.currentTimeMillis();
        Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
        if(validate){
            unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
            unmarshaller.setSchema(xmlSchema);
        }

        Object result = unmarshaller.unmarshal(rawReader);
        if(result instanceof JAXBElement){
            result=((JAXBElement) result).getValue();
        }
        if(log.isLoggable(Level.CONFIG)){
            log.log(Level.CONFIG,"Unmarshalled '"+result.getClass().getSimpleName()+"' from XMLStreamReader in "+(System.currentTimeMillis()-start)+"ms");
        }
        return result;
    }

    public String marshal(E rootElement) throws JAXBException {
        return marshal(rootElement,true, false);
    }

    /**
     * For marshalling root elements constructed with the ObjectFactory.
     * @param wrappedRootElement
     * @return
     * @throws JAXBException
     */
    public String marshal(JAXBElement<E> wrappedRootElement) throws JAXBException {
        return marshal(wrappedRootElement,true, false);
    }

    public <T> String marshalFragment(T element) throws JAXBException {
        Class<?> classT = element.getClass();
        String nsURI = "";
        for(Annotation annotation: classT.getPackage().getAnnotations()){
            if(annotation.annotationType() == XmlSchema.class){
                nsURI = ((XmlSchema)annotation).namespace();
                break;
            }
		}
		
		return marshal(new JAXBElement<T>(new QName(nsURI, classT.getSimpleName()), (Class<T>)classT, element), false, true);
	}

	public <T> String marshalFragment(JAXBElement<T> element, boolean validate) throws JAXBException {
		return marshal(element,validate,true);
	}

	public <T> String marshal(T element, boolean validate) throws JAXBException {
		return marshal(element,validate,false);
	}

	public <T> String marshal(T element, boolean validate, boolean fragment) throws JAXBException {
    StringWriter sw=new StringWriter();
		marshal(element, validate, fragment, sw);
		return sw.toString();
	}

	public Marshaller createMarshaller(boolean validate, boolean fragment) throws JAXBException {
		Marshaller m=jaxbCtx.createMarshaller();
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		if(fragment){
			m.setProperty(Marshaller.JAXB_FRAGMENT, fragment);
		}

		if(validate){
			m.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
			m.setSchema(xmlSchema);
		}

		return m;
	}

	public <T> void marshal(T element, boolean validate, boolean fragment, Writer dest) throws JAXBException {
		long start=System.currentTimeMillis();
		Marshaller m=createMarshaller(validate, fragment);
		m.marshal(element, dest);

		if(log.isLoggable(Level.CONFIG)){
			log.log(Level.CONFIG,"Marshalled '"+rootElementClass.getSimpleName()+"' to String in "+(System.currentTimeMillis()-start)+"ms");
		}
	}

	public OF getObjectFactory(){
		return objectFactory;
	}

}
