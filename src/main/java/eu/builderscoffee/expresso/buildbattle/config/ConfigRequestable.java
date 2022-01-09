package eu.builderscoffee.expresso.buildbattle.config;

import eu.builderscoffee.commons.common.redisson.packets.ServerManagerRequest;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.expresso.listeners.redisson.ConfigListener;

public interface ConfigRequestable {

    String getType();

    ServerManagerResponse request(ServerManagerRequest request, ServerManagerResponse response);

    default <T extends ConfigRequestable> T getRequestable(Class<T> clazz) {
        return (T) ConfigListener.getRequestables().get(clazz);
    }

    default ServerManagerResponse redirect(Class<? extends ConfigResponsible> clazz, ServerManagerResponse response) {
        return ConfigListener.getResponsibles().get(clazz).response(response);
    }
}
