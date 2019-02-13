package online.christopherstocks.highchrisben.characters.Libs;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

class Config extends YamlConfiguration {
    private JavaPlugin instance;
    private String fileName;

    Config(JavaPlugin plugin, String fileName) {
        this.instance = plugin;
        this.fileName = (fileName + (fileName.endsWith(".yml") ? "" : ".yml"));
        createFile();
    }

    private void createFile() {
        try {
            File file = new File(instance.getDataFolder(), fileName);
            if (!file.exists()) {
                if (instance.getResource(fileName) != null) {
                    instance.saveResource(fileName, false);
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
            save(new File(instance.getDataFolder(), fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
