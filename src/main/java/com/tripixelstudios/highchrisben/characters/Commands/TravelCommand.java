package com.tripixelstudios.highchrisben.characters.Commands;

import com.tripixelstudios.highchrisben.characters.Util.PlayerChat;
import com.tripixelstudios.highchrisben.characters.Util.PluginConfig;
import com.tripixelstudios.highchrisben.characters.Util.SenderChat;
import com.tripixelstudios.highchrisben.characters.Util.Travel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TravelCommand implements CommandExecutor {
    private final PluginConfig pluginConfig = new PluginConfig();

    public boolean onCommand(CommandSender sender, Command command, String str, String[] args) {
        if (!(sender instanceof Player)) {
            new SenderChat(sender).headerformatmsg("not-player");
            return false;
        }

        Player player = (Player) sender;
        PlayerChat playerChat = new PlayerChat(player);

        if (!(pluginConfig.getBoolean("travel-enabled"))) {
            playerChat.headerformatmsg("travel-disabled");
            return false;
        }

        if (!player.hasPermission(pluginConfig.getString("characters-permission"))) {
            playerChat.headerformatmsg("no-permission");
            return false;
        }

        if (args.length == 0) {
            playerChat.headerformatmsg("travel-command");
            return false;
        }
        Travel travel = new Travel(player);
        String speed = args[0].toLowerCase();
        if (!travel.isSpeed(speed)){
            playerChat.headermsg(pluginConfig.getString("travel-methods").replace("%input%", pluginConfig.buildList(pluginConfig.getStringList("travel"))));
            return false;
        }
        travel.setSpeed(speed);
        playerChat.headerformatmsg("travel-" + speed + "-message");
        return true;
    }
}
