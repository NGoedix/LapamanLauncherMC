package mainLauncher.updater;

import mainLauncher.Launcher;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.util.Date;

public class makeUpdate {

    private static final String REMOTE_URL = "https://github.com/NGoedix/LapamanClientFiles.git";
    private Updater updater;

    public makeUpdate(Updater updater) {
        this.updater = updater;
    }

    public void start() {
        // Print information of the download.
        this.printInfo();
        // Begin the counter.
        long startTime = System.currentTimeMillis();
        try {
            // Download the files.
            downloadClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Finish the counter.
        long finalTime = System.currentTimeMillis();

        // Get the time.
        long timeDuration = finalTime - startTime;
        System.out.println("Downloaded the files in " + timeDuration / 1000L + " seconds");
    }

    public void downloadClient() throws GitAPIException {

        // If the launcher fail you can remove the version.txt file and the files will be downloaded other time.
        Launcher.DIR.delete();

        // Download the files from the SECRET github repository.
        System.out.println("Downloading files from SECRET SERVER to " + Launcher.DIR);
        try (Git result = Git.cloneRepository()
                .setURI(REMOTE_URL)
                .setDirectory(Launcher.DIR)
                .call()) {
            System.out.println("Having repository: " + result.getRepository().getDirectory());
        } catch (Exception e) {
            System.out.println("Failed updating the client. Check if the folder exists.");
        }
    }

    // Future function to pull (update the client).
    public void updateClient() {

    }

    private void printInfo() {
        // Logs
        System.out.println("Updater 1.0 by Goedix");
        System.out.printf("Current time is %s\n", new Object[]{(new Date(System.currentTimeMillis())).toString()});
        System.out.println("Starting updating...");
        System.out.printf("    Output Dir: %s\n", new Object[]{this.updater.getOutputFolder().getAbsolutePath()});
    }
}
