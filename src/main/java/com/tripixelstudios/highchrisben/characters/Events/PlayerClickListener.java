package com.tripixelstudios.highchrisben.characters.Events;

import com.tripixelstudios.highchrisben.characters.Util.PluginConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.HashMap;

public class PlayerClickListener implements Listener {
    private final PluginConfig pluginConfig = new PluginConfig();
    private HashMap<Player, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onRightClickPlayer(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();

        if ((e.getRightClicked() instanceof Player)) {
            if (pluginConfig.getInt("player-click") == 1) {
                if (player.isSneaking()) {
                    preform(e, player);
                    return;
                }
                return;
            }

            if (pluginConfig.getInt("player-click") == 0) {
                preform(e, player);
            }
        }
    }

    private boolean checkTime(Player player) {
        if (cooldowns.containsKey(player)) {
            int cooldown = pluginConfig.getInt("player-click-cooldown") * 1000;
            if (cooldown < 1000) {
                cooldown = 1000;
            }
            if (System.currentTimeMillis() - cooldowns.get(player) >= cooldown) {
                cooldowns.remove(player);
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    private void preform(PlayerInteractEntityEvent e, Player player) {
        Player target = (Player) e.getRightClicked();
        if (!checkTime(player)) {
            return;
        }
        player.performCommand("char display " + target.getName() + " " + pluginConfig.getInt("display-click-page"));
        cooldowns.put(player, System.currentTimeMillis());
    }
}
