package eu.builderscoffee.expresso.configuration;


import eu.builderscoffee.api.common.configuration.annotation.Configuration;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@Configuration("settings")
public class SettingsConfiguration {

    // Global settings
    String plotWorldName = "plotevent";
    String global_spawn_location = "plotevent:0:65:0:90.0:0.0";

    // PlotSquared Settings
    int plotMaxSize = 500;
    int plotMinSize = 10;

    // Backup settings
    String path_for_backup = "/home/builderscoffee/backup/minecraft/schematics/";
    String path_for_backup_world = "/home/builderscoffee/backup/minecraft/worlds/";

    // Permission settings
    String expresso_all_permission = "expresso.*";
    String expresso_jury_permission = "expresso.jury";
    String expresso_eplot_permission = "expresso.eplot";

    // Game settings
    List<String> game_plugin_end_disable = Arrays.asList("worldedit", "fastasyncworldedit", "fastasyncvoxelsniper", "voxelsniper", "betterbrushes", "gobrush", "schematicbrush", "schematicsbrowser", "arceon", "gopaint");
    String game_schematic_to_paste = "cobble";

    // Team settings
    int team_maxplayer = 2;

}
