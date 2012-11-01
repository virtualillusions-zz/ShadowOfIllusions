/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.IO.blog.json.nillable;

import java.io.File;
import javax.xml.bind.*;

public class Demo {

    public static void main(String[] args) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(Customer.class);

        // Unmarshal from XML
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        File xml = new File("src/test/IO/blog/json/nillable/input.xml");
        Customer customer = (Customer) unmarshaller.unmarshal(xml);

        // Marshal to JSON
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        long startTime = System.nanoTime();
        long endTime;
        try {
            marshaller.marshal(customer, System.out);
        } finally {
            endTime = System.nanoTime();
        }
        long duration1 = endTime - startTime;

        startTime = System.nanoTime();
        try {
            //marshaller.setProperty("eclipselink.media-type", "application/json");
            marshaller.marshal(customer, System.out);
        } finally {
            endTime = System.nanoTime();
        }
        long duration2 = endTime - startTime;

        System.out.printf("XML: %d \nJSN: %d \nJ-X: %d ", duration1,duration2,Math.abs(duration2-duration1));


    }
}