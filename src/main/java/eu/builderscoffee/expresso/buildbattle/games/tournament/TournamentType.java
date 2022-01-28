package eu.builderscoffee.expresso.buildbattle.games.tournament;

import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.expresso.buildbattle.BuildBattleCategory;
import eu.builderscoffee.expresso.buildbattle.BuildBattleType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class TournamentType extends BuildBattleType {

    public TournamentType() {
        super("Manche X du tournois X");
        category = BuildBattleCategory.TOURNAMENT;
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
