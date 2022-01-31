package eu.builderscoffee.expresso.buildbattle.toolbars;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

/***
 * Représente un item d'une barre à outils
 */
public abstract class ToolbarItem {

    /* Slot de l'outil */
    @Getter
    protected int slot;
    public ToolbarItem(int slot) {
        this.slot = slot;
    }

    /* Retourne l'itemstack de l'outil */
    public abstract ItemStack getItem(Player player);

    /* Interaction avec l'outil */
    public abstract void interact(Player player, Action action);


}
