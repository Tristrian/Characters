package online.christopherstocks.highchrisben.characters.Commands;

import online.christopherstocks.highchrisben.characters.Libs.Character;
import online.christopherstocks.highchrisben.characters.Libs.Chat;
import online.christopherstocks.highchrisben.characters.Libs.Logic;
import online.christopherstocks.highchrisben.characters.Libs.PluginConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Karma implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        PluginConfig pluginConfig = new PluginConfig();
        Logic logic = new Logic();

        if (!pluginConfig.getBoolean("karma-enabled")) {
            new Chat(sender).sendMessage(pluginConfig.getString("module-disabled"), null);
            return true;
        }

        List<String> ranks = new ArrayList<>(), amounts = new ArrayList<>();
        for (String method : pluginConfig.getStringList("karma-levels")) {
            ranks.add(method.split(":")[0]);
            amounts.add(method.split(":")[1]);
        }

        if (!(sender instanceof Player)) {
            if (args.length == 0){
                return false;
            }

            Player target = logic.getTarget(args[0]);
            Chat chat = new Chat(sender);
            Character characterTarget = new Character(target);

            if (args.length == 1 && logic.verifyTarget(target)) {
                chat.sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", characterTarget.getString("karma.amount")).replaceAll(":rank:", characterTarget.getString("karma.rank")), target);
                return true;
            }

            if (args.length >= 2 && logic.integer(args[1])) {
                int amount = Integer.parseInt(args[1]);
                if (characterTarget.getInt("karma.amount") + amount < pluginConfig.getInt("karma-min-amount")) {
                    characterTarget.set("karma.amount", pluginConfig.getInt("karma-min-amount"));
                    new Chat(target).sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", characterTarget.getString("karma.amount")).replaceAll(":rank:", characterTarget.getString("karma.rank")), target);
                    chat.sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", characterTarget.getString("karma.amount")).replaceAll(":rank:", characterTarget.getString("karma.rank")), target);
                    return true;
                }

                if (characterTarget.getInt("karma.amount") + amount > pluginConfig.getInt("karma-max-amount")) {
                    characterTarget.set("karma.amount", pluginConfig.getInt("karma-max-amount"));
                    new Chat(target).sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", characterTarget.getString("karma.amount")).replaceAll(":rank:", characterTarget.getString("karma.rank")), target);
                    chat.sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", characterTarget.getString("karma.amount")).replaceAll(":rank:", characterTarget.getString("karma.rank")), target);
                    return true;
                }

                characterTarget.set("karma.amount", characterTarget.getInt("karma.amount") + amount);
                characterTarget.set("karma.rank", characterTarget.sortKarma(characterTarget.getInt("karma.amount")));
                new Chat(target).sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", characterTarget.getString("karma.amount")).replaceAll(":rank:", characterTarget.getString("karma.rank")), target);
                chat.sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", characterTarget.getString("karma.amount")).replaceAll(":rank:", characterTarget.getString("karma.rank")), target);
                return true;
            } else {
                for (int i = 0; i < ranks.size(); i++) {
                    if (ranks.get(i).equalsIgnoreCase(args[1])) {
                        characterTarget.set("karma", ranks.get(i));
                        characterTarget.set("karma-amount", amounts.get(i));
                        new Chat(target).sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", characterTarget.getString("karma.amount")).replaceAll(":rank:", characterTarget.getString("karma.rank")), target);
                        chat.sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", characterTarget.getString("karma.amount")).replaceAll(":rank:", characterTarget.getString("karma.rank")), target);
                        return true;
                    }
                }
                return true;
            }
        }

        Player player = (Player) sender;
        Chat chat = new Chat(player);
        Character character = new Character(player);

        if (args.length == 0) {
            chat.sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", character.getString("karma.amount")).replaceAll(":rank:", character.getString("karma.rank")), player);
            return true;
        }

        Player target = logic.getTarget(args[0]);
        Character characterTarget = new Character(target);

        if (args.length == 1 && logic.verifyTarget(target) && player.hasPermission("karma.view.other")) {
            chat.sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", characterTarget.getString("karma.amount")).replaceAll(":rank:", characterTarget.getString("karma.rank")), target);
            return true;
        }

        if (player.hasPermission("karma.karma.other")) {
            if (logic.integer(args[1])) {
                int amount = Integer.parseInt(args[1]);
                if (characterTarget.getInt("karma.amount") + amount < pluginConfig.getInt("karma-min-amount")) {
                    characterTarget.set("karma.amount", pluginConfig.getInt("karma-min-amount"));
                    if (target != player) {
                        new Chat(target).sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", characterTarget.getString("karma.amount")).replaceAll(":rank:", characterTarget.getString("karma.rank")), target);
                    }
                    chat.sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", characterTarget.getString("karma.amount")).replaceAll(":rank:", characterTarget.getString("karma.rank")), target);
                    return true;
                }

                if (characterTarget.getInt("karma.amount") + amount > pluginConfig.getInt("karma-max-amount")) {
                    characterTarget.set("karma.amount", pluginConfig.getInt("karma-max-amount"));
                    if (target != player) {
                        new Chat(target).sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", characterTarget.getString("karma.amount")).replaceAll(":rank:", characterTarget.getString("karma.rank")), target);
                    }
                    chat.sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", characterTarget.getString("karma.amount")).replaceAll(":rank:", characterTarget.getString("karma.rank")), target);
                    return true;
                }

                characterTarget.set("karma.amount", characterTarget.getInt("karma.amount") + amount);
                characterTarget.set("karma.rank", characterTarget.sortKarma(characterTarget.getInt("karma.amount")));
                if (target != player) {
                    new Chat(target).sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", characterTarget.getString("karma.amount")).replaceAll(":rank:", characterTarget.getString("karma.rank")), target);
                }
                chat.sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", characterTarget.getString("karma.amount")).replaceAll(":rank:", characterTarget.getString("karma.rank")), target);
                return true;
            } else {
                for (int i = 0; i < ranks.size(); i++) {
                    if (ranks.get(i).equalsIgnoreCase(args[1])) {
                        characterTarget.set("karma", ranks.get(i));
                        characterTarget.set("karma-amount", amounts.get(i));
                        if (target != player) {
                            new Chat(target).sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", characterTarget.getString("karma.amount")).replaceAll(":rank:", characterTarget.getString("karma.rank")), target);
                        }
                        chat.sendMessage(pluginConfig.getString("karma-karma").replaceAll(":value:", characterTarget.getString("karma.amount")).replaceAll(":rank:", characterTarget.getString("karma.rank")), target);
                        return true;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
