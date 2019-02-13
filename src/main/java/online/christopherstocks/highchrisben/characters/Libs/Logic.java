package online.christopherstocks.highchrisben.characters.Libs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Logic {
    public Player getTarget(String target) {
        try {
            return Bukkit.getPlayer(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean verifyTarget(Player target) {
        if (target == null) {
            return false;
        }
        if (target.hasPermission("characters.create")) {
            new Character(target);
            return true;
        }
        return false;
    }

    public boolean integer(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
