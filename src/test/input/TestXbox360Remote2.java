package test.input;

import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.JoyInput;
import com.jme3.input.Joystick;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.JoyAxisTrigger;
import com.jme3.input.controls.JoyButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.system.AppSettings;

public class TestXbox360Remote2 extends SimpleApplication implements AnalogListener, ActionListener {

    public static void main(String[] args) {
        TestXbox360Remote2 app = new TestXbox360Remote2();
        AppSettings settings = new AppSettings(true);
        settings.setUseJoysticks(true);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Joystick[] joysticks = inputManager.getJoysticks();
        if (joysticks == null) {
            throw new IllegalStateException("Cannot find any joysticks!");
        }
        String[] temp = new String[xbox360.values().length];
        for (int i = 0; i < temp.length; i++) {
            xbox360 t = xbox360.values()[i];
            temp[i] = t.toString();
            inputManager.addMapping(t.toString(), t.getTrigger());
        }
        xbox360.set360AxisDeadZone(inputManager);
        inputManager.addListener(this, temp);
    }

    public void onAnalog(String name, float isPressed, float tpf) {
        // System.out.println(name + " = " + isPressed / tpf);
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed) {
            System.out.println(name);

            //this.inputManager.getJoysticks()[0]..rumble(2);
        }
    }

    enum xbox360 {

        A(new JoyButtonTrigger(0, 0)),
        B(new JoyButtonTrigger(0, 1)),
        X(new JoyButtonTrigger(0, 2)),
        Y(new JoyButtonTrigger(0, 3)),
        LeftShoulder(new JoyButtonTrigger(0, 4)),
        LeftTrigger(new JoyAxisTrigger(0, 4, false)),
        RightShoulder(new JoyButtonTrigger(0, 5)),
        RightTrigger(new JoyAxisTrigger(0, 4, true)),
        Back(new JoyButtonTrigger(0, 6)),
        Start(new JoyButtonTrigger(0, 7)),
        LeftStick(new JoyButtonTrigger(0, 8)),
        LeftThumbstickUp(new JoyAxisTrigger(0, 0, true)),
        LeftThumbstickDown(new JoyAxisTrigger(0, 0, false)),
        LeftThumbstickLeft(new JoyAxisTrigger(0, 1, true)),
        LeftThumbstickRight(new JoyAxisTrigger(0, 1, false)),
        RightStick(new JoyButtonTrigger(0, 9)),
        RightThumbstickUp(new JoyAxisTrigger(0, 2, true)),
        RightThumbstickDown(new JoyAxisTrigger(0, 2, false)),
        RightThumbstickLeft(new JoyAxisTrigger(0, 3, true)),
        RightThumbstickRight(new JoyAxisTrigger(0, 3, false)),
        DPadLeft(new JoyAxisTrigger(0, JoyInput.AXIS_POV_X, true)),
        DpadRight(new JoyAxisTrigger(0, JoyInput.AXIS_POV_X, false)),
        DPadDown(new JoyAxisTrigger(0, JoyInput.AXIS_POV_Y, true)),
        DPadUp(new JoyAxisTrigger(0, JoyInput.AXIS_POV_Y, false));
        private final Trigger trigger;

        xbox360(Trigger trig) {
            this.trigger = trig;

        }

        public Trigger getTrigger() {
            return trigger;
        }

        public static void set360AxisDeadZone(InputManager manager) {
            manager.setAxisDeadZone(0.2f);
        }
    }
}
