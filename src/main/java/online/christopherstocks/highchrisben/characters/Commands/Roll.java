package online.christopherstocks.highchrisben.characters.Commands;

import online.christopherstocks.highchrisben.characters.Libs.Character;
import online.christopherstocks.highchrisben.characters.Libs.Chat;
import online.christopherstocks.highchrisben.characters.Libs.Logic;
import online.christopherstocks.highchrisben.characters.Libs.PluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.List;

public class Roll implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        PluginConfig pluginConfig = new PluginConfig();
        Logic logic = new Logic();

        if (!pluginConfig.getBoolean("roll-enabled")) {
            new Chat(sender).sendMessage(pluginConfig.getString("module-disabled"), null);
            return true;
        }

        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        Character character = new Character(player);

        boolean statEnabled = pluginConfig.getBoolean("races-classes-enabled");

        if (args.length < 1) {
            int die = pluginConfig.getInt("die-default");
            int side = pluginConfig.getInt("side-default");
            roll(die, side, 0, null, player);
            return true;
        }

        if (args.length == 1) {
            if (logic.integer(args[0])) {
                int die = pluginConfig.getInt("die-default");
                int side = pluginConfig.getInt("side-default");
                int mod = parseInt(args[0], pluginConfig.getInt("mod-max"));
                if (mod > pluginConfig.getInt("mod-max")) {
                    mod = pluginConfig.getInt("mod-max");
                }
                roll(die, side, mod, null, player);
                return true;
            }

            if ((getStat(args[0]) != null) && (statEnabled)) {
                int die = pluginConfig.getInt("die-default");
                int side = pluginConfig.getInt("side-default");
                String stat = getStat(args[0]);
                int mod = getStatTotal(stat, pluginConfig.getInt(pluginConfig.getString("races-classes.race") + "." + stat), pluginConfig.getInt(pluginConfig.getString("races-classes.class") + "." + stat), character);
                roll(die, side, mod, stat, player);
                return true;
            }

            if (args[0].contains("d")) {
                String[] dice = args[0].split("d");
                if ((logic.integer(dice[0])) && (logic.integer(dice[1]))) {
                    int die = parseInt(dice[0], pluginConfig.getInt("die-max"));
                    int side = parseInt(dice[1], pluginConfig.getInt("side-max"));
                    if ((die <= 0) || (side <= 0)) {
                        return false;
                    }
                    roll(die, side, 0, null, player);
                    return true;
                }
                return false;
            }
            return false;
        }


        if (args.length == 2) {
            if (args[0].contains("d")) {
                String[] dice = args[0].split("d");
                int die = parseInt(dice[0], pluginConfig.getInt("die-max"));
                int side = parseInt(dice[1], pluginConfig.getInt("side-max"));
                if ((die <= 0) || (side <= 0)) {
                    return false;
                }

                if (logic.integer(args[1])) {
                    int mod = parseInt(args[1], pluginConfig.getInt("mod-max"));
                    if (mod > pluginConfig.getInt("mod-max")) {
                        mod = pluginConfig.getInt("mod-max");
                    }
                    roll(die, side, mod, null, player);
                    return true;
                }

                if ((getStat(args[1]) != null) && (statEnabled)) {
                    String stat = getStat(args[1]);
                    int mod = getStatTotal(stat, pluginConfig.getInt(pluginConfig.getString("races-classes.race") + "." + stat), pluginConfig.getInt(pluginConfig.getString("races-classes.class") + "." + stat), character);
                    roll(die, side, mod, stat, player);
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }


    private void roll(int die, int side, int mod, String stat, Player player) {
        if (new PluginConfig().getBoolean("rolling-advanced")) {
            advRoll(die, side, mod, stat, player);
        } else {
            simpleRoll(die, side, mod, stat, player);
        }
    }

    private int getStatTotal(String stat, int race, int cclass, Character character) {
        return race + cclass + character.getInt("races-classes." + stat);
    }

    private String getStat(String target) {
        for (String stat : new PluginConfig().getStringList("stats")) {
            if (stat.equalsIgnoreCase(target)) {
                return stat;
            }
        }
        return null;
    }

    private void advRoll(int die, int side, int mod, String stat, Player player) {
        PluginConfig pluginConfig = new PluginConfig();
        String modString = mod >= 0 ? "+" + mod : String.valueOf(mod);
        double range = pluginConfig.getDouble("rolling-range");
        SecureRandom random = new SecureRandom();
        int total = 0;
        Character character = new Character(player);
        if (stat != null) {
            if (range <= 0.0D) {
                shout(pluginConfig.getString("advanced-rolling").replaceAll(":displayname:", character.getString(pluginConfig.getString("name-field"))).replaceAll(":player:", player.getName()).replaceAll(":dice:", die + "d" + side + modString).replaceAll(":stat:", stat));
            }else{
                speak(player, range, pluginConfig.getString("advanced-rolling").replaceAll(":displayname:", character.getString(pluginConfig.getString("name-field"))).replaceAll(":player:", player.getName()).replaceAll(":dice:", die + "d" + side + modString).replaceAll(":stat:", stat));
            }
        } else {
            if (range <= 0.0D) {
                shout(pluginConfig.getString("advanced-rolling").replaceAll(":displayname:", character.getString(pluginConfig.getString("name-field"))).replaceAll(":player:", player.getName()).replaceAll(":dice:", die + "d" + side + modString));
            }else{
                speak(player, range, pluginConfig.getString("advanced-rolling").replaceAll(":displayname:", character.getString(pluginConfig.getString("name-field"))).replaceAll(":player:", player.getName()).replaceAll(":dice:", die + "d" + side + modString));
            }
        }
        for (int i = 0; i < die; i++) {
            int value = random.nextInt(side) + 1;
            total += value;
            if (range <= 0.0D) {
                shout(pluginConfig.getString("roll-result").replaceAll(":result:", String.valueOf(value)));
            } else {
                speak(player, range, pluginConfig.getString("roll-result").replaceAll(":result:", String.valueOf(value)));
            }
        }
        if (range <= 0.0D) {
            shout(pluginConfig.getString("roll-total").replaceAll(":total:", String.valueOf(total + mod)));
        } else {
            speak(player, range, pluginConfig.getString("roll-total").replaceAll(":total:", String.valueOf(total + mod)));
        }
    }

    private void simpleRoll(int die, int side, int mod, String stat, Player player) {
        PluginConfig pluginConfig = new PluginConfig();
        String modString = mod >= 0 ? "+" + mod : String.valueOf(mod);
        double range = pluginConfig.getDouble("rolling-range");
        SecureRandom random = new SecureRandom();
        int total = 0;
        Character character = new Character(player);
        for (int i = 0; i < die; i++) {
            int value = random.nextInt(side) + 1;
            total += value;
        }
        if (stat != null) {
            if (range <= 0.0D) {
                shout(pluginConfig.getString("simple-stat-rolling").replaceAll(":displayname:", character.getString(pluginConfig.getString("name-field").replaceAll(":player:", player.getName()).replaceAll(":result:", String.valueOf(total + mod)).replaceAll(":dice:", die + "d" + side + modString).replaceAll(":stat:", stat))));
            } else {
                speak(player, range, pluginConfig.getString("simple-stat-rolling").replaceAll(":displayname:", character.getString(pluginConfig.getString("name-field"))).replaceAll(":player:", player.getName()).replaceAll(":result:", String.valueOf(total + mod)).replaceAll(":dice:", die + "d" + side + modString).replaceAll(":stat:", stat));
            }
        } else {
            if (range <= 0.0D) {
                shout(pluginConfig.getString("simple-rolling").replaceAll(":displayname:", character.getString(pluginConfig.getString("name-field").replaceAll(":player:", player.getName()).replaceAll(":result:", String.valueOf(total + mod)).replaceAll(":dice:", die + "d" + side + modString))));
            } else {
                speak(player, range, pluginConfig.getString("simple-rolling").replaceAll(":displayname:", character.getString(pluginConfig.getString("name-field"))).replaceAll(":player:", player.getName()).replaceAll(":result:", String.valueOf(total + mod)).replaceAll(":dice:", die + "d" + side + modString));
            }
        }
    }

    private void speak(Player player, double distance, String message) {
        List<Entity> entities = player.getNearbyEntities(distance, distance, distance);
        entities.add(player);
        for (Object entity : entities) {
            if ((entity instanceof Player)) {
                new Chat((Player) entity).sendMessage(message, (Player) entity);
            }
        }
    }

    private void shout(String message) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            new Chat(target).sendMessage(message, target);
        }
    }

    private int parseInt(String intAsString, int max) {
        try {
            return Math.min(Math.max(Integer.parseInt(intAsString), -max), max);
        } catch (NumberFormatException ignored) {
        }
        return Integer.MAX_VALUE;
    }
}
