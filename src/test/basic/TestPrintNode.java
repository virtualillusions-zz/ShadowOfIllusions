/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.basic;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.spectre.app.debug.PrintNodeUtil;

/**
 *
 * @author Kyle
 */
public class TestPrintNode {

    public static void main(String[] args) {
        n.attachChild(new Node("CHARLIE"));
        n.attachChild(new Node("CHUCK"));
        ((Node) n.getChild(0)).attachChild(new Node("RITCHIE"));
        n.attachChild(new Geometry("BROCK"));
        n.getChild(0).addControl(new TestPrintNodeControl());
        n.getChild(1).addControl(new TestPrintNodeControl());
        PrintNodeUtil.printNode(n, true);
    }
    private static Node n = new Node("Bret");

    private static class TestPrintNodeControl extends AbstractControl {

        @Override
        protected void controlUpdate(float tpf) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
