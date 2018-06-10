package com.tripixelstudios.highchrisben.characters.Commands;

import com.tripixelstudios.highchrisben.characters.Util.PlayerChat;
import com.tripixelstudios.highchrisben.characters.Util.PluginConfig;
import com.tripixelstudios.highchrisben.characters.Util.SenderChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.List;

public class RollingCommand implements CommandExecutor {
    private final PluginConfig pluginConfig = new PluginConfig();

    public boolean onCommand(CommandSender sender, Command command, String str, String[] args) {
        if (!(sender instanceof Player)) {
            new SenderChat(sender).headerformatmsg("not-player");
            return false;
        }

        Player player = (Player) sender;
        PlayerChat playerChat = new PlayerChat(player);

        if (!pluginConfig.getBoolean("rolling-enabled")) {
            playerChat.headerformatmsg("rolling-disabled");
            return false;
        }

        String[] rolling = getRolling(args);

        if (rolling.length < 2) {
            playerChat.headerformatmsg("roll-requires");
            return false;
        }
        if ((!pluginConfig.integer(rolling[0])) && (pluginConfig.integer(rolling[1]))) {
            playerChat.headerformatmsg("not-number");
            return false;
        }

        int dRoll = parseInt(rolling[0], pluginConfig.getInt("rolling-max-die"));
        int sRoll = parseInt(rolling[1], pluginConfig.getInt("rolling-max-side"));
        int mod = 0;

        if (args.length > 1) {
            if (!pluginConfig.integer(args[1])) {
                playerChat.headerformatmsg("not-number");
                return false;
            }
            mod = parseInt(args[1], pluginConfig.getInt("rolling-max-mod"));
        }

        if ((dRoll == Integer.MAX_VALUE) || (sRoll == Integer.MAX_VALUE) || (mod == Integer.MAX_VALUE) || (dRoll == 0) || (sRoll == 0)) {
            playerChat.headerformatmsg("roll-min-max");
            return false;
        }

        String modString = mod >= 0 ? "+" + mod : String.valueOf(mod);
        double range = pluginConfig.getDouble("rolling-range");
        if (pluginConfig.getBoolean("rolling-advanced")) {
            anMsg(player, range, pluginConfig.getString("rolling-advanced-message").replace("%displayname%", player.getDisplayName()).replace("%player%", player.getName()).replace("%dice%", dRoll + "d" + sRoll + modString));
            SecureRandom random = new SecureRandom();
            int total = 0;
            for (int i = 0; i < dRoll; i++) {
                int val = random.nextInt(sRoll) + 1;
                total += val;
                anMsg((Player) sender, range, pluginConfig.getString("rolling-result").replace("%result%", String.valueOf(val)));
            }
            anMsg((Player) sender, range, pluginConfig.getString("rolling-total").replace("%total%", String.valueOf(total + mod)));
            return false;
        }
        SecureRandom random = new SecureRandom();
        int total = 0;
        for (int i = 0; i < dRoll; i++) {
            int val = random.nextInt(sRoll) + 1;
            total += val;
        }
        anMsg(player, range, pluginConfig.getString("rolling-simple-message").replace("%displayname%", player.getDisplayName()).replace("%player%", player.getName()).replace("%result%", String.valueOf(total + mod)).replace("%dice%", dRoll + "d" + sRoll + modString));
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

    private int parseInt(String intAsString, int max) {
        try {
            return Math.min(Math.max(Integer.parseInt(intAsString), -max), max);
        } catch (NumberFormatException ignored) {
        }
        return Integer.MAX_VALUE;
    }

    private String[] getRolling(String[] args) {
        if ((args.length < 1) || (!args[0].contains("d"))) {
            return new String[]{pluginConfig.getString("rolling-default-die"), pluginConfig.getString("rolling-default-side")};
        }
        return args[0].split("d");
    }
}
