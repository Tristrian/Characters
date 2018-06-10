package com.tripixelstudios.highchrisben.characters.Commands;

import com.tripixelstudios.highchrisben.characters.Util.Character;
import com.tripixelstudios.highchrisben.characters.Util.PlayerChat;
import com.tripixelstudios.highchrisben.characters.Util.PluginConfig;
import com.tripixelstudios.highchrisben.characters.Util.SenderChat;
import com.tripixelstudios.highchrisben.characters.Util.Skills;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class SkillsCommand implements CommandExecutor {
    private final PluginConfig pluginConfig = new PluginConfig();

    public boolean onCommand(CommandSender sender, Command command, String str, String[] args) {
        if (!(sender instanceof Player)) {
            new SenderChat(sender).headerformatmsg("not-player");
            return false;
        }

        Player player = (Player) sender;
        PlayerChat playerChat = new PlayerChat(player);

        if (!(pluginConfig.getBoolean("skills-enabled"))) {
            playerChat.headerformatmsg("skills-disabled");
            return false;
        }

        if (!player.hasPermission(pluginConfig.getString("characters-permission"))) {
            playerChat.headerformatmsg("no-permission");
            return false;
        }

        if (args.length == 0) {
            playerChat.headerformatmsg("skills-command");
            return false;
        }

        Skills skills = new Skills(player);

        if (args.length <= 1) {
            playerChat.headerformatmsg("skills-command");
            return false;
        }

        if (args[0].equalsIgnoreCase("hitpoints")){
            if (!pluginConfig.integer(args[1])) {
                playerChat.headerformatmsg("not-number");
                return false;
            }
            skills.alterHitpoints(Integer.parseInt(args[1]));
            anMsg(player, 30, pluginConfig.getString("skills-alter").replace("%value%", args[1]).replace("%result%", skills.getHitpoints()).replace("%player%", player.getName()).replace("%type%", StringUtils.capitalize(args[0].toLowerCase())));
        }
        if (args[0].equalsIgnoreCase("magicka")) {
            if (!pluginConfig.integer(args[1])) {
                playerChat.headerformatmsg("not-number");
                return false;
            }
            skills.alterMagicka(Integer.parseInt(args[1]));
            anMsg(player, 30, pluginConfig.getString("skills-alter").replace("%value%", args[1]).replace("%result%", skills.getMagicka()).replace("%player%", player.getName()).replace("%type%", StringUtils.capitalize(args[0].toLowerCase())));

        }
        if (args[0].equalsIgnoreCase("stamina")) {
            if (!pluginConfig.integer(args[1])) {
                playerChat.headerformatmsg("not-number");
                return false;
            }
            skills.alterStamina(Integer.parseInt(args[1]));
            anMsg(player, 30, pluginConfig.getString("skills-alter").replace("%value%", args[1]).replace("%result%", skills.getStamina()).replace("%player%", player.getName()).replace("%type%", StringUtils.capitalize(args[0].toLowerCase())));
        }

        return false;
    }

    private void anMsg(Player player, double distance, String message) {
        List<Entity> entities = player.getNearbyEntities(distance, distance, distance);
        entities.add(player);
        for (Object entity : entities) {
            if ((entity instanceof Player)) {
                new PlayerChat((Player) entity).headermsg(message);
            }
        }
    }
}
