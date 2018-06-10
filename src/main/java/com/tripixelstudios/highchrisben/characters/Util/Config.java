package com.tripixelstudios.highchrisben.characters.Util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

class Config extends YamlConfiguration {
    private JavaPlugin plugin;
    private String fileName;

    Config(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = (fileName + (fileName.endsWith(".yml") ? "" : ".yml"));
        createFile();
    }

    private void createFile() {
        try {
            File file = new File(plugin.getDataFolder(), fileName);
            if (!file.exists()) {
                if (plugin.getResource(fileName) != null) {
                    plugin.saveResource(fileName, false);
                } else {
                    save(file);
                }
            } else {
                load(file);
                save(file);
            }
        } catch (Exception ignored) {
        }
    }

    void save() {
        try {
            save(new File(plugin.getDataFolder(), fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
