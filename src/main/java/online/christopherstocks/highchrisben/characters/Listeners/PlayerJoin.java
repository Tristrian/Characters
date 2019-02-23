package online.christopherstocks.highchrisben.characters.Listeners;

import online.christopherstocks.highchrisben.characters.Ext.Updater;
import online.christopherstocks.highchrisben.characters.Libs.Character;
import online.christopherstocks.highchrisben.characters.Libs.Chat;
import online.christopherstocks.highchrisben.characters.Libs.PluginConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Chat chat = new Chat(player);
        PluginConfig pluginConfig = new PluginConfig();

        if (pluginConfig.getBoolean("login-create")) {
            new Character(player);
        }

        if (pluginConfig.getBoolean("set-speed-join")) {
            if (player.hasPermission("characters.create")) {
                for (String method : pluginConfig.getStringList("travel-methods")) {
                    if (method.split(":")[0].equalsIgnoreCase(new Character(player).getString("travel.travel"))) {
                        player.setWalkSpeed(Float.parseFloat(method.split(":")[1]) / 10.0F);
                    }
                }
            } else {
                player.setWalkSpeed((float) (pluginConfig.getDouble("travel-default-speed") / 10.0F));
            }
        }

        if (player.hasPermission("characters.updater")) {
            try {
                Updater updater = new Updater();
                if (updater.getLatestVersion().contains("b") && pluginConfig.getBoolean("beta-updates") && updater.checkForUpdates()) {
                    chat.sendMessage(pluginConfig.getString("update-new"), player);
                } else if (updater.checkForUpdates() && !updater.getLatestVersion().contains("b")) {
                    chat.sendMessage(pluginConfig.getString("update-new"), player);
                }
            } catch (Exception error) {
                chat.sendMessage(pluginConfig.getString("update-fail"), player);
            }
        }
    }
}
