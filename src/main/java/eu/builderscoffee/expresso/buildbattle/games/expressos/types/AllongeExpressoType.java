package eu.builderscoffee.expresso.buildbattle.games.expressos.types;

import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.expresso.buildbattle.games.expressos.ExpressoType;
import eu.builderscoffee.expresso.buildbattle.phase.bases.EndPhase;
import eu.builderscoffee.expresso.buildbattle.phase.bases.GamePhase;
import eu.builderscoffee.expresso.buildbattle.phase.bases.LaunchingPhase;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

import static eu.builderscoffee.expresso.utils.TimeUtils.HOUR;

public class AllongeExpressoType extends ExpressoType {

    public AllongeExpressoType() {
        super("Allongé");
        phases.add(new LaunchingPhase(30));
        phases.add(new GamePhase(8 * HOUR));
        phases.add(new EndPhase());
    }

    @Override
    public ItemStack getIcon() {
        return new ItemBuilder(Material.LIGHT_BLUE_DYE)
                .setName(getName())
                .build();
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("§7Expresso classique à longue durée");
    }
}
