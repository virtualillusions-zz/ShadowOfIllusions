/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spectre.app.debug;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.spectre.app.SpectreApplicationState;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Kyle D. Williams
 */
public class PrintNodeUtil {

    public static void printNode(Node node) {
        printNode(node, false);
    }

    /**
     * Print all children from this node on down
     */
    public static void printNode(Node node, boolean detailed) {
        String html = "<!DOCTYPE html><html>";

        html = html + graphNode(node, detailed);

        html = html + "</html>";
        try {
            File f = File.createTempFile("node", ".html");
            FileWriter fw = new FileWriter(f);
            fw.write(html);
            fw.flush();
            fw.close();
            Desktop.getDesktop().open(f);
        } catch (IOException ex) {
            SpectreApplicationState.log.error("Error writing html file", ex);
        }

    }

    private static String graphNode(Node parent, boolean detailed) {
        String type = !detailed ? "" : " (" + parent.getClass().getSimpleName() + ")";
        String s = parent.getName() + type;
        if (!parent.getChildren().isEmpty()) {
            s = s + "<ul>";
            for (Spatial child : parent.getChildren()) {
                if (child instanceof Node) {
                    s = s + "<li>" + graphNode((Node) child, detailed) + "</li>";
                } else {
                    String type2 = !detailed ? "" : " (" + child.getClass().getSimpleName() + ")";
                    s = s + "<li>" + child.getName() + type2 + "</li>";
                    s = s + graphControl(parent);
                }
            }
            s = s + "</ul>";
        }
        s = s + graphControl(parent);

        return s;
    }

    private static String graphControl(Spatial parent) {
        String s = "";
        int cont = parent.getNumControls();
        if (cont > 0) {
            s = s + "<br><ul><li>CONTROL LIST:<ul>";
            for (int i = 0; i < cont; i++) {
                Control c = parent.getControl(i);
                s = s + "<li>" + c.getClass().getSimpleName() + "</li>";
            }
            s = s + "</ul></li></ul><br>";
        }
        return s;
    }
}
