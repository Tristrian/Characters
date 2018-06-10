package com.tripixelstudios.highchrisben.characters.Util;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class Character {
    private final PluginConfig pluginConfig = new PluginConfig();
    private Player player;
    private Config charData;
    private int currentChar;

    public Character(Player player) {
        this.player = player;
        if (checkData(player)) {
            charData = getCharData();
            currentChar = charData.getInt("character-selected");
        } else {
            setupChar();
        }
    }

    private boolean checkData(Player player) {
        return check(pluginConfig.getPlugin(), File.separator + "CharacterData" + File.separator + player.getUniqueId());
    }

    private Config getCharData() {
        return new Config(pluginConfig.getPlugin(), File.separator + "CharacterData" + File.separator + player.getUniqueId());
    }

    public void reset() {
        for (String charField : pluginConfig.getCharFields()) {
            charData.set(charField, "Empty");
        }
    }

    public void set(String charField, Object value) {
        charData.set("characters." + charField.toLowerCase() + "." + currentChar, value);
        charData.save();
    }

    public String getString(String charField) {
        return charData.getString("characters." + charField + "." + currentChar);
    }

    public int getInt(String charField){
        return charData.getInt("characters." + charField + "." + currentChar);
    }

    public int getCurrentChar() {
        return currentChar;
    }

    private void setupChar() {
        if (player.hasPermission(pluginConfig.getString("characters-permission"))) {
            charData = getCharData();
            charData.addDefault("character-selected", 1);
            for (int character = 1; character <= pluginConfig.getInt("characters-cards"); character++) {
                for (String charField : pluginConfig.getCharFields()) {
                    charData.addDefault("characters." + charField.toLowerCase() + "." + character, "Empty");
                }
                if (new Skills(player).getEnabled()) {
                    charData.addDefault("characters.hitpoints." + character, pluginConfig.getInt("hitpoints-default"));
                    charData.addDefault("characters.magicka." + character, pluginConfig.getInt("magicka-default"));
                    charData.addDefault("characters.stamina." + character, pluginConfig.getInt("stamina-default"));
                }
            }
            charData.options().copyDefaults(true);
            charData.save();
            currentChar = charData.getInt("character-selected");
        }
    }

    private boolean check(Plugin plugin, String fileName) {
        fileName = fileName + (fileName.endsWith(".yml") ? "" : ".yml");
        try {
            File file = new File(plugin.getDataFolder(), fileName);
            if (!file.exists()) {
                return false;
            }
            return true;
        } catch (Exception ignored) {
        }

        return false;
    }
}
