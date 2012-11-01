package test.input;

import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.JoyInput;
import com.jme3.input.Joystick;
import com.jme3.input.controls.ActionListener;
import com.jme3.system.AppSettings;

public class TestXbox360Remote extends SimpleApplication implements ActionListener {

    public static void main(String[] args) {
        TestXbox360Remote app = new TestXbox360Remote();
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

        xbox360.setUpRemote(inputManager.getJoysticks()[0], inputManager);
        inputManager.addListener(this, xbox360.getButtons());
        inputManager.addListener(this, xbox360.getAxis());
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed) {
            System.out.println(name);
        }
    }

    enum xbox360 {

        A(0),
        B(1),
        X(2),
        Y(3),
        LeftShoulder(4),
        RightShoulder(5),
        LeftRightTriggerAxis(4),
        Back(6),
        Start(7),
        LeftStick(8),
        LeftThumbstickUpDownAxis(0),
        LeftThumbstickLeftRightAxis(1),
        RightStick(9),
        RightThumbstickUpDownAxis(2),
        RightThumbstickLeftRightAxis(3),
        DPadRightLeftAxis(JoyInput.AXIS_POV_X),
        DPadUpDownAxis(JoyInput.AXIS_POV_Y);
        private final int i;

        xbox360(Integer i) {
            this.i = i;

        }

        private int getID() {
            return i;
        }

        public static void setUpRemote(Joystick js, InputManager manager) {
            for (xbox360 b : xbox360.values()) {
                if (!b.toString().contains("Axis")) {
                    js.assignButton(b.toString(), b.getID());
                }
            }

            js.assignAxis("LeftThumbstickDown", "LeftThumbstickUp", LeftThumbstickUpDownAxis.getID());
            js.assignAxis("LeftThumbstickRight", "LeftThumbstickLeft", LeftThumbstickLeftRightAxis.getID());
            js.assignAxis("RightThumbstickDown", "RightThumbstickUp", RightThumbstickUpDownAxis.getID());
            js.assignAxis("RightThumbstickRight", "RightThumbstickLeft", RightThumbstickLeftRightAxis.getID());
            js.assignAxis("LeftTrigger", "RightTrigger", LeftRightTriggerAxis.getID());
            js.assignAxis("DPadRight", "DPadLeft", JoyInput.AXIS_POV_X);
            js.assignAxis("DPadUp", "DPadDown", JoyInput.AXIS_POV_Y);

            manager.setAxisDeadZone(0.2f);
        }

        public static String[] getButtons() {
            return new String[]{"A", "B", "X", "Y", "LeftShoulder", "RightShoulder", "Back", "Start", "LeftStick", "RightStick"};
        }

        public static String[] getAxis() {
            return new String[]{"LeftThumbstickUp", "LeftThumbstickDown", "LeftThumbstickLeft", "LeftThumbstickRight", "RightThumbstickUp", "RightThumbstickDown",
                        "RightThumbstickLeft", "RightThumbstickRight", "LeftTrigger", "RightTrigger", "DPadLeft", "DPadRight", "DPadUp", "DPadDown"};
        }
    }
}
