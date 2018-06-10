package com.tripixelstudios.highchrisben.characters.Util;

import org.bukkit.entity.Player;

public class Skills {
    private final PluginConfig pluginConfig = new PluginConfig();
    private Player player;
    private Character character;

    public Skills(Player player) {
        this.player = player;
        this.character = new Character(player);
    }

    boolean getEnabled() {
        return pluginConfig.getBoolean("skills-enabled");
    }

    public void alterHitpoints(int amount) {
        int currentHitpoints = character.getInt("hitpoints");
        if (currentHitpoints + amount < -1) {
            new PlayerChat(player).headermsg("&6You do not have enough Hitpoints!");
        } else {
            character.set("hitpoints", currentHitpoints + amount);
        }
    }

    public void setHitpoints(int amount) {
        character.set("hitpoints", amount);
    }

    public String getHitpoints() { return character.getString("hitpoints"); }

    public void alterMagicka(int amount) {
        int currentMagicka = character.getInt("magicka");
        if (currentMagicka + amount < 0) {
            new PlayerChat(player).headermsg("&6You do not have enough Magicka!");
        } else {
            character.set("magicka", currentMagicka + amount);
        }
    }

    public void setMagicka(int amount) {
        character.set("magicka", amount);
    }

    public String getMagicka() { return character.getString("magicka"); }

    public void alterStamina(int amount) {
        int currentStamina = character.getInt("stamina");
        if (currentStamina + amount < 0) {
            new PlayerChat(player).headermsg("&6You do not have enough Stamina!");
        } else {
            character.set("stamina", currentStamina + amount);
        }
    }

    public void setStamina(int amount) {
        character.set("stamina", amount);
    }

    public String getStamina() { return character.getString("stamina"); }
}
