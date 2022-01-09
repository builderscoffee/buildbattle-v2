package eu.builderscoffee.expresso.buildbattle.toolbars;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public abstract class ToolbarItem {
    @Getter
    protected int slot;

    public ToolbarItem(int slot) {
        this.slot = slot;
    }

    public abstract ItemStack getItem(Player player);

    public abstract void interact(Player player, Action action);


}
