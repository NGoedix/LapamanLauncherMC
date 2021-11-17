package mainLauncher;

import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.ProcessLogManager;
import mainLauncher.updater.Updater;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Launcher {

    // Vars to the ProgressBar
    private static AtomicLong numberOfTotalDownloadedBytes = new AtomicLong();
    private static AtomicLong numberOfTotalBytesToDownload = new AtomicLong();

    // Game information
    public static final GameVersion VERSION = new GameVersion("1.12.2", GameType.V1_8_HIGHER);
    public static final GameInfos INFOS = new GameInfos("lapaman", VERSION, new GameTweak[] {GameTweak.FORGE});
    public static final File DIR = INFOS.getGameDir();
    public static AuthInfos authInfos;

    // Process and thread
    private static Thread updateThread;
    public static Process p;

    // Authentication of user to open the game.
    public static void auth(String username) {
        authInfos = new AuthInfos(username, "null", "null");
    }

    public static void update() throws Exception {
        Updater up = new Updater(DIR);

        // Get the bytes of the repo (To the Progress bar)
        // up.getBytes();

        // Check the version to update or not the client.
        int versionLocal = 0;
        int versionServer = up.getVersion();

        File version = new File(DIR + "\\version.txt");

        // If the file exist compare the versions
        if(version.exists()) {
            Scanner sc = new Scanner(version);
            versionLocal = sc.nextInt();
        }

        if(versionServer != versionLocal || !version.exists()) {
            //TODO
            // Thread to update the Progress Bar
            updateThread = new Thread() {
                private int val;
                private int max;

                public void run() {
                    while(!isInterrupted()) {
                        Main.getInstance().getLauncherPanel().getProgressBar().setVisible(true);
                        this.val = (int)(numberOfTotalDownloadedBytes.get() / 1000L);
                        this.max = (int)(numberOfTotalBytesToDownload.get() / 1000L);
                        Main.getInstance().getLauncherPanel().getProgressBar().setMaximum(max);
                        Main.getInstance().getLauncherPanel().getProgressBar().setValue(val);
                    }
                }
            };
            // Start the thread and the update
            updateThread.start();
            up.start();
            updateThread.interrupt();
        }
    }

    public static void Launch() throws LaunchException {

        // Create the profile with the Authentication, the location of the Folder and the version of the minecraft.
        ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(INFOS, GameFolder.BASIC, authInfos);

        // Add the necessary VM arguments to run the Minecraft.
        profile.getVmArgs().addAll(Arrays.asList(Main.getInstance().getLauncherPanel().getRamSelector().getRamArguments()));
        profile.getVmArgs().add("-XX:+IgnoreUnrecognizedVMOptions");

        // Logs.
        System.out.println("Username: " + authInfos.getUsername());
        System.out.println("Commands: " + profile.getVmArgs());
        System.out.println("Args: " + profile.getArgs());

        // Launch the game.
        ExternalLauncher launcher = new ExternalLauncher(profile);
        p = launcher.launch();

        // Get the logs.
        ProcessLogManager manager = new ProcessLogManager(p.getInputStream(), new File(DIR, "logs.txt"));
        manager.start();

        try {
            Thread.sleep(500L);
            // Hide the Launcher.
            Main.getInstance().setVisible(false);
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Hide the Launcher.
        Main.getInstance().setVisible(false);
    }
}
