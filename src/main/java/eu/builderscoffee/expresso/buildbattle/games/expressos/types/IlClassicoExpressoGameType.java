package eu.builderscoffee.expresso.buildbattle.games.expressos.types;

import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.expresso.buildbattle.games.expressos.ExpressoGameType;
import eu.builderscoffee.expresso.buildbattle.phase.bases.EndPhase;
import eu.builderscoffee.expresso.buildbattle.phase.bases.GamePhase;
import eu.builderscoffee.expresso.buildbattle.phase.bases.LaunchingPhase;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

import static eu.builderscoffee.expresso.utils.TimeUtils.HOUR;

public class IlClassicoExpressoGameType extends ExpressoGameType {

    public IlClassicoExpressoGameType() {
        super("IlClassico");
        this.phases.add(new LaunchingPhase(30));
        this.phases.add(new GamePhase(2 * HOUR));
        this.phases.add(new EndPhase());
    }

    @Override
    public ItemStack getIcon() {
        return new ItemBuilder(Material.WHITE_DYE)
                .setName(getName())
                .build();
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("§7L'expresso des plus classique");
    }
}
