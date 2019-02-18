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

public class Characters implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        Chat chat = new Chat(sender);
        PluginConfig pluginConfig = new PluginConfig();
        Logic logic = new Logic();

        if (args.length == 0) {
            return false;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            chat = new Chat(player);

            Character character = new Character(player);

            String base = args[0];

            if (base.equalsIgnoreCase("help")) {
                int page = 1;
                int items = pluginConfig.getInt("help-items");
                List<String> pageItems = pluginConfig.getStringList("help");
                if (args.length > 1 && logic.integer(args[1])) {
                    page = Integer.parseInt(args[1]);
                }
                double pages = Math.ceil((double) pageItems.size() / items);
                if (page > pages || page <= 0) {
                    return false;
                }
                for (int i = ((page - 1) * items); i < (page * items); i++) {
                    if (i >= pageItems.size()){
                        break;
                    }
                    if (pageItems.get(i).equalsIgnoreCase("empty")) {
                        continue;
                    }
                    if (pageItems.get(i).equalsIgnoreCase("spacer")) {
                        chat.sendMessage("", player);
                        continue;
                    }
                    chat.sendMessage(pageItems.get(i).replaceAll(":page:", String.valueOf(page)).replaceAll(":pages:", String.valueOf((int) pages)), player);
                }
                return true;
            }

            if (base.equalsIgnoreCase("select") && pluginConfig.getBoolean("races-classes-enabled")) {
                if (args.length == 1){
                    return false;
                }
                for (String race : pluginConfig.getStringList("races")) {
                    if (race.equalsIgnoreCase(args[1])) {
                        character.set("races-classes.race", race);
                        chat.sendMessage(pluginConfig.getString("races-classes-set").replaceAll(":type:", "Race").replaceAll(":value:", race), player);
                        return true;
                    }
                }
                for (String cclass : pluginConfig.getStringList("classes")) {
                    if (cclass.equalsIgnoreCase(args[1])) {
                        character.set("races-classes.class", cclass);
                        chat.sendMessage(pluginConfig.getString("races-classes-set").replaceAll(":type:", "Class").replaceAll(":value:", cclass), player);
                        return true;
                    }
                }
                return false;
            }

            if (base.equalsIgnoreCase("show")) {
                int page = 1;
                int items = pluginConfig.getInt("display-items");
                List<String> pageItems = pluginConfig.getStringList("display");
                double pages = Math.ceil((double) pageItems.size() / items);
                if (page > pages) {
                    return false;
                }
                if (args.length == 1) {
                    for (int i = ((page - 1) * items); i < (page * items); i++) {
                        if (i >= pageItems.size()){
                            break;
                        }
                        if (i == ((page - 1) * items)) {
                            chat.sendMessage(pluginConfig.getString("display-header").replaceAll(":page:", String.valueOf(page)).replaceAll(":pages:", String.valueOf((int) pages)).replace(":player:", player.getName()), player);
                        }
                        if (pageItems.get(i).equalsIgnoreCase("empty")) {
                            continue;
                        }
                        if (pageItems.get(i).equalsIgnoreCase("spacer")) {
                            chat.sendMessage("", player);
                            continue;
                        }
                        chat.sendMessage(pluginConfig.getString("character-display").replaceAll(":field:", pageItems.get(i)).replaceAll(":value:", character.getString(pageItems.get(i))), player);
                    }
                    return true;
                }
                if (args.length == 2) {
                    if (logic.integer(args[1])) {
                        page = Integer.parseInt(args[1]);
                        if (page > pages || page <= 0) {
                            return false;
                        }
                        for (int i = ((page - 1) * items); i < (page * items); i++) {
                            if (i >= pageItems.size()){
                                break;
                            }
                            if (i == ((page - 1) * items)) {
                                chat.sendMessage(pluginConfig.getString("display-header").replaceAll(":page:", String.valueOf(page)).replaceAll(":pages:", String.valueOf((int) pages)).replace(":player:", player.getName()), player);
                            }
                            if (pageItems.get(i).equalsIgnoreCase("empty")) {
                                continue;
                            }
                            if (pageItems.get(i).equalsIgnoreCase("spacer")) {
                                chat.sendMessage("", player);
                                continue;
                            }
                            chat.sendMessage(pluginConfig.getString("character-display").replaceAll(":field:", pageItems.get(i)).replaceAll(":value:", character.getString(pageItems.get(i))), player);
                        }
                        return true;
                    }
                    if (player.hasPermission("characters.show.other")) {
                        Player target = logic.getTarget(args[1]);
                        if (logic.verifyTarget(target)) {
                            Character characterTarget = new Character(target);
                            for (int i = ((page - 1) * items); i < (page * items); i++) {
                                if (i >= pageItems.size()){
                                    break;
                                }
                                if (i == ((page - 1) * items)) {
                                    chat.sendMessage(pluginConfig.getString("display-header").replaceAll(":page:", String.valueOf(page)).replaceAll(":pages:", String.valueOf((int) pages)).replace(":player:", target.getName()), target);
                                }
                                if (pageItems.get(i).equalsIgnoreCase("empty")) {
                                    continue;
                                }
                                if (pageItems.get(i).equalsIgnoreCase("spacer")) {
                                    chat.sendMessage("", target);
                                    continue;
                                }
                                chat.sendMessage(pluginConfig.getString("character-display").replaceAll(":field:", pageItems.get(i)).replaceAll(":value:", characterTarget.getString(pageItems.get(i))), target);
                            }
                            return true;
                        }
                        return false;
                    }
                    return false;
                }
                if (player.hasPermission("characters.show.other")) {
                    Player target = logic.getTarget(args[1]);
                    if (logic.verifyTarget(target)) {
                        if (logic.integer(args[2])) {
                            page = Integer.parseInt(args[2]);
                            if (page > pages || page <= 0) {
                                return false;
                            }
                            Character characterTarget = new Character(target);
                            for (int i = ((page - 1) * items); i < (page * items); i++) {
                                if (i >= pageItems.size()){
                                    break;
                                }
                                if (i == ((page - 1) * items)) {
                                    chat.sendMessage(pluginConfig.getString("display-header").replaceAll(":page:", String.valueOf(page)).replaceAll(":pages:", String.valueOf((int) pages)).replace(":player:", target.getName()), target);
                                }
                                if (pageItems.get(i).equalsIgnoreCase("empty")) {
                                    continue;
                                }
                                if (pageItems.get(i).equalsIgnoreCase("spacer")) {
                                    chat.sendMessage("", target);
                                    continue;
                                }
                                chat.sendMessage(pluginConfig.getString("character-display").replaceAll(":field:", pageItems.get(i)).replaceAll(":value:", characterTarget.getString(pageItems.get(i))), target);
                            }
                            return true;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }

            if (base.equalsIgnoreCase("reload")) {
                if (player.hasPermission("characters.reload")) {
                    pluginConfig.reload();
                    chat.sendMessage(pluginConfig.getString("config-reloaded"), player);
                    return true;
                } else {
                    return false;
                }
            }

            if (base.equalsIgnoreCase("reset")) {
                if (args.length == 1) {
                    character.reset();
                    chat.sendMessage(pluginConfig.getString("character-reset"), player);
                    return true;
                }
                if (args.length == 2) {
                    if (logic.integer(args[1])) {
                        int card = Integer.parseInt(args[1]);
                        if (card <= 0 || card > pluginConfig.getInt("characters-max")) {
                            return false;
                        }
                        character.reset(card);
                        chat.sendMessage(pluginConfig.getString("character-reset"), player);
                        return true;
                    }
                    if (player.hasPermission("characters.reset.other")) {
                        Player target = logic.getTarget(args[1]);
                        if (logic.verifyTarget(target)) {
                            new Character(target).reset();
                            if (target != player) {
                                new Chat(target).sendMessage(pluginConfig.getString("character-reset"), target);
                            }
                            chat.sendMessage(pluginConfig.getString("character-reset"), player);
                            return true;
                        }
                        return false;
                    }
                    return false;
                }
                if (player.hasPermission("characters.reset.other")) {
                    Player target = logic.getTarget(args[1]);
                    if (logic.verifyTarget(target)) {
                        int card = Integer.parseInt(args[2]);
                        if (card <= 0 || card > pluginConfig.getInt("characters-max")) {
                            return false;
                        }
                        new Character(target).reset(card);
                        if (target != player) {
                            new Chat(target).sendMessage(pluginConfig.getString("character-reset"), target);
                        }
                        chat.sendMessage(pluginConfig.getString("character-reset"), player);
                        return true;
                    }
                    return false;
                }
                return false;
            }

            if (base.equalsIgnoreCase("slot")) {
                if (args.length == 1) {
                    chat.sendMessage(pluginConfig.getString("character-slot").replaceAll(":slot:", String.valueOf(character.getSlot())), player);
                    return true;
                }
                if (logic.integer(args[1])) {
                    int card = Integer.parseInt(args[1]);
                    if (card <= 0 || card > pluginConfig.getInt("characters-max")) {
                        return false;
                    }
                    character.setSlot(card);
                    chat.sendMessage(pluginConfig.getString("character-slot").replaceAll(":slot:", String.valueOf(character.getSlot())), player);
                    return true;
                }
                return false;
            }

            List<String> commands = pluginConfig.getStringList("fields");

            if (args.length == 1) {
                return false;
            }

            if (!args[1].equalsIgnoreCase("set")) {
                for (String command : commands) {
                    if (command.equalsIgnoreCase(base)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            if (i + 1 == args.length) {
                                stringBuilder.append(args[i]);
                                continue;
                            }
                            stringBuilder.append(args[i]).append(" ");
                        }
                        String value = stringBuilder.toString();

                        if ((value.contains("&") && (!player.hasPermission("characters.colour"))) || (value.contains("&") && value.length() <= 2)) {
                            return false;
                        }

                        for (String field : pluginConfig.getStringList("perm-fields")) {
                            if (field.equalsIgnoreCase(command) && (!player.hasPermission("characters.permission.*") || !player.hasPermission("characters.permission." + field))) {
                                return false;
                            }
                        }

                        List<String> fields = pluginConfig.getStringList("fixed-fields");
                        if (fields.contains(command)) {
                            for (String field : fields) {
                                if (field.equalsIgnoreCase(command)) {
                                    for (String accepted : pluginConfig.getStringList(field)) {
                                        if (accepted.equalsIgnoreCase(value)) {
                                            character.set(command, accepted);
                                            chat.sendMessage(pluginConfig.getString("character-set").replaceAll(":field:", command).replaceAll(":value:", accepted), player);
                                            return true;
                                        }
                                    }
                                }
                            }
                            return false;
                        }

                        character.set(command, value);
                        chat.sendMessage(pluginConfig.getString("character-set").replaceAll(":field:", command).replaceAll(":value:", value), player);
                        return true;
                    }
                }
            }

            if (args.length <= 3) {
                return false;
            }

            if (player.hasPermission("characters.set.other") && args[1].equalsIgnoreCase("set")) {
                for (String command : commands) {
                    if (command.equalsIgnoreCase(base)) {
                        Player target = logic.getTarget(args[2]);
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 3; i < args.length; i++) {
                            if (i + 1 == args.length) {
                                stringBuilder.append(args[i]);
                                continue;
                            }
                            stringBuilder.append(args[i]).append(" ");
                        }
                        String value = stringBuilder.toString();

                        if ((value.contains("&") && (!player.hasPermission("characters.colour.other"))) || (value.contains("&") && value.length() <= 2)) {
                            return false;
                        }

                        for (String field : pluginConfig.getStringList("perm-fields")) {
                            if (field.equalsIgnoreCase(command) && (!player.hasPermission("characters.permission.*") || !player.hasPermission("characters.permission." + field + ".other"))) {
                                return false;
                            }
                        }

                        List<String> fields = pluginConfig.getStringList("fixed-fields");
                        if (fields.contains(command)) {
                            for (String field : fields) {
                                if (field.equalsIgnoreCase(command)) {
                                    for (String accepted : pluginConfig.getStringList(field)) {
                                        if (accepted.equalsIgnoreCase(value)) {
                                            new Character(target).set(command, accepted);
                                            if (target != player) {
                                                new Chat(target).sendMessage(pluginConfig.getString("character-set").replaceAll(":field:", command).replaceAll(":value:", accepted), target);
                                            }
                                            chat.sendMessage(pluginConfig.getString("character-set").replaceAll(":field:", command).replaceAll(":value:", accepted), target);
                                            return true;
                                        }
                                    }
                                }
                            }
                            return false;
                        }

                        new Character(target).set(command, value);
                        if (target != player) {
                            new Chat(target).sendMessage(pluginConfig.getString("character-set").replaceAll(":field:", command).replaceAll(":value:", value), target);
                        }
                        chat.sendMessage(pluginConfig.getString("character-set").replaceAll(":field:", command).replaceAll(":value:", value), target);
                        return true;
                    }
                }
                return false;
            }

        } else {
            String base = args[0];

            if (base.equalsIgnoreCase("help")) {
                int page = 1;
                int items = pluginConfig.getInt("help-items");
                List<String> pageItems = pluginConfig.getStringList("help");
                if (args.length > 1 && logic.integer(args[1])) {
                    page = Integer.parseInt(args[1]);
                }
                double pages = Math.ceil((double) pageItems.size() / items);
                if (page > pages || page <= 0) {
                    return false;
                }
                for (int i = ((page - 1) * items); i < (page * items); i++) {
                    if (i >= pageItems.size()){
                        break;
                    }
                    if (pageItems.get(i).equalsIgnoreCase("empty")) {
                        continue;
                    }
                    if (pageItems.get(i).equalsIgnoreCase("spacer")) {
                        chat.sendMessage("", null);
                        continue;
                    }
                    chat.sendMessage(pageItems.get(i).replaceAll(":page:", String.valueOf(page)).replaceAll(":pages:", String.valueOf((int) pages)), null);
                }
                return true;
            }

            if (base.equalsIgnoreCase("reload")) {
                pluginConfig.reload();
                chat.sendMessage(pluginConfig.getString("config-reloaded"), null);
                return true;
            }

            if (base.equalsIgnoreCase("reset")) {
                if (args.length == 2) {
                    Player target = logic.getTarget(args[1]);
                    if (logic.verifyTarget(target)) {
                        new Character(target).reset();
                        new Chat(target).sendMessage(pluginConfig.getString("character-reset"), target);
                        chat.sendMessage(pluginConfig.getString("character-reset"), null);
                        return true;
                    }
                    return false;
                }
                Player target = logic.getTarget(args[1]);
                if (logic.verifyTarget(target)) {
                    int card = Integer.parseInt(args[2]);
                    if (card <= 0 || card > pluginConfig.getInt("characters-max")) {
                        return false;
                    }
                    new Character(target).reset(card);
                    new Chat(target).sendMessage(pluginConfig.getString("character-reset"), target);
                    chat.sendMessage(pluginConfig.getString("character-reset"), null);
                    return true;
                }
                return false;
            }

            if (base.equalsIgnoreCase("show")) {
                int page = 1;
                int items = pluginConfig.getInt("display-items");
                List<String> pageItems = pluginConfig.getStringList("display");
                double pages = Math.ceil((double) pageItems.size() / items);
                if (page > pages) {
                    return false;
                }
                if (args.length == 1) {
                    return false;
                }
                if (args.length == 2) {
                    Player target = logic.getTarget(args[1]);
                    if (logic.verifyTarget(target)) {
                        Character characterTarget = new Character(target);
                        for (int i = ((page - 1) * items); i < (page * items); i++) {
                            if (i >= pageItems.size()){
                                break;
                            }
                            if (i == ((page - 1) * items)) {
                                chat.sendMessage(pluginConfig.getString("display-header").replaceAll(":page:", String.valueOf(page)).replaceAll(":pages:", String.valueOf((int) pages)).replace(":player:", target.getName()), target);
                            }
                            if (pageItems.get(i).equalsIgnoreCase("empty")) {
                                continue;
                            }
                            if (pageItems.get(i).equalsIgnoreCase("spacer")) {
                                chat.sendMessage("", target);
                                continue;
                            }
                            chat.sendMessage(pluginConfig.getString("character-display").replaceAll(":field:", pageItems.get(i)).replaceAll(":value:", characterTarget.getString(pageItems.get(i))), target);
                        }
                        return true;
                    }
                }
                Player target = logic.getTarget(args[1]);
                if (logic.verifyTarget(target)) {
                    if (logic.integer(args[2])) {
                        page = Integer.parseInt(args[2]);
                        if (page > pages || page <= 0) {
                            return false;
                        }
                        Character characterTarget = new Character(target);
                        for (int i = ((page - 1) * items); i < (page * items); i++) {
                            if (i >= pageItems.size()){
                                break;
                            }
                            if (i == ((page - 1) * items)) {
                                chat.sendMessage(pluginConfig.getString("display-header").replaceAll(":page:", String.valueOf(page)).replaceAll(":pages:", String.valueOf((int) pages)).replace(":player:", target.getName()), target);
                            }
                            if (pageItems.get(i).equalsIgnoreCase("empty")) {
                                continue;
                            }
                            if (pageItems.get(i).equalsIgnoreCase("spacer")) {
                                chat.sendMessage("", target);
                                continue;
                            }
                            chat.sendMessage(pluginConfig.getString("character-display").replaceAll(":field:", pageItems.get(i)).replaceAll(":value:", characterTarget.getString(pageItems.get(i))), target);
                        }
                        return true;
                    }
                    return false;
                }
                return false;
            }
            if (args.length > 3) {
                for (String command : pluginConfig.getStringList("fields")) {
                    if (command.equalsIgnoreCase(base)) {
                        Player target = logic.getTarget(args[2]);
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 3; i < args.length; i++) {
                            if (i + 1 == args.length) {
                                stringBuilder.append(args[i]);
                                continue;
                            }
                            stringBuilder.append(args[i]).append(" ");
                        }
                        String value = stringBuilder.toString();

                        List<String> fields = pluginConfig.getStringList("fixed-fields");
                        if (fields.contains(command)) {
                            for (String field : fields) {
                                if (field.equalsIgnoreCase(command)) {
                                    for (String accepted : pluginConfig.getStringList(field)) {
                                        if (accepted.equalsIgnoreCase(value)) {
                                            new Character(target).set(command, accepted);
                                            new Chat(target).sendMessage(pluginConfig.getString("character-set").replaceAll(":field:", command).replaceAll(":value:", accepted), target);
                                            chat.sendMessage(pluginConfig.getString("character-set").replaceAll(":field:", command).replaceAll(":value:", accepted), target);
                                            return true;
                                        }
                                    }
                                }
                            }
                            return false;
                        }

                        new Character(target).set(command, value);
                        new Chat(target).sendMessage(pluginConfig.getString("character-set").replaceAll(":field:", command).replaceAll(":value:", value), target);
                        chat.sendMessage(pluginConfig.getString("character-set").replaceAll(":field:", command).replaceAll(":value:", value), target);
                        return true;
                    }
                }
                return false;
            }

            return false;
        }
        return false;
    }
}
