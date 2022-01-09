package eu.builderscoffee.expresso.buildbattle.games.expressos;

import eu.builderscoffee.expresso.buildbattle.BuildBattleGameType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class ExpressoGameType extends BuildBattleGameType {

    public ExpressoGameType(String name) {
        super(name);
    }

    /**
     * Retourne l'icone de l'expresso
     * @return
     */
    public abstract ItemStack getIcon();

    /**
     * Retourne la description de l'expresso
     * @return
     */
    public abstract List<String> getDescription();
}
