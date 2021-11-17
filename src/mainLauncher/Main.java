package mainLauncher;

import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.animation.Animator;
import fr.theshark34.swinger.util.WindowMover;
import discord.DiscordManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame implements ActionListener {

    private static Main instance;
    public static DiscordManager discord;
    private LauncherPanel launcherPanel;

    public Main() {
        // Configuration of the screen
        setTitle("Lapaman Launcher");
        setSize(1080, 720);
        setDefaultCloseOperation(3);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setBackground(Swinger.TRANSPARENT);
        setIconImage(Swinger.getResource("icon.png"));
        setContentPane(launcherPanel = new LauncherPanel());

        // Mouse Listener
        WindowMover mover = new WindowMover(this);
        addMouseListener(mover);
        addMouseMotionListener(mover);
        Animator.fadeInFrame(this, 10); // The animation when open the application (Java SE 8)
        setVisible(true);
    }

    public static void main(String[] args) {
        // Look for the Resources path and start the Launcher and start the Discord instance with the Launcher
        Swinger.setSystemLookNFeel();
        Swinger.setResourcePath("/mainLauncher/resources");
        instance = new Main();
        discord = new DiscordManager();
        // If the folder doesn't exist create it
        if(!Launcher.DIR.exists()) {
            Launcher.DIR.mkdir();
        }
    }

    public static Main getInstance() {
        return instance;
    }

    public LauncherPanel getLauncherPanel() {
        return launcherPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Main.getInstance().execute();
    }

    public void execute() {
        if(Launcher.p != null) {
            if(!Launcher.p.isAlive()) System.exit(0);
        }
    }
}
