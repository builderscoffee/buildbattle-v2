package eu.builderscoffee.expresso.buildbattle.config.configs;

import eu.builderscoffee.commons.common.redisson.packets.ServerManagerRequest;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.expresso.buildbattle.config.ConfigRequestable;
import eu.builderscoffee.expresso.listeners.redisson.ConfigListener;
import lombok.val;

import java.util.Objects;

public class PreviousConfig implements ConfigRequestable {

    @Override
    public String getType() {
        return "previous_manager";
    }

    @Override
    public ServerManagerResponse request(ServerManagerRequest request, ServerManagerResponse response) {
        val responsible = ConfigListener.getResponsibles().keySet().stream()
                .filter(entry -> entry.getName().equals(request.getData()))
                .findFirst().get();

        if (Objects.nonNull(responsible)) {
            redirect(responsible, response);
        }

        return redirect(BuildbattleInstanceType.class, response);
    }
}
