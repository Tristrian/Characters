package com.tripixelstudios.highchrisben.characters.Events;

import com.tripixelstudios.highchrisben.characters.Util.Character;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements org.bukkit.event.Listener {

    @org.bukkit.event.EventHandler
    public void onJoinCharSetup(PlayerJoinEvent e) {
        new Character(e.getPlayer());
    }
}
