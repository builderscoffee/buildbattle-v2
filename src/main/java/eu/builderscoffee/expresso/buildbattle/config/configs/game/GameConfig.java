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
import org.bukkit.inventory.ItemStack;

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
                    new WorldBuilder.DefaultWorldBuilder()
                            .setBedrock(true)
                            .setPlotFilling(new ItemStack(Material.DIRT))
                            .setPlotFloor(new ItemStack(Material.GRASS, 1))
                            .setPlotHeight(64)
                            .setPlotSize(getRequestable(PlotConfig.class).getPlotSize())
                            .setRoadBlock(new ItemStack(Material.QUARTZ_BLOCK))
                            .setRoadHeight(64)
                            .setRoadWidth(7)
                            .setWall(new ItemStack(Material.STONE_SLAB2))
                            .setWallClaimed(new ItemStack(Material.STONE_SLAB2, 1, (short) 2))
                            .setWallFilling(new ItemStack(Material.STONE))
                            .setWallHeight(64)
                            .generate(ExpressoBukkit.getSettings().getPlotWorldName());
                }
                // Lancer la partie
                ExpressoBukkit.getBbGame().setReady(true);
                ExpressoBukkit.getBbGame().getBbGameManager().startGame();
                return redirect(EndConfig.class, response);
            case "stop":
                // Stopper la partie
                if (ExpressoBukkit.getBbGame().isReady()) {
                    ExpressoBukkit.getBbGame().getBbGameManager().cancelGame();
                    //TODO Send message to server , fix console error
                }
                break;
            case "pause":
                Log.get().info("Receive Pause Action");
                // Mettre en pause la partie
                ExpressoBukkit.getBbGame().getBbGameManager().PauseGame();
                // Envoyer le menu start
                return redirect(StartConfig.class, response);
            case "worldbackup":
                if (!BackupUtils.backupOfWorldExist(ExpressoBukkit.getSettings().getPlotWorldName(), ExpressoBukkit.getInstance().getServer().getServerName())) {
                    BackupUtils.backupWorld(ExpressoBukkit.getSettings().getPlotWorldName(), ExpressoBukkit.getInstance().getServer().getServerName());
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

        itemsAction.addItem(2, 2, new ItemBuilder(Material.WOOL, 1, (short) 1).setName("§cStop").build(), "stop");
        itemsAction.addItem(2, 4, new ItemBuilder(Material.WOOL, 1, (short) 6).setName("§7Pause").build(), "pause");
        itemsAction.addItem(2, 6, new ItemBuilder(Material.DROPPER, 1).setName("§6Reset").build(), "reset");

        // Add Action to response
        response.getActions().add(itemsAction);
        return response;
    }
}
