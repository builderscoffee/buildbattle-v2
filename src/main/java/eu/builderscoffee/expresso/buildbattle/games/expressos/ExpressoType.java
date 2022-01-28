package eu.builderscoffee.expresso.buildbattle.games.expressos;

import eu.builderscoffee.expresso.buildbattle.BuildBattleCategory;
import eu.builderscoffee.expresso.buildbattle.BuildBattleType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class ExpressoType extends BuildBattleType {

    public ExpressoType(String name) {
        super(name);
        category = BuildBattleCategory.EXPRESSO;
    }
}
