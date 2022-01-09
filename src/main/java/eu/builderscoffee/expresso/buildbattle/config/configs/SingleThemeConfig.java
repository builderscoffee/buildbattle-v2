package eu.builderscoffee.expresso.buildbattle.config.configs;

import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.api.common.data.DataManager;
import eu.builderscoffee.api.common.data.tables.BuildbattleThemeNameEntity;
import eu.builderscoffee.api.common.data.tables.Profil;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerRequest;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.config.ConfigTemplate;
import lombok.val;
import org.bukkit.Material;

public class SingleThemeConfig extends ConfigTemplate {

    public SingleThemeConfig() {
        super("theme");
    }

    @Override
    public ServerManagerResponse request(ServerManagerRequest request, ServerManagerResponse response) {
        System.out.println(">> Request " + this.getClass().getSimpleName());
        ExpressoBukkit.getBbGame().getBbGameManager().setTheme(request.getData()); //TODO Register BuildbattleThemeEntity
        return redirect(PlotConfig.class, response);
    }

    @Override
    public ServerManagerResponse response(ServerManagerResponse response) {
        System.out.println(">> Response " + this.getClass().getSimpleName());
        val pageItemsAction = new ServerManagerResponse.PageItems();
        pageItemsAction.setType(type);
        // Get Themes form database
        val data = DataManager.getBuildbattleThemeNameStore().select(BuildbattleThemeNameEntity.class)
                .where(BuildbattleThemeNameEntity.LANGUAGE.eq(Profil.Languages.FR)).get();

        // Paginate the themes
        data.forEach(theme -> {
            pageItemsAction.addItem(new ItemBuilder(Material.MAP).setName("§a" + theme.getName()).build(), theme.getName());
        });

        // Add Action to response
        response.getActions().add(pageItemsAction);

        // Add return item
        addPreviousConfigItem(response, PhasesConfig.class);
        return response;
    }
}
