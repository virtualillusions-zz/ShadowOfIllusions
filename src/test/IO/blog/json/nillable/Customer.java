package test.IO.blog.json.nillable;

import javax.xml.bind.annotation.*;

import java.util.List;
import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Customer {

    @XmlAttribute
    private int id;
    private String firstName;
    @XmlElement(nillable = true)
    private String lastName;
    private List<String> email;
}