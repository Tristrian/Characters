package com.tripixelstudios.highchrisben.characters.Util;

import com.tripixelstudios.highchrisben.characters.Main;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.Set;

public class PluginConfig {

    JavaPlugin getPlugin() {
        return Main.getPlugin();
    }

    private FileConfiguration getConfig() {
        return getPlugin().getConfig();
    }

    public void saveConfig() {
        getPlugin().saveConfig();
    }

    public void reloadConfig() {
        getPlugin().reloadConfig();
    }

    public void set(String path, Object value) {
        getConfig().set(path, value);
        reloadConfig();
    }

    public List<String> getStringList(String path) {
        return getConfig().getStringList(path);
    }

    public String getString(String path) {
        return getConfig().getString(path);
    }

    public int getInt(String path) {
        return getConfig().getInt(path);
    }

    public boolean getBoolean(String path) {
        return getConfig().getBoolean(path);
    }

    public double getDouble(String path) {
        return getConfig().getDouble(path);
    }

    private List<String> getFields(String path) {
        return getConfig().getStringList(path);
    }

    public List<String> getCharFields() {
        return getFields("charfields");
    }

    public List<String> getPermFields() {
        return getFields("permfields");
    }

    public List<String> getFixedFields() {
        return getFields("fixedfields");
    }

    public List<String> getPlaceholderFields() {
        return getFields("placeholderfields");
    }

    public File getMessagesFile(){
        return new File(getPlugin().getDataFolder(), "messages.yml");
    }

    public String getValue(List<String> list, String value) {
        for (String valueItem : list) {
            if (value.equalsIgnoreCase(valueItem)) {
                return valueItem;
            }
        }
        return "Empty";
    }

    public String build(String[] args, int index) {
        StringBuilder input = new StringBuilder();
        for (int i = index; i < args.length; i++) {
            if (i + 1 == args.length) {
                input.append(args[i]);
            } else
                input.append(args[i]).append(" ");
        }
        return input.toString();
    }

    public String buildList(List<String> args) {
        StringBuilder input = new StringBuilder();
        for (int i = 0; i < args.size(); i++) {
            if (i + 1 == args.size()) {
                input.append(StringUtils.capitalize(args.get(i)));

            } else if (i + 2 == args.size()) {
                input.append(StringUtils.capitalize(args.get(i))).append(" ").append("or").append(" ");
            } else
                input.append(StringUtils.capitalize(args.get(i) + "," + " "));
        }
        return input.toString();
    }

    //Leftover Methods from CharacterUtil
    public boolean integer(String string) {
        try {
            int num = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException ignored) {
        }
        return false;
    }

    public Player getTarget(String targetName) {
        try {
            return Bukkit.getPlayer(targetName);
        } catch (Exception ignored) {
        }
        return null;
    }

    public boolean verifyTarget(CommandSender sender, Player target) {
        if (target == null) {
            SenderChat senderChat = new SenderChat(sender);
            return true;
        }
        new Character(target);
        return false;
    }
}
