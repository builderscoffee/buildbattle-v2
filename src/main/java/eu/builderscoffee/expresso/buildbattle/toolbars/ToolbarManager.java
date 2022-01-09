package eu.builderscoffee.expresso.buildbattle.toolbars;

import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.toolbars.tools.JoinItem;
import eu.builderscoffee.expresso.buildbattle.toolbars.tools.NotationItem;
import eu.builderscoffee.expresso.buildbattle.toolbars.tools.PlotItem;
import eu.builderscoffee.expresso.buildbattle.toolbars.tools.TeleportationItem;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.*;

public class ToolbarManager implements Listener {

    public static Map<Player, Toolbars> playerToolbars = new HashMap<>();

    public ToolbarManager() {
        // Enregistrer les événements de la class
        Bukkit.getPluginManager().registerEvents(this, ExpressoBukkit.getInstance());
    }

    /***
     * Ajouter une ToolBar à un joueur
     * @param player - Joueur
     * @param toolbars - Barre à outils
     */
    public void addToolBar(@NonNull Player player, @NonNull Toolbars toolbars) {
        // Ajouter le joueur dans la liste active
        playerToolbars.put(player, toolbars);
        // Nettoyer la HotBar du joueur
        cleanHotBar(player);
        // Définir les items suivants la barre choisie en fonction de leur slot
        toolbars.getToolbarItems().forEach(toolbarItem -> player.getInventory().setItem(toolbarItem.getSlot(), toolbarItem.getItem(player)));
    }

    /***
     * Retirer la ToolBar du joueur
     * @param player
     */
    public void removeToolBar(@NonNull Player player) {
        if (playerToolbars.containsKey(player)) {
            // Retirer le joueur de la liste active
            playerToolbars.remove(player);
            // Nettoyer la HotBar du joueur
            cleanHotBar(player);
        }
    }

    /***
     * Nettoyer la hotbar
     * @param player
     */
    public void cleanHotBar(@NonNull Player player) {
        for (int slot = 0; slot < 8; slot++) {
            player.getInventory().clear(slot);
        }
    }

    /***
     * Intéraction avec les items de la toolbar
     * @param event
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        // Checker si le joueur est dans la liste
        if (!playerToolbars.containsKey(event.getPlayer())) return;
        // Check si le joueur à un item en main
        if (Objects.nonNull(event.getPlayer().getInventory().getItemInHand())) {
            if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
                event.setCancelled(true);
                // Check si l'item correspond à la liste de sa toolbar
                val item = playerToolbars.get(event.getPlayer()).getToolbarItems().stream().filter(toolbarItem ->
                        toolbarItem.getSlot() == event.getPlayer().getInventory().getHeldItemSlot()).findFirst().orElse(null);
                if (Objects.nonNull(item)) item.interact(event.getPlayer(), event.getAction());
            }
        }
    }

    public enum Toolbars {

        JURORS("Jury", new PlotItem(0), new TeleportationItem(1), new NotationItem(2)),
        SPECTATOR("Spectateur", new PlotItem(0), new JoinItem(1));

        @Getter
        private final String barName;
        @Getter
        private final List<ToolbarItem> toolbarItems;

        Toolbars(String barName, ToolbarItem... toolbarItems) {
            this.barName = barName;
            this.toolbarItems = Arrays.asList(toolbarItems);
        }
    }
}
