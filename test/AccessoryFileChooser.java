/*
Java Swing, 2nd Edition
By Marc Loy, Robert Eckstein, Dave Wood, James Elliott, Brian Cole
ISBN: 0-596-00408-7
Publisher: O'Reilly 
*/
// AccessoryFileChooser.java
//An example of the JFileChooser class in action with an accessory. This
//accessory (see AudioAccessory.java) will play simple audio files within
//the file chooser.
//

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AccessoryFileChooser extends JFrame {
  JFileChooser chooser = null;

  JLabel statusbar;

  public AccessoryFileChooser() {
    super("Accessory Test Frame");
    setSize(350, 200);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    Container c = getContentPane();
    c.setLayout(new FlowLayout());

    JButton accButton = new JButton("Accessory");
    statusbar = new JLabel("Output of your selection will go here");
    chooser = new JFileChooser();
    AudioAccessory aa = new AudioAccessory();
    chooser.setAccessory(aa);
    chooser.addPropertyChangeListener(aa); // to receive selection changes
    chooser.addActionListener(aa); // to receive approve/cancel button
                     // events

    accButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        int option = chooser.showOpenDialog(AccessoryFileChooser.this);
        if (option == JFileChooser.APPROVE_OPTION) {
          statusbar.setText("You chose "
              + ((chooser.getSelectedFile() != null) ? chooser
                  .getSelectedFile().getName() : "nothing"));
        } else {
          statusbar.setText("You canceled.");
        }
      }
    });
    c.add(accButton);
    c.add(statusbar);
  }

  public static void main(String args[]) {
    AccessoryFileChooser afc = new AccessoryFileChooser();
    afc.setVisible(true);
  }
}

//AudioAccessory.java
//An accessory for JFileChooser that lets you play music clips. Only the
//simple .au, .aiff and .wav formats available through the Applet sound
//classes can be played.
//

class AudioAccessory extends JPanel implements PropertyChangeListener,
    ActionListener {

  AudioClip currentClip;

  String currentName = "";

  JLabel fileLabel;

  JButton playButton, stopButton;

  public AudioAccessory() {
    // Set up the accessory. The file chooser will give us a reasonable
    // size.
    setLayout(new BorderLayout());
    add(fileLabel = new JLabel("Clip Name"), BorderLayout.NORTH);
    JPanel p = new JPanel();
    playButton = new JButton("Play");
    stopButton = new JButton("Stop");
    playButton.setEnabled(false);
    stopButton.setEnabled(false);
    p.add(playButton);
    p.add(stopButton);
    add(p, BorderLayout.CENTER);

    playButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (currentClip != null) {
          currentClip.stop();
          currentClip.play();
        }
      }
    });
    stopButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (currentClip != null) {
          currentClip.stop();
        }
      }
    });
  }

  public void propertyChange(PropertyChangeEvent e) {
    String pname = e.getPropertyName();
    if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(pname)) {
      // Ok, the user selected a file in the chooser
      File f = (File) e.getNewValue();

      // Make reasonably sure it's an audio file
      if ((f != null)
          && (f.getName().toLowerCase().endsWith(".au")
              || f.getName().toLowerCase().endsWith(".wav")
              || f.getName().toLowerCase().endsWith(".aif") || f
              .getName().toLowerCase().endsWith(".aiff"))) {
        setCurrentClip(f);
      } else {
        setCurrentClip(null);
      }
    }
  }

  public void setCurrentClip(File f) {
    if (currentClip != null) {
      currentClip.stop();
    }
    // Make sure we have a real file, otherwise, disable the buttons
    if ((f == null) || (f.getName() == null)) {
      fileLabel.setText("no audio selected");
      playButton.setEnabled(false);
      stopButton.setEnabled(false);
      return;
    }

    // Ok, seems the audio file is real, so load it and enable the buttons
    String name = f.getName();
    if (name.equals(currentName)) {
      // Same clip they just loaded...make sure the player is enabled
      fileLabel.setText(name);
      playButton.setEnabled(true);
      stopButton.setEnabled(true);
      return;
    }
    currentName = name;
    try {
      URL u = new URL("file:///" + f.getAbsolutePath());
      currentClip = Applet.newAudioClip(u);
    } catch (Exception e) {
      e.printStackTrace();
      currentClip = null;
      fileLabel.setText("Error loading clip.");
    }
    fileLabel.setText(name);
    playButton.setEnabled(true);
    stopButton.setEnabled(true);
  }

  public void actionPerformed(ActionEvent ae) {
    // Be a little cavalier here...we're assuming the dialog was just
    // approved or cancelled so we should stop any playing clip
    if (currentClip != null) {
      currentClip.stop();
    }
  }
}

           
         
    
    