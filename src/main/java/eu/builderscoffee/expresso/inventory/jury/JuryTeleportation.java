package eu.builderscoffee.expresso.inventory.jury;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.SmartInventory;
import eu.builderscoffee.api.bukkit.gui.content.*;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.utils.MessageUtils;
import eu.builderscoffee.expresso.utils.PlotUtils;
import lombok.val;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

;

public class JuryTeleportation implements InventoryProvider {
    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("juryTeleporte")
            .provider(new JuryTeleportation())
            .size(6, 9)
            .title(ChatColor.WHITE + MessageUtils.getDefaultMessageConfig().getMenu().getTeleportsMenu())
            .manager(ExpressoBukkit.getInventoryManager())
            .build();

    ClickableItem blackGlasses = ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();
        val messages = MessageUtils.getMessageConfig(player).getMenu();

        // Get All plots
        Set<Plot> plots = new PlotAPI().getAllPlots();
        List<Plot> list = new ArrayList<>(plots);
        int plotSize = plots.size();
        ClickableItem[] plotsItem = new ClickableItem[plotSize];

        for (int i = 0; i < plotsItem.length; i++) {
            int tempPlot = i;
            Plot currentPlot = list.get(tempPlot);
            if (ExpressoBukkit.getBbGame().getNotationManager().playerHasNote(currentPlot, player)) {
                plotsItem[i] = ClickableItem.of(new ItemBuilder(Material.GRASS).addGLow().setName(messages.getPlotItem().replace("%plot%", String.valueOf(i))).build(),
                        e -> {
                            PlotUtils.convertPlotCenterLoc(currentPlot.getCenter());
                            player.teleport(PlotUtils.convertPlotCenterLoc(currentPlot.getCenter()));
                        });
            } else {
                plotsItem[i] = ClickableItem.of(new ItemBuilder(Material.GRASS).setName(messages.getPlotItem().replace("%plot%", String.valueOf(i))).build(),
                        e -> {
                            PlotUtils.convertPlotCenterLoc(currentPlot.getCenter());
                            player.teleport(PlotUtils.convertPlotCenterLoc(currentPlot.getCenter()));
                        });
            }
        }

        //Fill Black borders
        contents.fillBorders(blackGlasses);

        contents.set(5, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).setName(messages.getPreviousItem()).build(),
                e -> INVENTORY.open(player, pagination.previous().getPage())));
        contents.set(5, 4, ClickableItem.of(new ItemBuilder(Material.BARRIER).setName(messages.getCloseItem()).build(),
                e -> INVENTORY.close(player)));
        contents.set(5, 5, ClickableItem.of(new ItemBuilder(Material.ARROW).setName(messages.getNextItem()).build(),
                e -> INVENTORY.open(player, pagination.next().getPage())));
        contents.set(5, 8, ClickableItem.empty(new ItemBuilder(Material.PAPER).setName(messages.getPageItem().replace("%page%", String.valueOf(pagination.getPage()))).build()));

        pagination.setItems(plotsItem);
        pagination.setItemsPerPage(36);

        //Fill Plots Item
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, SlotPos.of(1, 0)));

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        // Nothing to do here
    }
}
