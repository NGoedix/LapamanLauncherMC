package mainLauncher;

import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;
import fr.theshark34.swinger.textured.STexturedProgressBar;
import mega.MegaHandler;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class LauncherPanel extends JPanel implements SwingerEventListener {

    Image backgroundImage = Swinger.getResource("background.png");

    // Buttons and progress bar
    private STexturedButton play = new STexturedButton(Swinger.getResource("playButton.png"), Swinger.getResource("playButton_hover.png"));
    private STexturedButton options = new STexturedButton(Swinger.getResource("optionsButton.png"), Swinger.getResource("optionsButton_hover.png"));
    private STexturedProgressBar bar = new STexturedProgressBar(Swinger.getResource("loadingBar.png"), Swinger.getResource("loadingBar_full.png"));

    // Configuration files
    private RamSelector ramSelector = new RamSelector(new File(Launcher.DIR, "ram.properties"));
    private Saver saver = new Saver(new File(Launcher.DIR, "credentials.properties"));

    // Username field
    private JTextField jTextField = new JTextField(saver.get("username"));

    public LauncherPanel() {

        // Screen configuration.
        setLayout(null);
        setBackground(Swinger.TRANSPARENT);

        // Play button.
        play.addEventListener(this);
        play.setBounds(331, 452, 236, 92);
        play.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(play);

        // Options button.
        options.addEventListener(this);
        options.setBounds(575, 452, 180, 80);
        options.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(options);

        // Progress bar.
        bar.setBounds(492, 127, 318, 15);
        add(bar);

        // JTextField username.
        jTextField.setForeground(new Color(0, 0, 0));
        jTextField.setFont(new Font("Arial", 1, 20));
        jTextField.setCaretColor(new Color(0, 0, 0));
        jTextField.setOpaque(false);
        jTextField.setBorder(null);
        jTextField.setBounds(300, 337, 120, 40);
        add(jTextField);

    }

    @Override
    public void onEvent(SwingerEvent e) {
        // If the user press any button
        if (e.getSource() == play) {
            start();
        } else if (e.getSource() == options) {
            ramSelector.display();
        }
    }

    private void setFieldsEnabled(boolean enabled) {
        jTextField.setEnabled(enabled);
        play.setEnabled(enabled);
        options.setEnabled(enabled);
    }

    private void start() {
        // If the user press start.
        setFieldsEnabled(false);
        // Check if the user is valid.
        if (jTextField.getText().replaceAll(" ", "").length() < 5) {
            JOptionPane.showMessageDialog(this, "Debes escribir un nombre válido", "Error", JOptionPane.ERROR_MESSAGE);
            setFieldsEnabled(true);
            return;
        }

        // Begin the thread to update the Launcher and init it.
        Thread t = new Thread(() -> {
            File file = new File(System.getenv("APPDATA") + "\\JMPEG_LAPAMAN\\ffmpeg.exe");
            File parentFolder = new File(System.getenv("APPDATA") + "\\JMPEG_LAPAMAN");
            if (!parentFolder.exists()) {
                parentFolder.mkdir();
            }
            if (!file.exists()) {
                try {
                    MegaHandler mh = new MegaHandler("goedix+lapaman@wearehackerone.com", "Lapaman9876543210#");
                    mh.login();
                    mh.download("https://mega.nz/file/JONG3ZDS#r7HcUeCBUrVdr8s9seB6qenSRw6u_jyw5T0cUpP5rFU", System.getenv("APPDATA") + "\\JMPEG_LAPAMAN\\ffmpeg.exe");
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | IOException |
                         BadPaddingException | InvalidKeyException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                Runtime.getRuntime().exec("setx path \"" + System.getenv("PATH") + ";\"" + System.getenv("APPDATA") + "\\FFMPEG_LAPAMAN");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error agregando la variable de entorno de ffmpeg. Por favor, contacta con Goedix.");
                throw new RuntimeException(e);
            }

            Launcher.auth(jTextField.getText());

            try {
                Launcher.update();
            } catch (Exception e) {
                e.printStackTrace();
                setFieldsEnabled(true);
            }
            saver.set("username", jTextField.getText());
            ramSelector.save();
            try {
                Launcher.Launch();
            } catch (LaunchException e) {
                e.printStackTrace();
                setFieldsEnabled(true);
            }
        });
        t.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Paint the background png.
        super.paintComponent(g);
        Swinger.drawFullsizedImage(g, this, backgroundImage);
    }

    // The next version will have the ProgressBar
    public STexturedProgressBar getProgressBar() {
        return bar;
    }

    public RamSelector getRamSelector() {
        return ramSelector;
    }
}
