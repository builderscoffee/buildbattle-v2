package eu.builderscoffee.expresso.inventory.jury;

import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.Plot;
import eu.builderscoffee.api.bukkit.gui.ClickableItem;
import eu.builderscoffee.api.bukkit.gui.SmartInventory;
import eu.builderscoffee.api.bukkit.gui.content.InventoryContents;
import eu.builderscoffee.api.bukkit.gui.content.InventoryProvider;
import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.notation.Notation;
import eu.builderscoffee.expresso.utils.MessageUtils;
import eu.builderscoffee.expresso.utils.PlotUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class JuryNotationInventory implements InventoryProvider {
    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("Jury_Notation")
            .provider(new JuryNotationInventory())
            .size(4, 9)
            .title(ChatColor.WHITE + MessageUtils.getDefaultMessageConfig().getMenu().getNotationMenuName())
            .manager(ExpressoBukkit.getInventoryManager())
            .build();

    private int beaute, crea, ame, folkore, fun = 0;

    @Override
    public void init(Player player, InventoryContents contents) {

        Location loc = PlotUtils.convertBukkitLoc(player.getLocation());
        Plot plot = loc.getPlotAbs(); // On est sur qu'il y a un plot

        ClickableItem blackGlasses = ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
        contents.fillRow(0, blackGlasses);
        contents.fillRow(3, blackGlasses);

        // Items montrant les catégories

        contents.set(1, 2, ClickableItem.of(new ItemBuilder(Material.YELLOW_FLOWER).setName(MessageUtils.getMessageConfig(player).getMenu().getBeautyCategory()).build(),
                e -> INVENTORY.close(player)));
        contents.set(1, 3, ClickableItem.of(new ItemBuilder(Material.PAINTING).setName(MessageUtils.getMessageConfig(player).getMenu().getCreativeCategory()).build(),
                e -> INVENTORY.close(player)));
        contents.set(1, 4, ClickableItem.of(new ItemBuilder(Material.SIGN).setName(MessageUtils.getMessageConfig(player).getMenu().getAmenagementCategory()).build(),
                e -> INVENTORY.close(player)));
        contents.set(1, 5, ClickableItem.of(new ItemBuilder(Material.WRITTEN_BOOK).setName(MessageUtils.getMessageConfig(player).getMenu().getFolkloreCategory().replace("%plot%", plot.getId().toString())).build(),
                e -> INVENTORY.close(player)));
        contents.set(1, 6, ClickableItem.of(new ItemBuilder(Material.RAW_FISH).setName(MessageUtils.getMessageConfig(player).getMenu().getFunCategory()).build(),
                e -> INVENTORY.close(player)));

        // Items montrant les points en caches

        contents.set(2, 2, ClickableItem.of(new ItemBuilder(Material.PAPER).setName(MessageUtils.getMessageConfig(player).getMenu().getPointsItem().replace("%points%", String.valueOf(beaute))).build(),
                e -> {
                    changeValues(beaute, Notation.NotationType.Beauty, e.getClick());
                    INVENTORY.open(player);
                }));

        contents.set(2, 3, ClickableItem.of(new ItemBuilder(Material.PAPER).setName(MessageUtils.getMessageConfig(player).getMenu().getPointsItem().replace("%points%", String.valueOf(crea))).build(),
                e -> {
                    changeValues(crea, Notation.NotationType.Creative, e.getClick());
                    INVENTORY.open(player);
                }));
        contents.set(2, 4, ClickableItem.of(new ItemBuilder(Material.PAPER).setName(MessageUtils.getMessageConfig(player).getMenu().getPointsItem().replace("%points%", String.valueOf(ame))).build(),
                e -> {
                    changeValues(ame, Notation.NotationType.Amenagement, e.getClick());
                    INVENTORY.open(player);
                }));
        contents.set(2, 5, ClickableItem.of(new ItemBuilder(Material.PAPER).setName(MessageUtils.getMessageConfig(player).getMenu().getPointsItem().replace("%points%", String.valueOf(folkore))).build(),
                e -> {
                    changeValues(folkore, Notation.NotationType.Folklore, e.getClick());
                    INVENTORY.open(player);
                }));

        contents.set(2, 6, ClickableItem.of(new ItemBuilder(Material.PAPER).setName(MessageUtils.getMessageConfig(player).getMenu().getPointsItem().replace("%points%", String.valueOf(fun))).build(),
                e -> {
                    changeValues(fun, Notation.NotationType.Fun, e.getClick());
                    INVENTORY.open(player);
                }));

        // Valider les points du plot

        contents.set(3, 4, ClickableItem.of(new ItemBuilder(Material.RAW_FISH).setName(MessageUtils.getMessageConfig(player).getMenu().getValidedNotationItem()).build(),
                e -> {
                    Notation note = new Notation(player.getUniqueId());
                    ExpressoBukkit.getBbGame().getNotationManager().addNotationInPlot(plot, note);
                    INVENTORY.close(player);
                }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        // Nothing to do here
    }

    /***
     * Changer la valeur en cache d'une notation sur un plot
     * @param cachedValue - Valeur en cache
     * @param notationType - Type de notation
     * @param clickType - Type de click
     * @return - Retourne la valeur calculée
     */
    public int changeValues(int cachedValue, Notation.NotationType notationType, ClickType clickType) {
        switch (clickType) {
            case LEFT:
                if ((cachedValue + notationType.getNormalClickValue()) > notationType.getMaxValue()) {
                    return cachedValue;
                } else {
                    return cachedValue + notationType.getNormalClickValue();
                }
            case SHIFT_LEFT:
                if ((cachedValue + notationType.getShiftClickValue()) > notationType.getMaxValue()) {
                    return cachedValue;
                } else {
                    return cachedValue + notationType.getShiftClickValue();
                }
            case RIGHT:
                if ((cachedValue - notationType.getNormalClickValue()) < 0) {
                    return cachedValue;
                } else {
                    return cachedValue - notationType.getNormalClickValue();
                }
            case SHIFT_RIGHT:
                if ((cachedValue - notationType.getShiftClickValue()) < 0) {
                    return cachedValue;
                } else {
                    return cachedValue - notationType.getShiftClickValue();
                }
        }
        return cachedValue;
    }
}
