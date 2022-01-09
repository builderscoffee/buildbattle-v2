package eu.builderscoffee.expresso.listeners.bukkit;

import eu.builderscoffee.api.bukkit.utils.LocationsUtil;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.BuildBattleManager;
import eu.builderscoffee.expresso.buildbattle.toolbars.ToolbarManager;
import eu.builderscoffee.expresso.configuration.SettingsConfiguration;
import eu.builderscoffee.expresso.utils.MessageUtils;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

public class PlayerListener implements Listener {

    SettingsConfiguration settings = ExpressoBukkit.getSettings();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        val messages = MessageUtils.getMessageConfig(player);

        // Scoreboard Updater
        if (Objects.nonNull(ExpressoBukkit.getBbGame())) {
            ExpressoBukkit.getBbGame().getBbGameTypes().getBaseBoard().update(player);
        }
        // Player Inventory
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setAllowFlight(true);
        player.getInventory().clear();

        // Teleport Player If player as plot
        World world = Bukkit.getWorld(ExpressoBukkit.getSettings().getPlotWorldName());
        if (Objects.nonNull(world)) {
            // If World in config exist teleport to spawn location
            player.teleport(LocationsUtil.getLocationFromString(ExpressoBukkit.getSettings().getGlobal_spawn_location()));
        } else {
            // If World doesn't exist teleport to default location
            player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        }

        if (Objects.nonNull(ExpressoBukkit.getBbGame())) {
            if (ExpressoBukkit.getBbGame().getGameState().equals(BuildBattleManager.GameState.IN_GAME)) {
                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage(messages.getGame().getPlotAuto().replace("%prefix%", MessageUtils.getDefaultMessageConfig().getPrefix()));
                ExpressoBukkit.getBbGame().getToolbarManager().addToolBar(player, ToolbarManager.Toolbars.SPECTATOR);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDisconnect(PlayerQuitEvent event) {
        // Scoreboard clean
        ExpressoBukkit.getBbGame().getBbGameTypes().getBaseBoard().remove(event.getPlayer());

        // Clean toolbars
        ExpressoBukkit.getBbGame().getToolbarManager().removeToolBar(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().toLowerCase().contains("/plot auto") || event.getMessage().toLowerCase().contains("/plot claim")) {
            if (ExpressoBukkit.getBbGame().getGameState().equals(BuildBattleManager.GameState.IN_GAME)) {
                ExpressoBukkit.getBbGame().addCompetitor(event.getPlayer());
            } else {
                event.setCancelled(true);
                event.getPlayer().sendMessage(MessageUtils.getMessageConfig(event.getPlayer()).getGame().getPlotAutoSpam());
            }
        }
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!ExpressoBukkit.getBbGame().getGameState().equals(BuildBattleManager.GameState.IN_GAME)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!ExpressoBukkit.getBbGame().getGameState().equals(BuildBattleManager.GameState.IN_GAME)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}
