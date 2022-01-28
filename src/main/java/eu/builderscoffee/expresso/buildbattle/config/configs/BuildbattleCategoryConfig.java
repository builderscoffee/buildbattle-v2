package eu.builderscoffee.expresso.buildbattle.config.configs;

import eu.builderscoffee.commons.common.redisson.packets.ServerManagerRequest;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.BuildBattleCategory;
import eu.builderscoffee.expresso.buildbattle.config.ConfigTemplate;
import lombok.Getter;
import lombok.val;

import java.util.Objects;

public class BuildbattleCategoryConfig extends ConfigTemplate {


    @Getter
    private BuildBattleCategory category = BuildBattleCategory.NONE;

    public BuildbattleCategoryConfig() {
        super("type");
    }

    @Override
    public ServerManagerResponse request(ServerManagerRequest request, ServerManagerResponse response) {
        System.out.println(">> Request " + this.getClass().getSimpleName());
        category = BuildBattleCategory.valueOf(request.getData());
        if (Objects.nonNull(ExpressoBukkit.getBuildBattle().getType()))
            ExpressoBukkit.getBuildBattle().getType().getCategory().getBaseBoard().removeAll();
        return redirect(BuildBattleTypesConfig.class, response);
    }

    @Override
    public ServerManagerResponse response(ServerManagerResponse response) {
        System.out.println(">> Response " + this.getClass().getSimpleName());
        val itemsAction = new ServerManagerResponse.Items();

        itemsAction.setType(type);
        int column = 2;
        for (BuildBattleCategory bbit : BuildBattleCategory.values()) {
            if (bbit.equals(BuildBattleCategory.NONE)) continue;
            itemsAction.addItem(2, column, bbit.getIcon(), bbit.name());
            column += 2;
        }

        // Add Action to response
        response.getActions().add(itemsAction);
        return response;
    }
}
