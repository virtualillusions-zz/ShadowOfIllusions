/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.zay_es.basic;

import com.simsilica.es.EntityComponent;

public class TestComponent implements EntityComponent {

    private String value;

    public TestComponent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
