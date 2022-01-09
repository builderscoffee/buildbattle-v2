package eu.builderscoffee.expresso.buildbattle.toolbars.tools;

import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.expresso.buildbattle.toolbars.ToolbarItem;
import eu.builderscoffee.expresso.inventory.jury.JuryTeleportation;
import eu.builderscoffee.expresso.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class TeleportationItem extends ToolbarItem {

    public TeleportationItem(int slot) {
        super(slot);
    }

    @Override
    public ItemStack getItem(Player player) {
        return new ItemBuilder(Material.ENDER_EYE).setName(MessageUtils.getMessageConfig(player).getToolbar().getTeleportationItems()).build();
    }

    @Override
    public void interact(Player player, Action action) {
        JuryTeleportation.INVENTORY.open(player);
    }
}
