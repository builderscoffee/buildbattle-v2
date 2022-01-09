package eu.builderscoffee.expresso.buildbattle.config.configs.game;

import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.expresso.buildbattle.config.ConfigResponsible;

public class EndConfig implements ConfigResponsible {

    @Override
    public ServerManagerResponse response(ServerManagerResponse response) {
        response.setFinished(true);
        return response;
    }
}
