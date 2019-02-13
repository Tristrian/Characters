package online.christopherstocks.highchrisben.characters.Ext;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import online.christopherstocks.highchrisben.characters.Characters;
import online.christopherstocks.highchrisben.characters.Libs.Character;
import online.christopherstocks.highchrisben.characters.Libs.PluginConfig;
import org.bukkit.entity.Player;

public class Placeholders extends EZPlaceholderHook {

    public Placeholders(Characters instance) {
        super(instance, "characters");
    }

    @Override
    public String onPlaceholderRequest(Player player, String s) {
        PluginConfig pluginConfig = new PluginConfig();
        Character character = new Character(player);
        if (s.equalsIgnoreCase("travel_travel")){
            return character.getString("travel.travel");
        }
        if (s.equalsIgnoreCase("karma_rank")){
            return character.getString("karma.rank");
        }
        if (s.equalsIgnoreCase("karma_amount")){
            return character.getString("karma.amount");
        }
        if (s.equalsIgnoreCase("levels_level")){
            return character.getString("levels.level");
        }
        if (s.equalsIgnoreCase("levels_exp")){
            return character.getString("levels.exp");
        }
        if (s.equalsIgnoreCase("levels_points")){
            return character.getString("levels.points");
        }
        if (s.equalsIgnoreCase("races-classes_class")){
            return character.getString("races-classes.class");
        }
        if (s.equalsIgnoreCase("races-classes_race")){
            return character.getString("races-classes.race");
        }
        for (String placeholder : pluginConfig.getStringList("fields")) {
            if (s.equalsIgnoreCase(placeholder)) {
                return character.getString(placeholder);
            }
        }
        for (String placeholder : pluginConfig.getStringList("attributes")){
            if (s.equalsIgnoreCase(placeholder)) {
                return character.getString("attribute." + placeholder);
            }
        }
        for (String placeholder : pluginConfig.getStringList("stats")){
            if (s.equalsIgnoreCase(placeholder)){
                return character.getString("races-classes." + placeholder);
            }
        }
        return null;
    }
}
