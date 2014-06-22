/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Json.Moxy;
    
import java.io.File;
import javax.xml.bind.*;
import java.util.List;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Kyle Williams
 */
public class JsonDemo2 {



    public static void main(String[] args) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(Customer.class);

        // Unmarshal from XML
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        File xml = new File("test/Json/Moxy/input.xml");
        Customer customer = (Customer) unmarshaller.unmarshal(xml);

        // Marshal to JSON
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty("eclipselink.media-type", "application/json");
        marshaller.marshal(customer, System.out);
    }

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
private static class Customer {

    @XmlAttribute
    private int id;

    private String firstName;

    @XmlElement(nillable=true)
    private String lastName;

    private List<String> email;

}


}
