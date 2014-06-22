/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.basic;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;

/**
 *
 * @author Kyle Williams
 */
public class RawInputTest extends SimpleApplication {

    public static void main(String[] args) {
        SimpleApplication app = new RawInputTest();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        getInputManager().addRawInputListener(new RawInputListener() {
            @Override
            public void beginInput() {
            }

            @Override
            public void endInput() {
            }

            @Override
            public void onJoyAxisEvent(JoyAxisEvent evt) {
                System.out.println(evt);
            }

            @Override
            public void onJoyButtonEvent(JoyButtonEvent evt) {
                System.out.println(evt);
            }

            @Override
            public void onMouseMotionEvent(MouseMotionEvent evt) {
                System.out.println(evt);
            }

            @Override
            public void onMouseButtonEvent(MouseButtonEvent evt) {
                System.out.println(evt);
            }

            @Override
            public void onKeyEvent(KeyInputEvent evt) {
                System.out.println(evt);
            }

            @Override
            public void onTouchEvent(TouchEvent evt) {
                System.out.println(evt);
            }
        });
    }
}
