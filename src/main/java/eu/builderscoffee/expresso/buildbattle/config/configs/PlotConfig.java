package eu.builderscoffee.expresso.buildbattle.config.configs;

import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerRequest;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.config.ConfigTemplate;
import eu.builderscoffee.expresso.buildbattle.config.configs.game.StartConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.bukkit.Material;

import java.util.Arrays;

public class PlotConfig extends ConfigTemplate {

    @Getter
    @Setter
    private int plotSize = 0;

    public PlotConfig() {
        super("plot");
    }

    @Override
    public ServerManagerResponse request(ServerManagerRequest request, ServerManagerResponse response) {
        System.out.println(">> Request " + this.getClass().getSimpleName());
        val maxPlotSize = ExpressoBukkit.getSettings().getPlotMaxSize();
        val minPlotSize = ExpressoBukkit.getSettings().getPlotMinSize();
        switch (request.getData()) {
            case "size":
                switch (request.getItemAction()) {
                    case LEFT_CLICK:
                        plotSize = plotSize >= maxPlotSize ? maxPlotSize : plotSize + 1;
                        break;
                    case SHIFT_LEFT_CLICK:
                        plotSize = plotSize >= maxPlotSize ? maxPlotSize : plotSize + 10;
                        break;
                    case RIGHT_CLICK:
                        plotSize = plotSize <= minPlotSize ? 0 : plotSize - 1;
                        break;
                    case SHIFT_RIGHT_CLICK:
                        plotSize = plotSize <= minPlotSize ? 0 : plotSize - 10;
                        break;
                }
                return response(response);
            case "mapgen":
                return redirect(StartConfig.class, response);
        }
        return response(response);
    }

    @Override
    public ServerManagerResponse response(ServerManagerResponse response) {
        System.out.println(">> Response " + this.getClass().getSimpleName());
        val itemsAction = new ServerManagerResponse.Items();
        itemsAction.setType("plot");

        itemsAction.addItem(2, 2, new ItemBuilder(Material.NAME_TAG).setName("§aTaille").addLoreLine(Arrays.asList("§7Taille du plot " + plotSize, "", "§aClick gauche +1 ", "§aClick droit -1", "§aShift click gauche +10", "§aShift click droit -10")).build(), "size");
        itemsAction.addItem(2, 6, new ItemBuilder(Material.BARRIER).setName("§cBientot").build(), "commingsoon");
        itemsAction.addItem(3, 4, new ItemBuilder(Material.WOOL, 1, (short) 13).setName("§aValider la génération").build(), "mapgen");

        // Add Action to response
        response.getActions().add(itemsAction);

        // Add return item
        addPreviousConfigItem(response, SingleThemeConfig.class);
        return response;
    }
}
