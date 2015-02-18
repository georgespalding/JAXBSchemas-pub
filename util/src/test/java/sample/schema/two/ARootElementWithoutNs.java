package sample.schema.two;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created with IntelliJ IDEA.
 * User: geospa
 * Date: 07/09/14
 * Time: 22:45
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class ARootElementWithoutNs {
    public AnElementWithoutNs anElementWithoutNs;
    public AnElementWithNs anElementWithNs;
}
