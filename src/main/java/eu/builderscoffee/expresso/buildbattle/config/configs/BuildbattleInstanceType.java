package eu.builderscoffee.expresso.buildbattle.config.configs;

import eu.builderscoffee.commons.common.redisson.packets.ServerManagerRequest;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.BuildBattle;
import eu.builderscoffee.expresso.buildbattle.BuildBattleInstanceType;
import eu.builderscoffee.expresso.buildbattle.config.ConfigTemplate;
import lombok.val;

import java.util.Objects;

public class BuildbattleInstanceType extends ConfigTemplate {


    public BuildbattleInstanceType() {
        super("type");
    }

    @Override
    public ServerManagerResponse request(ServerManagerRequest request, ServerManagerResponse response) {
        System.out.println(">> Request " + this.getClass().getSimpleName());
        val type = BuildBattleInstanceType.valueOf(request.getData());
        if (Objects.nonNull(ExpressoBukkit.getBbGame()))
            ExpressoBukkit.getBbGame().getBbGameTypes().getBaseBoard().removeAll();
        ExpressoBukkit.setBbGame(new BuildBattle(type));
        return redirect(GameTypesConfig.class, response);
    }

    @Override
    public ServerManagerResponse response(ServerManagerResponse response) {
        System.out.println(">> Response " + this.getClass().getSimpleName());
        val itemsAction = new ServerManagerResponse.Items();

        itemsAction.setType(type);
        int column = 2;
        for (BuildBattleInstanceType bbit : BuildBattleInstanceType.values()) {
            if (bbit.equals(BuildBattleInstanceType.NONE)) continue;
            itemsAction.addItem(2, column, bbit.getIcon(), bbit.name());
            column += 2;
        }

        // Add Action to response
        response.getActions().add(itemsAction);
        return response;
    }
}
