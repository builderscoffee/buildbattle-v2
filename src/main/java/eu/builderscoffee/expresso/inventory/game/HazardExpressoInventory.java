package eu.builderscoffee.expresso.inventory.game;

import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.SmartInventory;
import eu.builderscoffee.api.bukkit.gui.content.*;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.games.expressos.engine.HazarEngine;
import eu.builderscoffee.expresso.utils.blocks.BlockData;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

public class HazardExpressoInventory implements InventoryProvider {

    @Getter
    @Setter
    public static HazarEngine hazarEngine;
    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("hazard_expresso")
            .provider(new HazardExpressoInventory(getHazarEngine()))
            .size(3, 9)
            .title(ChatColor.WHITE + "§fListe des blocks")
            .manager(ExpressoBukkit.getInventoryManager())
            .build();
    ClickableItem whiteGlasses = ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0));

    public HazardExpressoInventory(HazarEngine engine) {
        setHazarEngine(engine);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        // Get data
        val blockDataList = hazarEngine.convertBlockdata;
        val blockDataIndexes = blockDataList.keySet().stream().filter(element -> element instanceof BlockData).map(element -> (BlockData) element).collect(Collectors.toList());
        ClickableItem[] blockItems = new ClickableItem[blockDataList.size()];
        // Fill block items
        for (int i = 0; i < blockItems.length; i++) {
            int expressoIndex = i;
            blockItems[i] = ClickableItem.empty(new ItemBuilder(Material.getMaterial(blockDataIndexes.get(i).id), 1, (short) blockDataIndexes.get(i).shortId).addLoreLine("Converti en " + BlockData.getBlockDataById(i).id).build());
        }

        // Fill row
        contents.fillRow(2, whiteGlasses);
        // Prepare block convert line
        //contents.newIterator("blockconvert", SlotIterator.Type.HORIZONTAL, 1, 0);


        // Fill content Items
        contents.set(2, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).setName("§6Précédente").build(),
                e -> INVENTORY.open(player, pagination.previous().getPage())));
        contents.set(2, 4, ClickableItem.of(new ItemBuilder(Material.BARRIER).setName("§cFermer").build(),
                e -> INVENTORY.close(player)));
        contents.set(2, 5, ClickableItem.of(new ItemBuilder(Material.ARROW).setName("§6Suivant").build(),
                e -> INVENTORY.open(player, pagination.next().getPage())));
        contents.set(2, 8, ClickableItem.empty(new ItemBuilder(Material.PAPER).setName("§aPage §f" + pagination.getPage()).build()));

        // Set pages
        pagination.setItems(blockItems);
        pagination.setItemsPerPage(9);

        // Iterate pages
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, SlotPos.of(0, 0)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        /*
        SlotIterator iter = contents.iterator("blockconvert").get();
        if(iter.column() >= 7)
            return;
        iter.next();
        iter.set(ClickableItem.empty(new ItemStack(Material.WOOL, 1, (short) iter.column())));
        */
    }
}
