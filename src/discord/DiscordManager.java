package discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class DiscordManager {

    private DiscordRPC rcp;
    private DiscordEventHandlers handlers;
    private DiscordRichPresence presence;

    public DiscordManager() {
        // Get the Rich Presence instance.
        rcp = DiscordRPC.INSTANCE;
        handlers = new DiscordEventHandlers();
        // Application in discord.com/developers
        rcp.Discord_Initialize("908402070611177553", handlers, true, "");

        // Start the Rich Presence and configure it.
        presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.largeImageKey = "icon";
        presence.largeImageText = "Lapaman";
        //presence.smallImageKey = "";
        //presence.smallImageText = "";
        presence.details = "Lapaman - Official Launcher";
        presence.state = "In the menu";
        rcp.Discord_UpdatePresence(presence);
    }

    public void run() {
        // Update the presence.
        new Thread("RPC-Callback-Handler") {
            @Override
            public void run() {
                while(!Thread.currentThread().isInterrupted()) {
                    rcp.Discord_UpdatePresence(presence);
                    rcp.Discord_RunCallbacks();

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {}
                }
            }
        };
    }
}
