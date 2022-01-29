package eu.builderscoffee.expresso.buildbattle.toolbars.tools;

import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.toolbars.ToolbarItem;
import eu.builderscoffee.expresso.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

/***
 * Outils @JoinItem
 * Permet de rejoindre la partie en cours
 */
public class JoinItem extends ToolbarItem {

    public JoinItem(int slot) {
        super(slot);
    }

    @Override
    public ItemStack getItem(Player player) {
        return new ItemBuilder(Material.BIRCH_DOOR).setName(MessageUtils.getMessageConfig(player).getToolbar().getPlotItems()).build();
    }

    @Override
    public void interact(Player player, Action action) {
        // Ajouter le joueur à la liste des participants
        if (!ExpressoBukkit.getBuildBattle().getCompetitors().contains(player)) {
            ExpressoBukkit.getBuildBattle().getCompetitors().add(player);
            // Faire executer la commande aux joueurs pour l'ajouter dans un plot
            Bukkit.getServer().getPlayer(player.getName()).performCommand("/plot auto");
        }
    }
}
