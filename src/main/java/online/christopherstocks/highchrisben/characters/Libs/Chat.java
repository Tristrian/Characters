package online.christopherstocks.highchrisben.characters.Libs;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Chat {
    private PluginConfig pluginConfig = new PluginConfig();
    private Player player;
    private CommandSender sender;

    public Chat(CommandSender sender) {
        this.sender = sender;
        this.player = null;
    }

    public Chat(Player player) {
        this.sender = null;
        this.player = player;
    }

    public void sendMessage(String message, Player targetData) {
        if (sender != null) {
            sender.sendMessage(format(message, targetData));
        } else {
            player.sendMessage(format(message, targetData));
        }
    }

    private String format(String input, Player targetData) {
        if (pluginConfig.getBoolean("papi") && player != null) {
            return ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(targetData, input));
        } else {
            return ChatColor.translateAlternateColorCodes('&', input);
        }
    }
}
