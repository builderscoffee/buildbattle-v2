package eu.builderscoffee.expresso.buildbattle.config.configs.game;

import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerRequest;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.config.ConfigTemplate;
import eu.builderscoffee.expresso.buildbattle.config.configs.PlotConfig;
import eu.builderscoffee.expresso.utils.BackupUtils;
import eu.builderscoffee.expresso.utils.Log;
import eu.builderscoffee.expresso.utils.WorldBuilder;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Objects;

public class GameConfig extends ConfigTemplate {

    public GameConfig() {
        super("game");
    }

    @Override
    public ServerManagerResponse request(ServerManagerRequest request, ServerManagerResponse response) {
        System.out.println(">> Request " + this.getClass().getSimpleName());
        switch (request.getData()) {
            case "settings":
                return response(response);
            case "utils":
                return redirect(UtilsConfig.class, response);
            case "start":
                // Checker si le monde exist
                if (Objects.nonNull(ExpressoBukkit.getInstance().getServer().getWorld(ExpressoBukkit.getSettings().getPlotWorldName()))) {
                    // Si oui le supprimer
                    Bukkit.getWorld(ExpressoBukkit.getSettings().getPlotWorldName()).getWorldFolder().delete();
                }
                if (Objects.isNull(ExpressoBukkit.getInstance().getServer().getWorld(ExpressoBukkit.getSettings().getPlotWorldName()))) {
                    // Générer la map
                    Bukkit.getScheduler().runTask(ExpressoBukkit.getInstance(), () ->
                            new WorldBuilder.DefaultWorldBuilder()
                                    .setHasBedrock(true)
                                    .setRoadWidth(7)
                                    .setPlotBlock(Material.DIRT)
                                    .setPlotHeight(64)
                                    .setRoadHeight(64)
                                    .setRoadBlock(Material.QUARTZ_BLOCK)
                                    .setWallFillingBlock(Material.STONE)
                                    .setBlockOnTopOfPlotBlock(true)
                                    .setPlotFloorBlock(Material.GRASS_BLOCK)
                                    .setPlotWidth(getRequestable(PlotConfig.class).getPlotSize())
                                    .setTopWallBlockClaimed(Material.ACACIA_SLAB)
                                    .setTopWallBlockUnClaimed(Material.BIRCH_SLAB)
                                    .setWallHeight(64)
                                    .setup(ExpressoBukkit.getSettings().getPlotWorldName()));
                }
                // Lancer la partie
                ExpressoBukkit.getBuildBattle().setReady(true);
                ExpressoBukkit.getBuildBattle().getGameManager().startGame();
                return redirect(EndConfig.class, response);
            case "stop":
                // Stopper la partie
                if (ExpressoBukkit.getBuildBattle().isReady()) {
                    ExpressoBukkit.getBuildBattle().getGameManager().cancelGame();
                    //TODO Send message to server , fix console error
                }
                break;
            case "pause":
                Log.get().info("Receive Pause Action");
                // Mettre en pause la partie
                ExpressoBukkit.getBuildBattle().getGameManager().PauseGame();
                // Envoyer le menu start
                return redirect(StartConfig.class, response);
            case "worldbackup":
                if (!BackupUtils.backupOfWorldExist(ExpressoBukkit.getSettings().getPlotWorldName(), ExpressoBukkit.getInstance().getServer().getName())) {
                    BackupUtils.backupWorld(ExpressoBukkit.getSettings().getPlotWorldName(), ExpressoBukkit.getInstance().getServer().getName());
                    Log.get().info("Le monde à été backup");
                }
                break;
        }
        return response;
    }

    @Override
    public ServerManagerResponse response(ServerManagerResponse response) {
        System.out.println(">> Response " + this.getClass().getSimpleName());
        val itemsAction = new ServerManagerResponse.Items();
        itemsAction.setType("game");

        itemsAction.addItem(2, 2, new ItemBuilder(Material.RED_WOOL).setName("§cStop").build(), "stop");
        itemsAction.addItem(2, 4, new ItemBuilder(Material.ORANGE_WOOL).setName("§7Pause").build(), "pause");
        itemsAction.addItem(2, 6, new ItemBuilder(Material.DROPPER, 1).setName("§6Reset").build(), "reset");

        // Add Action to response
        response.getActions().add(itemsAction);
        return response;
    }
}
