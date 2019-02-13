package online.christopherstocks.highchrisben.characters.Ext;

import online.christopherstocks.highchrisben.characters.Libs.PluginConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Updater {
    private URL checkURL;
    private String newVersion;
    private JavaPlugin plugin;

    public Updater() {
        this.plugin = new PluginConfig().getInstance();
        this.newVersion = plugin.getDescription().getVersion();
        try {
            this.checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=45142");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String getLatestVersion() {
        return newVersion;
    }

    public boolean checkForUpdates() throws Exception {
        URLConnection con = checkURL.openConnection();
        this.newVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
        return !plugin.getDescription().getVersion().equals(newVersion);
    }
}
