package eu.builderscoffee.expresso.buildbattle.config;

import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.expresso.listeners.redisson.ConfigListener;
import lombok.val;
import org.bukkit.Material;

public interface ConfigResponsible {
    ServerManagerResponse response(ServerManagerResponse response);

    default void addPreviousConfigItem(ServerManagerResponse response, Class<? extends ConfigResponsible> clazz) {
        val action = new ServerManagerResponse.Items();
        action.setType("previous_manager");
        action.addItem(4, 0, new ItemBuilder(Material.ARROW).setName("§7Retour").build(), clazz.getName());
        response.getActions().add(action);
    }

    default <T extends ConfigResponsible> T getResponsible(Class<T> clazz) {
        return (T) ConfigListener.getResponsibles().get(clazz);
    }
}
