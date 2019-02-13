package online.christopherstocks.highchrisben.characters.Libs;

import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Character {
    private PluginConfig pluginConfig = new PluginConfig();
    private Config config;
    private Player player;

    public Character(Player player) {
        this.player = player;
        pluginConfig.reload();
        create();
    }

    private Config getConfig() {
        return new Config(pluginConfig.getInstance(), File.separator + "Characters" + File.separator + player.getUniqueId());
    }

    private void create() {
        if (player.hasPermission("characters.create")) {
            config = getConfig();
            int characters = pluginConfig.getInt("characters-max");
            config.addDefault("character-selected", 1);
            if (characters <= 0) {
                characters = 1;
            }
            for (int card = 1; card <= characters; card++) {
                if (pluginConfig.getBoolean("travel-enabled")) {
                    config.addDefault("character.travel.travel." + card, pluginConfig.getString("travel-default-name"));
                }
                if (pluginConfig.getBoolean("karma-enabled")){
                    config.addDefault("character.karma.rank." + card, sortKarma(pluginConfig.getInt("karma-default-amount")));
                    config.addDefault("character.karma.amount." + card, pluginConfig.getString("karma-default-amount"));
                }
                if (pluginConfig.getBoolean("levels-enabled")){
                    config.addDefault("character.levels.level." + card,pluginConfig.getString("level-default"));
                    config.addDefault("character.levels.exp." + card, pluginConfig.getStringList("exp").get(pluginConfig.getInt("level-default") - 1));
                }
                for (String field : pluginConfig.getStringList("fields")) {
                    config.addDefault("character." + field + "." + card, "Empty");
                }
                if (pluginConfig.getBoolean("attribute-enabled")){
                    for (String attribute : pluginConfig.getStringList("attributes")){
                        config.addDefault("character.attribute." + attribute + "." + card, pluginConfig.getInt("attribute-default-amount"));
                    }
                }
                if (pluginConfig.getBoolean("races-classes-enabled")){
                    config.addDefault("character.levels.points." + card,  pluginConfig.getStringList("points").get(pluginConfig.getInt("level-default") - 1));
                    for (String stat : pluginConfig.getStringList("stats")) {
                        config.addDefault("character.races-classes." + stat + "." + card, pluginConfig.getInt("stat-default"));
                    }
                    config.addDefault("characters.races-classes.race." + card, pluginConfig.getString("race-default"));
                    config.addDefault("characters.races-classes.class." + card, pluginConfig.getString("class-default"));
                }
            }
            config.options().copyDefaults(true);
            config.save();
            player.setWalkSpeed((float) (pluginConfig.getDouble("travel-default-speed") / 10.0F));
        }
    }

    public void resetPoints() {
        for (String stat : pluginConfig.getStringList("stats")) {
            config.set("races-classes." + stat, 0);
        }
        config.set("levels.points", pluginConfig.getStringList("points").get(getInt("levels.level") - 1));
        config.save();
    }

    public String getString(String path) {
        return config.getString("character." + path + "." + config.getInt("character-selected"));
    }

    public int getInt(String path) {
        return Integer.parseInt(config.getString("character." + path + "." + config.getInt("character-selected")));
    }

    public void set(String path, Object value) {
        config.set("character." + path + "." + config.getInt("character-selected"), value);
        config.save();
    }

    public void reset(int card) {
        for (String field : pluginConfig.getStringList("fields")) {
            config.set("character." + field + "." + card, "Empty");
        }
        config.save();
    }

    public void reset() {
        for (String field : pluginConfig.getStringList("fields")) {
            config.set("character." + field + "." + config.getInt("character-selected"), "Empty");
        }
        config.save();
    }

    public int getSlot() {
        return config.getInt("character-selected");
    }

    public void setSlot(int slot) {
        config.set("character-selected", slot);
        config.save();
    }

    public String sortKarma(int value) {
        List<String> levels = pluginConfig.getStringList("karma-levels");
        List<String> amounts = new ArrayList<>(), ranks = new ArrayList<>();
        String[] split;
        for (String level : levels) {
            split = level.split(":");
            ranks.add(split[0]);
            amounts.add(split[1]);
        }
        int index = 0;
        for (int i = 0; i < amounts.size(); i++) {
            if (value >= Integer.parseInt(amounts.get(i))) {
                index = i;
            }
        }
        return ranks.get(index);
    }
}
