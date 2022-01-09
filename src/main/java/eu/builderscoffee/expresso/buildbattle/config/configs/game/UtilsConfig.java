package eu.builderscoffee.expresso.buildbattle.config.configs.game;

import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.expresso.buildbattle.config.ConfigResponsible;
import lombok.val;
import org.bukkit.Material;

public class UtilsConfig implements ConfigResponsible {

    @Override
    public ServerManagerResponse response(ServerManagerResponse response) {
        val itemsAction = new ServerManagerResponse.Items();
        itemsAction.setType("game");

        itemsAction.addItem(2, 4, new ItemBuilder(Material.MAP).setName("§aBackup World").build(), "worldbackup");

        // Add Action to response
        response.getActions().add(itemsAction);
        return response;
    }
}
