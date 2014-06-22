
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Kyle Williams
 */
public class NewClass {

    public static void main(String[] args) {
        
        Object[] selectionValues = {"Pandas", "Dogs", "Horses"};
        Object selection = JOptionPane.showInputDialog(null, "What are your favorite animals?", "Zoo Quiz",
                JOptionPane.QUESTION_MESSAGE, null, selectionValues, "Dogs");
        System.out.println(selection);

        String bigList[] = new String[30];
        for (int i = 0; i < bigList.length; i++) {
            bigList[i] = Integer.toString(i);
        }
        selection = JOptionPane.showInputDialog(null, "Pick a printer", "Input",
                JOptionPane.QUESTION_MESSAGE, null, bigList, "Titan");
        System.out.println(selection);

        selection = JOptionPane.showInputDialog("Enter Input:");
        System.out.println(selection);
//
//
//
//        JOptionPane optionPane = new JOptionPane();
//        setUpSlider(optionPane);        
//        System.out.println("Input: " + optionPane.getInputValue());
//        System.exit(0);
    }

//    static void setUpSlider(final JOptionPane optionPane) {
//        JSlider slider = new JSlider();
//        slider.setMajorTickSpacing(1);
//        slider.setMaximum(5);
//        slider.setMinimum(-5);
//        slider.setValue(0);
//        final JLabel val = new JLabel();
//        val.setText(slider.getValue() + "");
//        slider.setPaintTicks(true);
//        slider.setPaintLabels(true);
//        ChangeListener changeListener = new ChangeListener() {
//
//            public void stateChanged(ChangeEvent changeEvent) {
//                JSlider theSlider = (JSlider) changeEvent.getSource();
//                if (!theSlider.getValueIsAdjusting()) {
//                    optionPane.setInputValue(new Integer(theSlider.getValue()));
//                    val.setText(theSlider.getValue() + "");
//                }
//            }
//        };
//        slider.addChangeListener(changeListener);
//        optionPane.setMessage(new Object[]{"Select a value: ", val, slider});
//        optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
//        optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
//        JDialog dialog = optionPane.createDialog(null, "My Slider");
//        dialog.setVisible(true);
//    }
//static void setUpSlider(final JOptionPane optionPane, final String message, final float init, final float maxPossible, final int tick, final int min, final int max) {
//        JSlider slider = new JSlider();
//        slider.setMajorTickSpacing(tick);
//        slider.setMaximum(max);
//        slider.setMinimum(min);
//        //0 is 0 5=1 slower<5 faster>5
//        int value = (int) (init / maxPossible * max);
//        slider.setValue(value);
//        final JLabel val = new JLabel(init + "");
//        slider.setPaintTicks(true);
//        slider.setPaintLabels(true);
//        optionPane.setInputValue(new Integer(slider.getValue()));
//        ChangeListener changeListener = new ChangeListener() {
//
//            @Override
//            public void stateChanged(ChangeEvent changeEvent) {
//                JSlider theSlider = (JSlider) changeEvent.getSource();
//                if (!theSlider.getValueIsAdjusting()) {
//                    optionPane.setInputValue(new Integer(theSlider.getValue()));
//                    val.setText((maxPossible * theSlider.getValue()) / max + "");
//                }
//            }
//        };
//        slider.addChangeListener(changeListener);
//        optionPane.setMessage(new Object[]{"Select a value: ", val, slider});
//        optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
//        optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
//        JDialog dialog = optionPane.createDialog(null, message);
//        dialog.setVisible(true);
//    }
}
