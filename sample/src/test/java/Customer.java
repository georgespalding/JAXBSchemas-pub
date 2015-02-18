import javax.xml.bind.annotation.*;

@XmlRootElement(name = "FunnyCustomer",namespace = "strangeprotocol://that.is.unknown.io/george")
@XmlAccessorType(XmlAccessType.FIELD)
public class Customer {

    @XmlAttribute
    int id;

    String firstName;

    String lastName;

}