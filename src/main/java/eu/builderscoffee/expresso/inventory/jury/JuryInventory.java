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

public class JuryInventory implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("network")
            .provider(new JuryInventory())
            .size(3, 9)
            .title(ChatColor.WHITE + MessageUtils.getDefaultMessageConfig().getMenu().getBaseMenuName())
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
            plotsItem[i] = ClickableItem.of(new ItemBuilder(Material.GRASS).setName(messages.getPlotItem().replace("%plot%", String.valueOf(i))).build(),
                    e -> {
                        Plot currentPlot = list.get(tempPlot);
                        PlotUtils.convertPlotCenterLoc(currentPlot.getCenter());
                        player.teleport(PlotUtils.convertPlotCenterLoc(currentPlot.getCenter()));
                    });
        }

        //Fill Black borders
        contents.fillColumn(0, blackGlasses);
        contents.fillColumn(8, blackGlasses);
        contents.fillRow(2, blackGlasses);

        contents.set(2, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).setName(messages.getPreviousItem()).build(),
                e -> INVENTORY.open(player, pagination.previous().getPage())));
        contents.set(2, 4, ClickableItem.of(new ItemBuilder(Material.BARRIER).setName(messages.getCloseItem()).build(),
                e -> INVENTORY.close(player)));
        contents.set(2, 5, ClickableItem.of(new ItemBuilder(Material.ARROW).setName(messages.getNextItem()).build(),
                e -> INVENTORY.open(player, pagination.next().getPage())));
        contents.set(2, 8, ClickableItem.empty(new ItemBuilder(Material.PAPER).setName(messages.getPageItem().replace("%page%", String.valueOf(pagination.getPage()))).build()));

        pagination.setItems(plotsItem);
        pagination.setItemsPerPage(18);

        //Fill Plots Item
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, SlotPos.of(0, 0)));

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        // Nothing to do here
    }
}
