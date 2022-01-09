package eu.builderscoffee.expresso.listeners.redisson;

import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.listeners.PacketListener;
import eu.builderscoffee.api.common.redisson.listeners.ProcessPacket;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerRequest;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.commons.common.redisson.topics.CommonTopics;
import eu.builderscoffee.expresso.buildbattle.config.ConfigRequestable;
import eu.builderscoffee.expresso.buildbattle.config.ConfigResponsible;
import eu.builderscoffee.expresso.utils.ReflectionUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class ConfigListener implements PacketListener {

    @Getter
    private static Map<Class<? extends ConfigRequestable>, ConfigRequestable> requestables = new HashMap<>();
    @Getter
    private static Map<Class<? extends ConfigResponsible>, ConfigResponsible> responsibles = new HashMap<>();

    public ConfigListener() {
        ReflectionUtils.reflectInstances(ConfigRequestable.class.getPackage(), ConfigRequestable.class)
                .forEach(requestable -> requestables.put(requestable.getClass(), requestable));
        ReflectionUtils.reflectInstances(ConfigResponsible.class.getPackage(), ConfigResponsible.class)
                .forEach(responsable -> responsibles.put(responsable.getClass(), responsable));
    }

    @ProcessPacket
    public void onConfigRequest(ServerManagerRequest request) {
        System.out.println("Request: " + request.getType());
        getRequestables().values().stream()
                .filter(config -> request.getType().equals(config.getType()))
                .forEach(config -> {
                    Redis.publish(CommonTopics.SERVER_MANAGER, config.request(request, new ServerManagerResponse(request)));
                });
    }
}
