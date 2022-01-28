package eu.builderscoffee.expresso.buildbattle.config.configs;

import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerRequest;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.BuildBattleCategory;
import eu.builderscoffee.expresso.buildbattle.config.ConfigTemplate;
import eu.builderscoffee.expresso.buildbattle.games.classic.ClassicType;
import lombok.val;

public class BuildBattleTypesConfig extends ConfigTemplate {

    public BuildBattleTypesConfig() {
        super("expresso");
    }

    @Override
    public ServerManagerResponse request(ServerManagerRequest request, ServerManagerResponse response) {
        System.out.println(">> Request " + this.getClass().getSimpleName());

        ExpressoBukkit.getBuildBattle().setType(ExpressoBukkit.getBuildBattle().getTypes().stream()
                .filter(buildBattleType -> buildBattleType.getClass().getName().equals(request.getData()))
                .findFirst().get());
        return redirect(PhasesConfig.class, response);
    }

    @Override
    public ServerManagerResponse response(ServerManagerResponse response) {
        System.out.println(">> Response " + this.getClass().getSimpleName());

        val pageItemsAction = new ServerManagerResponse.PageItems();
        pageItemsAction.setType(getType());
        ExpressoBukkit.getBuildBattle().getTypes().stream()
                .filter(buildBattleType -> buildBattleType.getCategory().equals(getRequestable(BuildbattleCategoryConfig.class).getCategory()))
                .forEach(buildBattleType -> pageItemsAction.addItem(new ItemBuilder(buildBattleType.getIcon().getType(), 1)
                        .setName("§a" + buildBattleType.getName())
                        .addLoreLine(buildBattleType.getDescription())
                        .build(), buildBattleType.getClass().getName()));

        response.getActions().add(pageItemsAction);

        addPreviousConfigItem(response, BuildbattleCategoryConfig.class);
        return response;
    }
}
