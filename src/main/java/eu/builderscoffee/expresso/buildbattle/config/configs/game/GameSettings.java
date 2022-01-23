package eu.builderscoffee.expresso.buildbattle.config.configs.game;

import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.expresso.buildbattle.config.ConfigResponsible;
import lombok.val;
import org.bukkit.Material;

public class GameSettings implements ConfigResponsible {

    @Override
    public ServerManagerResponse response(ServerManagerResponse response) {
        val itemsAction = new ServerManagerResponse.Items();
        itemsAction.setType("game");

        itemsAction.addItem(2, 2, new ItemBuilder(Material.COMPASS).setName("§7Gestion de la partie").build(), "settings");
        itemsAction.addItem(2, 6, new ItemBuilder(Material.CRAFTING_TABLE).setName("§7Utilitaire de la partie").build(), "utils");

        // Add Action to response
        response.getActions().add(itemsAction);
        return response;
    }
}
