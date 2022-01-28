package eu.builderscoffee.expresso.buildbattle;

import eu.builderscoffee.expresso.buildbattle.phase.BBPhase;
import lombok.Data;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

@Data
public abstract class BuildBattleType {

    protected final String name;
    protected BuildBattleCategory category = BuildBattleCategory.NONE;
    protected BukkitRunnable currentRunnable;
    protected BBPhase currentPhase;
    protected Deque<BBPhase> phases = new LinkedBlockingDeque<>();

    public BuildBattleType(String name) {
        this.name = name;
    }

    /**
     * Retourne le nom du build-battle
     */
    public final String getName() {
        return name;
    }


    /**
     * Retournes les phases du build-battle
     */
    public final Deque<BBPhase> getPhases() {
        return phases;
    }

    public abstract ItemStack getIcon();

    public abstract List<String> getDescription();
}
