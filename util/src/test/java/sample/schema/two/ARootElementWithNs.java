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
@XmlRootElement(namespace = "http://omegapoint.se/arootelementwithns")
@XmlType(namespace = "http://omegapoint.se/arootelementwithns")
@XmlAccessorType(XmlAccessType.FIELD)
public class ARootElementWithNs {
    public AnElementWithNs anElementWithNs;
    public AnElementWithoutNs anElementWithoutNs;
}
