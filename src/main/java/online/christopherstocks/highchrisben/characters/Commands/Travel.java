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

public class Travel implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        PluginConfig pluginConfig = new PluginConfig();
        Logic logic = new Logic();

        if (!pluginConfig.getBoolean("travel-enabled")) {
            return false;
        }

        List<String> types = new ArrayList<>(), speeds = new ArrayList<>();
        for (String method : pluginConfig.getStringList("travel-methods")) {
            types.add(method.split(":")[0]);
            speeds.add(method.split(":")[1]);
        }

        if (!(sender instanceof Player)) {
            if (args.length == 0){
                return false;
            }

            Player target = logic.getTarget(args[0]);
            Chat chat = new Chat(sender);
            Character characterTarget = new Character(target);

            if (args.length == 1 && logic.verifyTarget(target) ){
                chat.sendMessage(pluginConfig.getString("travel-travel").replaceAll(":value:", characterTarget.getString("travel.travel")), target);
                return true;
            }

            if (args.length == 2) {
                if (logic.verifyTarget(target)) {
                    for (int i = 0; i < types.size(); i++) {
                        if (types.get(i).equalsIgnoreCase(args[1])) {
                            target.setWalkSpeed(Integer.parseInt(speeds.get(i)) / 10.0F);
                            characterTarget.set("travel.travel", types.get(i));
                            new Chat(target).sendMessage(pluginConfig.getString("travel-travel").replaceAll(":value:", types.get(i)), target);
                            chat.sendMessage(pluginConfig.getString("travel-travel").replaceAll(":value:", types.get(i)), target);
                            return true;
                        }
                    }
                }
                return false;
            }
            return false;
        }

        Player player = (Player) sender;
        Chat chat = new Chat(player);
        Character character = new Character(player);

        if (args.length == 0) {
            chat.sendMessage(pluginConfig.getString("travel-travel").replaceAll(":value:", character.getString("travel.travel")), player);
            return true;
        }

        if (args.length == 1) {
            for (int i = 0; i < types.size(); i++) {
                if (types.get(i).equalsIgnoreCase(args[0])) {
                    player.setWalkSpeed(Integer.parseInt(speeds.get(i)) / 10.0F);
                    character.set("travel.travel", types.get(i));
                    chat.sendMessage(pluginConfig.getString("travel-travel").replaceAll(":value:", types.get(i)), player);
                    return true;
                }
            }
        }

        Player target = logic.getTarget(args[0]);
        Character characterTarget = new Character(target);

        if (args.length == 1 && logic.verifyTarget(target) && player.hasPermission("travel.view.other")){
            chat.sendMessage(pluginConfig.getString("travel-travel").replaceAll(":value:", characterTarget.getString("travel.travel")), target);
            return true;
        }

        if (player.hasPermission("travel.travel.other")) {
            if (logic.verifyTarget(target)) {
                for (int i = 0; i < types.size(); i++) {
                    if (types.get(i).equalsIgnoreCase(args[1])) {
                        target.setWalkSpeed(Integer.parseInt(speeds.get(i)) / 10.0F);
                        characterTarget.set("travel.travel", types.get(i));
                        if (target != player) {
                            new Chat(target).sendMessage(pluginConfig.getString("travel-travel").replaceAll(":value:", types.get(i)), target);
                        }
                        chat.sendMessage(pluginConfig.getString("travel-travel").replaceAll(":value:", types.get(i)), target);
                        return true;
                    }
                }
            }
            return false;
        }

        return false;
    }
}
