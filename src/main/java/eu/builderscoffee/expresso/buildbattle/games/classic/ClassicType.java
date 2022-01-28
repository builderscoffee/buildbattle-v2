package eu.builderscoffee.expresso.buildbattle.games.classic;

import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.expresso.buildbattle.BuildBattleCategory;
import eu.builderscoffee.expresso.buildbattle.BuildBattleType;
import eu.builderscoffee.expresso.buildbattle.phase.bases.EndPhase;
import eu.builderscoffee.expresso.buildbattle.phase.bases.GamePhase;
import eu.builderscoffee.expresso.buildbattle.phase.bases.LaunchingPhase;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

import static eu.builderscoffee.expresso.utils.TimeUtils.HOUR;

public class ClassicType extends BuildBattleType {

    public ClassicType() {
        super("BuildBattle Classic");
        category = BuildBattleCategory.CLASSIC;
        phases.add(new LaunchingPhase(30));
        phases.add(new GamePhase(2 * HOUR));
        phases.add(new EndPhase());
    }

    @Override
    public ItemStack getIcon() {
        return new ItemBuilder(Material.CRAFTING_TABLE).build();
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("");
    }
}