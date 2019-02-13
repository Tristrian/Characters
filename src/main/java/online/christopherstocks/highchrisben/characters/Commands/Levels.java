package online.christopherstocks.highchrisben.characters.Commands;

import online.christopherstocks.highchrisben.characters.Libs.Character;
import online.christopherstocks.highchrisben.characters.Libs.Chat;
import online.christopherstocks.highchrisben.characters.Libs.Logic;
import online.christopherstocks.highchrisben.characters.Libs.PluginConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Levels implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        PluginConfig pluginConfig = new PluginConfig();
        Logic logic = new Logic();

        if (!pluginConfig.getBoolean("levels-enabled")) {
            return false;
        }

        List<String> exp = pluginConfig.getStringList("exp");

        if (!(sender instanceof Player)) {
            if (args.length == 0) {
                return false;
            }

            Player target = logic.getTarget(args[0]);
            Chat chat = new Chat(sender);
            if (!logic.verifyTarget(target)) {
                return false;
            }
            Character characterTarget = new Character(target);

            if (args.length == 1 && args[0].equalsIgnoreCase(target.getName()) && pluginConfig.getBoolean("races-classes-enabled")) {
                chat.sendMessage(pluginConfig.getString("level-level").replaceAll(":level:", characterTarget.getString("levels.level")).replaceAll(":value:", characterTarget.getString("levels.exp")), target);
                if (pluginConfig.getBoolean("races-classes-enabled") ) {
                    chat.sendMessage(pluginConfig.getString("races-classess-points").replaceAll(":points:", characterTarget.getString("levels.points")), target);
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("stats") && args.length == 2 && pluginConfig.getBoolean("races-classes-enabled")) {
                target = logic.getTarget(args[1]);
                if (!logic.verifyTarget(target)) {
                    return false;
                }
                chat.sendMessage(pluginConfig.getString("stat-header").replaceAll(":player:", target.getName()), target);
                chat.sendMessage(pluginConfig.getString("races-classes").replaceAll(":r/c:", "Race").replaceAll(":value:", characterTarget.getString("races-classes.race")), target);
                chat.sendMessage(pluginConfig.getString("races-classes").replaceAll(":r/c:", "Class").replaceAll(":value:", characterTarget.getString("races-classes.class")), target);
                for (String stat : pluginConfig.getStringList("stats")) {
                    chat.sendMessage(pluginConfig.getString("races-classes-stat").replaceAll(":stat:", stat).replaceAll(":value:", characterTarget.getString("races-classes." + stat)), target);
                }
                return true;
            }

            if (logic.integer(args[1])) {
                int amount = Integer.parseInt(args[1]);
                Chat chatTarget = new Chat(target);

                if (characterTarget.getInt("levels.exp") + amount < Integer.parseInt(exp.get(0))) {
                    characterTarget.set("levels.exp", Integer.parseInt(exp.get(0)));
                    characterTarget.set("levels.level", sortLevel(characterTarget.getInt("levels.exp"), exp) + 1);
                } else if (characterTarget.getInt("levels.exp") + amount > Integer.parseInt(exp.get(exp.size() - 1))) {
                    characterTarget.set("levels.exp", Integer.parseInt(exp.get(exp.size() - 1)));
                    characterTarget.set("levels.level", sortLevel(characterTarget.getInt("levels.exp"), exp) + 1);
                } else {
                    characterTarget.set("levels.exp", characterTarget.getInt("levels.exp") + amount);
                    characterTarget.set("levels.level", sortLevel(characterTarget.getInt("levels.exp"), exp) + 1);
                }
                int level = characterTarget.getInt("levels.level");

                if (level > characterTarget.getInt("levels.level")) {
                    characterTarget.resetPoints();
                }
                if (level < characterTarget.getInt("levels.level")) {
                    characterTarget.resetPoints();
                }
                chatTarget.sendMessage(pluginConfig.getString("level-level").replaceAll(":level:", characterTarget.getString("levels.level")).replaceAll(":value:", characterTarget.getString("levels.exp")), target);
                if (pluginConfig.getBoolean("races-classes-enabled")) {
                    chatTarget.sendMessage(pluginConfig.getString("races-classes-points").replaceAll(":points:", characterTarget.getString("levels.points")), target);
                }
                chat.sendMessage(pluginConfig.getString("level-level").replaceAll(":level:", characterTarget.getString("levels.level")).replaceAll(":value:", characterTarget.getString("levels.exp")), target);
                if (pluginConfig.getBoolean("races-classes-enabled")) {
                    chat.sendMessage(pluginConfig.getString("races-classes-points").replaceAll(":points:", characterTarget.getString("levels.points")), target);
                }
                return true;
            }

            return false;
        }

        Player player = (Player) sender;
        Chat chat = new Chat(player);
        Character character = new Character(player);

        if (args.length == 0) {
            chat.sendMessage(pluginConfig.getString("level-level").replaceAll(":level:", character.getString("levels.level")).replaceAll(":value:", character.getString("levels.exp")), player);
            if (pluginConfig.getBoolean("races-classes-enabled")) {
                chat.sendMessage(pluginConfig.getString("races-classes-points").replaceAll(":points:", character.getString("levels.points")), player);
            }
            return true;
        }

        if (pluginConfig.getBoolean("races-classes-enabled")) {
            for (String stat : pluginConfig.getStringList("stats")) {
                if (stat.equalsIgnoreCase(args[0])) {
                    if (character.getInt("levels.points") == 0) {
                        return false;
                    }
                    character.set("races-classes." + stat, character.getInt("races-classes." + stat) + 1);
                    character.set("levels.points", character.getInt("levels.points") - 1);
                    chat.sendMessage(pluginConfig.getString("races-classes-stat-level").replaceAll(":stat:", stat).replaceAll(":value:", character.getString("races-classes." + stat)), player);
                    chat.sendMessage(pluginConfig.getString("races-classes-points").replaceAll(":points:", character.getString("levels.points")), player);
                    return true;
                }
            }
        }
        if (args[0].equalsIgnoreCase("stats") && args.length == 1 && pluginConfig.getBoolean("races-classes-enabled")) {
            chat.sendMessage(pluginConfig.getString("stat-header").replaceAll(":player:", player.getName()), player);
            chat.sendMessage(pluginConfig.getString("races-classes").replaceAll(":r/c:", "Race").replaceAll(":value:", character.getString("races-classes.race")), player);
            chat.sendMessage(pluginConfig.getString("races-classes").replaceAll(":r/c:", "Class").replaceAll(":value:", character.getString("races-classes.class")), player);
            for (String stat : pluginConfig.getStringList("stats")) {
                chat.sendMessage(pluginConfig.getString("races-classes-stat").replaceAll(":stat:", stat).replaceAll(":value:", character.getString("races-classes." + stat)), player);
            }
            return true;
        }

        Player target = logic.getTarget(args[0]);
        if (!logic.verifyTarget(target)) {
            return false;
        }
        Character characterTarget = new Character(target);
        if (args.length == 1 && args[0].equalsIgnoreCase(target.getName()) && player.hasPermission("levels.level.view.other") && pluginConfig.getBoolean("races-classes-enabled")) {
            chat.sendMessage(pluginConfig.getString("level-level").replaceAll(":level:", characterTarget.getString("levels.level")).replaceAll(":value:", characterTarget.getString("levels.exp")), target);
            if (pluginConfig.getBoolean("races-classes-enabled") && player.hasPermission("levels.point.view.other")) {
                chat.sendMessage(pluginConfig.getString("races-classes-points").replaceAll(":points:", characterTarget.getString("levels.points")), target);
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("stats") && args.length == 2 && pluginConfig.getBoolean("races-classes-enabled") && player.hasPermission("levels.stat.view.other")) {
            target = logic.getTarget(args[1]);
            if (!logic.verifyTarget(target)) {
                return false;
            }
            chat.sendMessage(pluginConfig.getString("stat-header").replaceAll(":player:", target.getName()), target);
            chat.sendMessage(pluginConfig.getString("races-classes").replaceAll(":r/c:", "Race").replaceAll(":value:", characterTarget.getString("races-classes.race")), target);
            chat.sendMessage(pluginConfig.getString("races-classes").replaceAll(":r/c:", "Class").replaceAll(":value:", characterTarget.getString("races-classes.class")), target);
            for (String stat : pluginConfig.getStringList("stats")) {
                chat.sendMessage(pluginConfig.getString("races-classes-stat").replaceAll(":stat:", stat).replaceAll(":value:", characterTarget.getString("races-classes." + stat)), target);
            }
            return true;
        }

        if (player.hasPermission("levels.level.manage")) {
            if (logic.integer(args[1])) {
                int amount = Integer.parseInt(args[1]);
                Chat chatTarget = new Chat(target);
                int oldlevel = character.getInt("levels.level");

                if (characterTarget.getInt("levels.exp") + amount < Integer.parseInt(exp.get(0))) {
                    characterTarget.set("levels.exp", Integer.parseInt(exp.get(0)));
                    characterTarget.set("levels.level", sortLevel(characterTarget.getInt("levels.exp"), exp) + 1);
                } else if (characterTarget.getInt("levels.exp") + amount > Integer.parseInt(exp.get(exp.size() - 1))) {
                    characterTarget.set("levels.exp", Integer.parseInt(exp.get(exp.size() - 1)));
                    characterTarget.set("levels.level", sortLevel(characterTarget.getInt("levels.exp"), exp) + 1);
                } else {
                    characterTarget.set("levels.exp", characterTarget.getInt("levels.exp") + amount);
                    characterTarget.set("levels.level", sortLevel(characterTarget.getInt("levels.exp"), exp) + 1);
                }
                int level = characterTarget.getInt("levels.level");

                if (level > oldlevel) {
                    characterTarget.resetPoints();
                }
                if (level < oldlevel) {
                    characterTarget.resetPoints();
                }
                if (target != player) {
                    chatTarget.sendMessage(pluginConfig.getString("level-level").replaceAll(":level:", characterTarget.getString("levels.level")).replaceAll(":value:", characterTarget.getString("levels.exp")), target);
                    if (pluginConfig.getBoolean("races-classes-enabled")) {
                        chatTarget.sendMessage(pluginConfig.getString("races-classes-points").replaceAll(":points:", characterTarget.getString("levels.points")), target);
                    }
                }
                chat.sendMessage(pluginConfig.getString("level-level").replaceAll(":level:", characterTarget.getString("levels.level")).replaceAll(":value:", characterTarget.getString("levels.exp")), target);
                if (pluginConfig.getBoolean("races-classes-enabled")) {
                    chat.sendMessage(pluginConfig.getString("races-classes-points").replaceAll(":points:", characterTarget.getString("levels.points")), target);
                }
                return true;
            }
        }

        return false;
    }

    private int sortLevel(int value, List<String> exp) {
        int index = 0;
        for (int i = 0; i < exp.size(); i++) {
            if (value >= Integer.parseInt(exp.get(i))) {
                index = i;
            }
        }
        return index;
    }
}
