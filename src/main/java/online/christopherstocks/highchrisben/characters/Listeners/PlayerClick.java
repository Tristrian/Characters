package online.christopherstocks.highchrisben.characters.Listeners;

import online.christopherstocks.highchrisben.characters.Libs.PluginConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.HashMap;

public class PlayerClick implements Listener {
    private final PluginConfig pluginConfig = new PluginConfig();
    private HashMap<Player, Long> cooldowns = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        if (pluginConfig.getBoolean("click-display"))
            if (e.getRightClicked() instanceof Player) {
                Player target = (Player) e.getRightClicked();
                if (pluginConfig.getBoolean("click-shift") && player.isSneaking()) {
                    preform(player, target);
                }
                if (!pluginConfig.getBoolean("click-shift")) {
                    preform(player, target);
                }
            }
    }

    private boolean cooldownOver(Player player) {
        if (cooldowns.containsKey(player)) {
            if (System.currentTimeMillis() - cooldowns.get(player) >= (pluginConfig.getInt("click-cooldown") * 1000)) {
                cooldowns.remove(player);
                return true;
            }
            return false;
        }
        return true;
    }

    private void preform(Player player, Player target) {
        if (cooldownOver(player)) {
            cooldowns.put(player, System.currentTimeMillis());
            player.performCommand("char display " + target.getName());
        }
    }
}
