/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.IO.blog.json.nillable;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class PhoneNumber {

    @XmlAttribute
    private String type;
    @XmlValue
    private String number;
}
