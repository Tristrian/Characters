package com.tripixelstudios.highchrisben.characters.Util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerChat {
    private final PluginConfig pluginConfig = new PluginConfig();
    private Player player;

    public PlayerChat(Player player) {
        this.player = player;
    }

    private String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private String formatConfig(String path) {
        return ChatColor.translateAlternateColorCodes('&', pluginConfig.getString(path));
    }

    public void headerformatmsg(String message) {
        this.player.sendMessage(formatConfig("header") + " " + formatConfig(message));
    }

    public void headermsg(String message) {
        this.player.sendMessage(formatConfig("header") + " " + format(message));
    }

    public void sendMessage(String message) {
        this.player.sendMessage(format(message));
    }
}
