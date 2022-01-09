package eu.builderscoffee.expresso.buildbattle.config.configs.game;

import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.config.ConfigResponsible;
import eu.builderscoffee.expresso.buildbattle.config.configs.PhasesConfig;
import lombok.val;
import org.bukkit.Material;

public class StartConfig implements ConfigResponsible {

    @Override
    public ServerManagerResponse response(ServerManagerResponse response) {
        val itemsAction = new ServerManagerResponse.Items();

        itemsAction.setType("game");
        if (!ExpressoBukkit.getBbGame().getBbGameManager().isRunning() || ExpressoBukkit.getBbGame().isPaused()) {
            itemsAction.addItem(2, 4, new ItemBuilder(Material.WOOL, 1, (short) 13).setName("§aDémarer").build(), "start");

            // Add Action to response
            response.getActions().add(itemsAction);

            switch (ExpressoBukkit.getBbGame().getBbGameTypes()) {
                case EXPRESSO:
                    // Add return item
                    addPreviousConfigItem(response, PhasesConfig.class);
                    break;
                case CLASSIC:
                case TOURNAMENT:
                    break;
            }
        }
        return response;
    }
}
