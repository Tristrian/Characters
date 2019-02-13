package online.christopherstocks.highchrisben.characters.Libs;

import online.christopherstocks.highchrisben.characters.Characters;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class PluginConfig {

    public JavaPlugin getInstance() {
        return Characters.getInstance();
    }

    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }

    public void reload() {
        getInstance().reloadConfig();
    }

    public String getVersion() {
        return getInstance().getDescription().getVersion();
    }

    public void set(String path, Object value) {
        if (value == null) {
            getConfig().set(path, "Empty");
            getInstance().saveConfig();
        } else {
            getConfig().set(path, value);
            getInstance().saveConfig();
        }
    }

    public boolean getBoolean(String path) {
        return getConfig().getBoolean(path);
    }

    public int getInt(String path) {
        return getConfig().getInt(path);
    }

    public String getString(String path) {
        return getConfig().getString(path);
    }

    public Double getDouble(String path) { return getConfig().getDouble(path);}

    public List<String> getStringList(String path) {
        return getConfig().getStringList(path);
    }

}
