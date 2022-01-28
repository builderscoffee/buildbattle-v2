package eu.builderscoffee.expresso.buildbattle.config.configs;

import eu.builderscoffee.commons.common.redisson.packets.ServerManagerRequest;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.BuildBattleCategory;
import eu.builderscoffee.expresso.buildbattle.config.ConfigTemplate;
import eu.builderscoffee.expresso.buildbattle.config.configs.game.GameSettings;
import eu.builderscoffee.expresso.buildbattle.config.configs.game.StartConfig;
import org.bukkit.Bukkit;

import java.util.Objects;

public class RequestConfig extends ConfigTemplate {
    public RequestConfig() {
        super("request_config");
    }

    @Override
    public ServerManagerResponse request(ServerManagerRequest request, ServerManagerResponse response) {
        System.out.println(">> Request " + this.getClass().getSimpleName());
        // Check if the category is chosen
        if (getRequestable(BuildbattleCategoryConfig.class).getCategory().equals(BuildBattleCategory.NONE)) {
            return redirect(BuildbattleCategoryConfig.class, response);
        }
        // Check if the BuildBattleType is already defined
        else if(Objects.isNull(ExpressoBukkit.getBuildBattle().getType())){
            return redirect(BuildBattleTypesConfig.class, response);
        }
        // Check if the phases have been initialized
        else if (Objects.isNull(ExpressoBukkit.getBuildBattle().getInstancePhases())) {
            return redirect(PhasesConfig.class, response);
        }
        // Check if the theme has been chosen
        else if (Objects.isNull(ExpressoBukkit.getBuildBattle().getGameManager().getTheme())) {
            return redirect(SingleThemeConfig.class, response);
        }
        // Check if the world has been created
        else if (Objects.isNull(Bukkit.getWorld(ExpressoBukkit.getSettings().getPlotWorldName()))) {
            return redirect(PlotConfig.class, response);
        }
        // Check if the game is ready or paused
        else if (!ExpressoBukkit.getBuildBattle().isReady() || ExpressoBukkit.getBuildBattle().isPaused()) {
            return redirect(StartConfig.class, response);
        }
        // Else, return the current game config
        else {
            return redirect(GameSettings.class, response);
        }
    }

    @Override
    public ServerManagerResponse response(ServerManagerResponse response) {
        System.out.println(">> Response " + this.getClass().getSimpleName());
        return null;
    }
}
