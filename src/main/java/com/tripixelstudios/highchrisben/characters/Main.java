package com.tripixelstudios.highchrisben.characters;

import com.tripixelstudios.highchrisben.characters.Commands.CharacterCommand;
import com.tripixelstudios.highchrisben.characters.Commands.RollingCommand;
import com.tripixelstudios.highchrisben.characters.Commands.SkillsCommand;
import com.tripixelstudios.highchrisben.characters.Commands.TravelCommand;
import com.tripixelstudios.highchrisben.characters.Events.PlayerClickListener;
import com.tripixelstudios.highchrisben.characters.Events.PlayerJoinListener;
import com.tripixelstudios.highchrisben.characters.Util.PluginConfig;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.PluginManager;

public class Main extends org.bukkit.plugin.java.JavaPlugin {
    private static Main plugin;

    public static Main getPlugin() {
        return plugin;
    }

    public void onEnable() {
        saveDefaultConfig();
        PluginManager pm = getServer().getPluginManager();
        plugin = this;
        getCommand("character").setExecutor(new CharacterCommand());
        getCommand("travel").setExecutor(new TravelCommand());
        getCommand("roll").setExecutor(new RollingCommand());
        getCommand("skill").setExecutor(new SkillsCommand());
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new PlayerClickListener(), this);
        PluginConfig pluginConfig = new PluginConfig();
        if (pluginConfig.getInt("characters-cards") <= 0) {
            pluginConfig.set("character-cards", 1);
            saveConfig();
        }
        new Metrics(this);
    }
}
