package mainLauncher.updater;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Updater {

    private File OutputFolder;
    private makeUpdate makeUpdate;

    private String URL_API_REPO = "https://api.github.com/repos/NGoedix/LapamanClientFiles";

    public Updater(File OutputFolder) {
        this.OutputFolder = OutputFolder;
    }

    // Start the download
    public void start() {
        this.makeUpdate = new makeUpdate(this);
        this.makeUpdate.start();
    }

    // Get the file OutputFolder
    public File getOutputFolder() {
        return OutputFolder;
    }

    // Get the version of the server files. The file is located on a *private server hosting*.
    public int getVersion() throws IOException {
        int result = 0;
        URL serverUrl = new URL("http://lapaman.c1.biz/version.txt"); // URL of the version txt
        HttpURLConnection con = (HttpURLConnection) serverUrl.openConnection();
        con.setRequestMethod("GET");

        // Make a GET request to the URL and get the version (integer value)
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result = Integer.parseInt(line);
            }

        }
        return result;
    }

    //TODO
    // Not finished
    public int getBytes() throws IOException {
        int result = 0;
        JSONObject resultLine;

        URL serverUrl = new URL(URL_API_REPO); // URL of the version txt
        HttpURLConnection con = (HttpURLConnection) serverUrl.openConnection();
        con.setRequestMethod("GET");

        // Make a GET request to the URL and get the version (integer value)
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                resultLine = new JSONObject();
                System.out.println(resultLine.get("size"));
            }

        }
        return result;
    }
}
