package online.christopherstocks.highchrisben.characters;

import online.christopherstocks.highchrisben.characters.Commands.*;
import online.christopherstocks.highchrisben.characters.Ext.Placeholders;
import online.christopherstocks.highchrisben.characters.Ext.Updater;
import online.christopherstocks.highchrisben.characters.Libs.PluginConfig;
import online.christopherstocks.highchrisben.characters.Listeners.PlayerClick;
import online.christopherstocks.highchrisben.characters.Listeners.PlayerJoin;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Characters extends JavaPlugin {
    private static Characters instance;
    private PluginConfig pluginConfig = new PluginConfig();

    public static Characters getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoin(), this);
        pluginManager.registerEvents(new PlayerClick(), this);
        getCommand("characters").setExecutor(new online.christopherstocks.highchrisben.characters.Commands.Characters());
        getCommand("travel").setExecutor(new Travel());
        getCommand("karma").setExecutor(new Karma());
        getCommand("attribute").setExecutor(new Attribute());
        getCommand("levels").setExecutor(new Levels());
        getCommand("roll").setExecutor(new Roll());
        for (String race : pluginConfig.getStringList("races")) {
            for (String stat : pluginConfig.getStringList("stats")){
                pluginConfig.set(race + "." + stat, 0);
            }
        }
        for (String cclass : pluginConfig.getStringList("classes")) {
            for (String stat : pluginConfig.getStringList("stats")){
                pluginConfig.set(cclass + "." + stat, 0);
            }
        }
        new Metrics(this);
        if (pluginManager.isPluginEnabled("PlaceholderAPI")) {
            try {
                new Placeholders(this).hook();
                pluginConfig.set("papi", true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            pluginConfig.set("papi", false);
        }
        try {
            if (new Updater().checkForUpdates()){
                getLogger().log(Level.INFO, pluginConfig.getString("update-new"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
