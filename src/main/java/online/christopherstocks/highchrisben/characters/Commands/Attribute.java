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

public class Attribute implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        PluginConfig pluginConfig = new PluginConfig();
        Logic logic = new Logic();

        if (!pluginConfig.getBoolean("attribute-enabled")) {
            new Chat(sender).sendMessage(pluginConfig.getString("module-disabled"), null);
            return true;
        }

        if (!(sender instanceof Player)) {
            if (args.length == 0) {
                return false;
            }

            Chat chat = new Chat(sender);

            if (args.length == 1) {
                Player target = logic.getTarget(args[0]);
                Character characterTarget = new Character(target);
                if (!logic.verifyTarget(target)) {
                    return false;
                }
                chat.sendMessage(pluginConfig.getString("attribute-header").replaceAll(":player:", target.getName()), target);
                for (String attribute : pluginConfig.getStringList("attributes")) {
                    chat.sendMessage(pluginConfig.getString("attribute-attribute").replaceAll(":attr:", attribute).replaceAll(":value:", characterTarget.getString(attribute)), target);
                }
                return true;
            }

            String base = args[0];
            List<String> attributes = pluginConfig.getStringList("attributes");

            if (args.length == 4 && args[1].equalsIgnoreCase("alter")) {
                for (String attr : attributes) {
                    if (attr.equalsIgnoreCase(base)) {
                        Player target = logic.getTarget(args[2]);
                        if (logic.verifyTarget(target) && logic.integer(args[3])) {
                            int amount = Integer.parseInt(args[3]);
                            Character characterTarget = new Character(target);

                            if (characterTarget.getInt("attribute." + attr) + amount < pluginConfig.getInt("attribute-min-amount")) {
                                characterTarget.set("attribute." + attr, pluginConfig.getInt("attribute-min-amount"));
                                new Chat(target).sendMessage(pluginConfig.getString("attribute-attribute").replaceAll(":value:", characterTarget.getString("attribute." + attr)).replaceAll(":attr:", attr), target);
                                chat.sendMessage(pluginConfig.getString("attribute-attribute").replaceAll(":value:", characterTarget.getString("attribute." + attr)).replaceAll(":attr:", attr), target);
                                return true;
                            }

                            if (characterTarget.getInt("attribute." + attr) + amount > pluginConfig.getInt("attribute-max-amount")) {
                                characterTarget.set("attribute." + attr, pluginConfig.getInt("attribute-max-amount"));
                                new Chat(target).sendMessage(pluginConfig.getString("attribute-attribute").replaceAll(":value:", characterTarget.getString("attribute." + attr)).replaceAll(":attr:", attr), target);
                                chat.sendMessage(pluginConfig.getString("attribute-attribute").replaceAll(":value:", characterTarget.getString("attribute." + attr)).replaceAll(":attr:", attr), target);
                                return true;
                            }

                            characterTarget.set("attribute." + attr, characterTarget.getInt("attribute." + attr) + amount);
                            new Chat(target).sendMessage(pluginConfig.getString("attribute-attribute").replaceAll(":value:", characterTarget.getString("attribute." + attr)).replaceAll(":attr:", attr), target);
                            chat.sendMessage(pluginConfig.getString("attribute-attribute").replaceAll(":value:", characterTarget.getString("attribute." + attr)).replaceAll(":attr:", attr), target);
                            return true;
                        }
                        return false;
                    }
                }
            }

            return false;
        }

        Player player = (Player) sender;
        Chat chat = new Chat(player);
        Character character = new Character(player);

        if (args.length == 0) {
            chat.sendMessage(pluginConfig.getString("attribute-header").replaceAll(":player:", player.getName()), player);
            for (String attribute : pluginConfig.getStringList("attributes")) {
                chat.sendMessage(pluginConfig.getString("attribute-attribute").replaceAll(":attr:", attribute).replaceAll(":value:", character.getString("attribute." + attribute)), player);
            }
            return true;
        }

        if (args.length == 1 && player.hasPermission("attribute.view.other")) {
            Player target = logic.getTarget(args[0]);
            if (!logic.verifyTarget(target)) {
                return false;
            }
            Character characterTarget = new Character(target);
            chat.sendMessage(pluginConfig.getString("attribute-header").replaceAll(":player:", target.getName()), target);
            for (String attribute : pluginConfig.getStringList("attributes")) {
                chat.sendMessage(pluginConfig.getString("attribute-attribute").replaceAll(":attr:", attribute).replaceAll(":value:", characterTarget.getString("attribute." + attribute)), target);
            }
            return true;
        }

        String base = args[0];
        List<String> attributes = pluginConfig.getStringList("attributes");

        // /atribute Stamina -10
        if (args.length == 2) {
            for (String attr : attributes) {
                if (attr.equalsIgnoreCase(base)) {
                    if (logic.integer(args[1])) {
                        int amount = Integer.parseInt(args[3]);

                        if (character.getInt(attr) + amount < pluginConfig.getInt("attribute-min-amount")) {
                            character.set("attribute." + attr, pluginConfig.getInt("attribute-min-amount"));
                            chat.sendMessage(pluginConfig.getString("attribute-attribute").replaceAll(":value:", character.getString(attr)).replaceAll(":attr:", attr), player);
                            return true;
                        }

                        if (character.getInt(attr) + amount > pluginConfig.getInt("attribute-max-amount")) {
                            character.set("attribute." + attr, pluginConfig.getInt("attribute-max-amount"));
                            chat.sendMessage(pluginConfig.getString("attribute-attribute").replaceAll(":value:", character.getString(attr)).replaceAll(":attr:", attr), player);
                            return true;
                        }

                        character.set("attribute." + attr, character.getInt(attr) + amount);
                        chat.sendMessage(pluginConfig.getString("attribute-attribute").replaceAll(":value:", character.getString(attr)).replaceAll(":attr:", attr), player);
                        return true;
                    }
                    return false;
                }
            }
        }

        if (args.length == 4 && player.hasPermission("attribute.attribute.other") && args[1].equalsIgnoreCase("alter")) {
            for (String attr : attributes) {
                if (attr.equalsIgnoreCase(base)) {
                    Player target = logic.getTarget(args[2]);
                    if (logic.verifyTarget(target) && logic.integer(args[3])) {
                        Character characterTarget = new Character(target);
                        int amount = Integer.parseInt(args[3]);

                        if (characterTarget.getInt(attr) + amount < pluginConfig.getInt("attribute-min-amount")) {
                            characterTarget.set("attribute." + attr, pluginConfig.getInt("attribute-min-amount"));
                            if (target != player) {
                                new Chat(target).sendMessage(pluginConfig.getString("attribute-attribute").replaceAll(":value:", characterTarget.getString(attr)).replaceAll(":attr:", attr), target);
                            }
                            chat.sendMessage(pluginConfig.getString("attribute-attribute").replaceAll(":value:", characterTarget.getString(attr)).replaceAll(":attr:", attr), target);
                            return true;
                        }

                        if (characterTarget.getInt("attribute." + attr) + amount > pluginConfig.getInt("attribute-max-amount")) {
                            characterTarget.set("attribute." + attr, pluginConfig.getInt("attribute-max-amount"));
                            if (target != player) {
                                new Chat(target).sendMessage(pluginConfig.getString("attribute-attribute").replaceAll(":value:", characterTarget.getString(attr)).replaceAll(":attr:", attr), target);
                            }
                            chat.sendMessage(pluginConfig.getString("attribute-attribute").replaceAll(":value:", characterTarget.getString(attr)).replaceAll(":attr:", attr), target);
                            return true;
                        }

                        characterTarget.set("attribute." + attr, characterTarget.getInt(attr) + amount);
                        if (target != player) {
                            new Chat(target).sendMessage(pluginConfig.getString("attribute-attribute").replaceAll(":value:", characterTarget.getString(attr)).replaceAll(":attr:", attr), target);
                        }
                        chat.sendMessage(pluginConfig.getString("attribute-attribute").replaceAll(":value:", characterTarget.getString(attr)).replaceAll(":attr:", attr), target);
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
    }
}
