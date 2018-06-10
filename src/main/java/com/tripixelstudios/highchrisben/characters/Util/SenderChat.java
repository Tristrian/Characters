package com.tripixelstudios.highchrisben.characters.Util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SenderChat {
    private final PluginConfig pluginConfig = new PluginConfig();
    private CommandSender sender;

    public SenderChat(CommandSender sender) {
        this.sender = sender;
    }

    private String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private String formatConfig(String path) {
        return ChatColor.translateAlternateColorCodes('&', pluginConfig.getString(path));
    }

    public void headerformatmsg(String message) {
        this.sender.sendMessage(formatConfig("header") + " " + formatConfig(message));
    }

    public void headermsg(String message) {
        this.sender.sendMessage(formatConfig("header") + " " + format(message));
    }

    public void sendMessage(String message) {
        this.sender.sendMessage(format(message));
    }
}
