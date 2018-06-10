package com.tripixelstudios.highchrisben.characters.Util;

import org.bukkit.entity.Player;

import java.util.List;

public class Travel {
    private PluginConfig pluginConfig = new PluginConfig();
    private Player player;
    private List<String> speeds;

    public Travel(Player player){
        this.player = player;
        this.speeds = pluginConfig.getStringList("travel");
    }

    public boolean isSpeed(String speed){
        for (String speedList : speeds){
            if (speedList.toLowerCase().equalsIgnoreCase(speed)){
                return true;
            }
        }
        return false;
    }

    public void setSpeed(String speed){
        this.player.setWalkSpeed((float) (pluginConfig.getDouble("travel-" + speed.toLowerCase()) / 10.0F));
    }

    public String getSpeed(){
        double playerSpeed = this.player.getWalkSpeed() * 10;
        for (String speed : speeds){
            if (playerSpeed == pluginConfig.getDouble("travel-" + speed.toLowerCase())){
                return speed;
            }
        }
        return null;
    }

    public boolean getEnabled(){
        if (pluginConfig.getBoolean("travel-enabled")){
            return true;
        }
        return false;
    }
}
