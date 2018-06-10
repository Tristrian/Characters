package com.tripixelstudios.highchrisben.characters.Commands;

import com.tripixelstudios.highchrisben.characters.Util.Character;
import com.tripixelstudios.highchrisben.characters.Util.PlayerChat;
import com.tripixelstudios.highchrisben.characters.Util.PluginConfig;
import com.tripixelstudios.highchrisben.characters.Util.SenderChat;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CharacterCommand implements CommandExecutor {
    private final PluginConfig pluginConfig = new PluginConfig();
    private final List<String> commands = new ArrayList<>();

    public boolean onCommand(CommandSender sender, Command command, String str, String[] args) {
        if (!(sender instanceof Player)) {
            new SenderChat(sender).headerformatmsg("not-player");
            return false;
        }
        commands.add("display");
        commands.add("help");
        commands.add("reload");
        commands.add("reset");
        commands.add("slot");

        Player player = (Player) sender;
        PlayerChat playerChat = new PlayerChat(player);

        if (!player.hasPermission(pluginConfig.getString("characters-permission"))) {
            playerChat.headerformatmsg("no-permission");
            return false;
        }

        Character character = new Character(player);

        if (args.length == 0) {
            playerChat.headerformatmsg("char-assign-command");
            if (sender.hasPermission(pluginConfig.getString("characters-staff"))) {
                playerChat.headerformatmsg("char-set-command");
                return false;
            }
            return true;
        }
        if (commands.contains(args[0].toLowerCase())) {
            handleSubCommands(player, playerChat, args);
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase(pluginConfig.getString("char-set"))) {
                if (!player.hasPermission(pluginConfig.getString("characters-staff"))) {
                    playerChat.headerformatmsg("no-permission");
                    return false;
                }
                playerChat.headerformatmsg("char-set-command");
                return false;
            }
            if (!pluginConfig.getCharFields().contains(args[0].toLowerCase())) {
                playerChat.headerformatmsg("enter-valid-field");
                return false;
            }
            playerChat.headermsg(pluginConfig.getString("enter-field-value").replace("%field%", StringUtils.capitalize(pluginConfig.getValue(pluginConfig.getCharFields(), args[0]))));
            return false;
        }
        if (args[0].equalsIgnoreCase(pluginConfig.getString("char-set"))) {
            if (!player.hasPermission(pluginConfig.getString("characters-staff"))) {
                playerChat.headerformatmsg("no-permission");
                return false;
            }
            Player target = pluginConfig.getTarget(args[1]);
            if (!target.hasPermission(pluginConfig.getString("characters-permission"))) {
                playerChat.headerformatmsg("no-character");
                return false;
            }
            if (pluginConfig.verifyTarget(sender, target)) {
                return false;
            }
            Character targetChar = new Character(target);
            if (player == target) {
                playerChat.headerformatmsg("use-char");
                return false;
            }
            if (args.length < 3) {
                playerChat.headerformatmsg("char-set-command");
                return false;
            }
            String field = args[2].toLowerCase();
            if (!pluginConfig.getCharFields().contains(field)) {
                playerChat.headerformatmsg("enter-valid-field");
                return false;
            }
            if (args.length < 4) {
                playerChat.headermsg(pluginConfig.getString("enter-field-value").replace("%field%", StringUtils.capitalize(pluginConfig.getValue(pluginConfig.getCharFields(), args[0]))));
                return false;
            }
            String input = pluginConfig.build(args, 3);

            if (!pluginConfig.getFixedFields().contains(field)) {
                targetChar.set(field, input);
                setMessages(player, target, org.apache.commons.lang.StringUtils.capitalize(field), input);
                return true;
            }
            List<String> allowedList = pluginConfig.getStringList(field);
            if (!allowedList.contains(input)) {
                playerChat.headermsg(pluginConfig.getString("field-valid").replace("%field%", org.apache.commons.lang.StringUtils.capitalize(field)).replace("%input%", pluginConfig.buildList(allowedList)));
                return false;
            }
            if (!pluginConfig.getPermFields().contains(field)) {
                targetChar.set(field, input);
                setMessages(player, target, org.apache.commons.lang.StringUtils.capitalize(field), input);
                return true;
            }
            if (!player.hasPermission(pluginConfig.getString("characters-staff-perm"))) {
                playerChat.headerformatmsg("no-permission");
                return false;
            }
            targetChar.set(field, input);
            setMessages(player, target, org.apache.commons.lang.StringUtils.capitalize(field), input);
            return true;
        }
        String field = args[0].toLowerCase();
        if (!pluginConfig.getCharFields().contains(field)) {
            playerChat.headerformatmsg("enter-valid-field");
            return false;
        }
        String input = pluginConfig.build(args, 1);
        if (!pluginConfig.getFixedFields().contains(field)) {
            character.set(field, input);
            playerChat.headermsg(pluginConfig.getString("field-set-to").replace("%field%", org.apache.commons.lang.StringUtils.capitalize(field)).replace("%input%", input));
            return true;
        }
        List<String> allowedList = pluginConfig.getStringList(field);
        if (!allowedList.contains(input)) {
            playerChat.headermsg(pluginConfig.getString("field-valid").replace("%field%", org.apache.commons.lang.StringUtils.capitalize(field)).replace("%input%", pluginConfig.buildList(allowedList)));
            return false;
        }
        if (!pluginConfig.getPermFields().contains(field)) {
            character.set(field, input);
            playerChat.headermsg(pluginConfig.getString("field-set-to").replace("%field%", org.apache.commons.lang.StringUtils.capitalize(field)).replace("%input%", input));
            return true;
        }
        if (!player.hasPermission("characters-" + input.toLowerCase())) {
            playerChat.headerformatmsg("no-permission");
            return false;
        }
        character.set(field, input);
        playerChat.headermsg(pluginConfig.getString("field-set-to").replace("%field%", org.apache.commons.lang.StringUtils.capitalize(field)).replace("%input%", input));
        return true;
    }

    private void setMessages(Player player, Player target, String field, String input) {
        new PlayerChat(player).headermsg(pluginConfig.getString("field-set-for").replace("%field%", StringUtils.capitalize(field)).replace("%input%", input).replace("%target%", target.getName()));
        new PlayerChat(target).headermsg(pluginConfig.getString("field-set-by").replace("%field%", StringUtils.capitalize(field)).replace("%input%", input).replace("%sender%", player.getName()));
    }

    private void displayDisplay(Player player, Player target, int page) {
        if ((page > pluginConfig.getInt("display-pages")) || (page < 1)) {
            new PlayerChat(player).sendMessage(pluginConfig.getString("valid-page").replace("%maxpage%", pluginConfig.getString("display-pages")));
            return;
        }
        display(player, target, pluginConfig.getStringList("display" + String.valueOf(page)), page);
    }

    private void display(Player player, Player target, List<String> list, int page) {
        PlayerChat playerChat = new PlayerChat(player);
        Character character = new Character(target);
        playerChat.sendMessage("            " + pluginConfig.getString("header"));
        String displayTarget;
        if (player != target) {
            displayTarget = pluginConfig.getString("character-of").replace("%target%", target.getName());
            playerChat.sendMessage(" " + displayTarget);
            playerChat.sendMessage(" " + pluginConfig.getString("page").replace("%page%", String.valueOf(page)).replace("%maxpage%", pluginConfig.getString("display-pages")));
        } else {
            playerChat.sendMessage(" " + pluginConfig.getString("your-character"));
            playerChat.sendMessage(" " + pluginConfig.getString("page").replace("%page%", String.valueOf(page)).replace("%maxpage%", pluginConfig.getString("display-pages")));
        }
        for (String displayfield : list) {
            playerChat.sendMessage(" " + pluginConfig.getString("field-display").replace("%field%", StringUtils.capitalize(displayfield)).replace("%input%", character.getString(displayfield)));
        }
    }

    private void handleSubCommands(Player player, PlayerChat playerChat, String[] args) {
        if (args[0].equalsIgnoreCase("display")) {
            int page = 1;
            if (args.length == 1) {
                displayDisplay(player, player, page);
                return;
            }
            Player target = pluginConfig.getTarget(args[1]);
            if (pluginConfig.verifyTarget(player, target)) {
                if (pluginConfig.integer(args[1])) {
                    page = Integer.parseInt(args[1]);
                    displayDisplay(player, player, page);
                    return;
                }
                return;
            }
            if (!(target.hasPermission(pluginConfig.getString("characters-permission")))) {
                playerChat.headerformatmsg("no-character");
                return;
            }
            if (args.length == 2) {
                displayDisplay(player, target, page);
                return;
            }
            if (!pluginConfig.integer(args[2])) {
                playerChat.headerformatmsg("not-number");
                return;
            }
            page = Integer.parseInt(args[2]);

            displayDisplay(player, target, page);
            return;
        }
        if (args[0].equalsIgnoreCase("help")) {
            int page = 1;
            if (args.length != 1) {
                String pageNum = args[1];
                if (!pluginConfig.integer(pageNum)) {
                    playerChat.headerformatmsg("not-number");
                    return;
                }
                page = Integer.parseInt(pageNum);
            }
            if ((page > pluginConfig.getInt("help-pages")) || (page < 1)) {
                playerChat.sendMessage(pluginConfig.getString("valid-page").replace("%maxpage%", pluginConfig.getString("help-pages")));
                return;
            }
            playerChat.sendMessage(("                              " + pluginConfig.getString("help-header")));
            playerChat.sendMessage((pluginConfig.getString("page").replace("%page%", String.valueOf(page)).replace("%maxpage%", pluginConfig.getString("help-pages"))));
            java.util.List<String> help = pluginConfig.getStringList("help" + String.valueOf(page));
            for (String helpString : help) {
                playerChat.sendMessage(helpString);
            }
            return;

        }
        if (args[0].equalsIgnoreCase("reload")) {
            pluginConfig.reloadConfig();
            for (Player players : Bukkit.getOnlinePlayers()) {
                new Character(players);
            }
            playerChat.headerformatmsg("config-reloaded");
            return;
        }
        if (args[0].equalsIgnoreCase("reset")) {
            if (args.length == 1) {
                Character character = new Character(player);
                character.reset();
                playerChat.headerformatmsg("reset");
                return;
            }
            if (!player.hasPermission(pluginConfig.getString("characters-staff"))) {
                playerChat.headerformatmsg("no-permission");
                return;
            }
            Player target = pluginConfig.getTarget(args[1]);
            if (target == null) {
                playerChat.headerformatmsg("not-found");
                return;
            }
            if (!(target.hasPermission(pluginConfig.getString("characters-permission")))) {
                playerChat.headerformatmsg("no-character");
                return;
            }
            Character character = new Character(target);
            character.reset();
            if (player != target) {
                PlayerChat targetChat = new PlayerChat(target);
                targetChat.headerformatmsg("reset");
                playerChat.headerformatmsg("reset-other");
            } else {
                playerChat.headerformatmsg("reset");
            }
            return;

        }
        if (args[0].equalsIgnoreCase("slot")) {
            if (args.length == 1) {
                playerChat.headerformatmsg("slot-command");
                return;
            }

            if (!pluginConfig.integer(args[1])) {
                playerChat.headerformatmsg("not-number");
                return;
            }
            int select = Integer.parseInt(args[1]);
            if (select < 1) {
                playerChat.headerformatmsg("slot-avaliable");
                return;
            }
            if (select > pluginConfig.getInt("characters-cards")) {
                playerChat.headerformatmsg("slot-avaliable");
                return;
            }
            Character character = new Character(player);
            if (select == character.getCurrentChar()) {
                playerChat.headerformatmsg("slot-same");
                return;
            }
            character.set("character-selected", select);
            return;
        }
    }

}
