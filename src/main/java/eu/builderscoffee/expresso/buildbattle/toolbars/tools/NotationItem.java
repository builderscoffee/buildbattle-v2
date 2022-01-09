package eu.builderscoffee.expresso.buildbattle.toolbars.tools;


import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.expresso.buildbattle.toolbars.ToolbarItem;
import eu.builderscoffee.expresso.inventory.jury.JuryNotationInventory;
import eu.builderscoffee.expresso.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class NotationItem extends ToolbarItem {

    public NotationItem(int slot) {
        super(slot);
    }

    @Override
    public ItemStack getItem(Player player) {
        return new ItemBuilder(Material.OAK_SIGN).setName(MessageUtils.getMessageConfig(player).getToolbar().getNotationsItems()).build();
    }

    @Override
    public void interact(Player player, Action action) {
        JuryNotationInventory.INVENTORY.open(player);
    }
}
