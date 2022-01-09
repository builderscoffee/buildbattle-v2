package eu.builderscoffee.expresso.buildbattle;

import eu.builderscoffee.expresso.buildbattle.phase.BBPhase;
import lombok.Data;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

@Data
public abstract class BuildBattleGameType {

    protected final String name;
    public BukkitRunnable currentRunnable;
    public BBPhase currentPhase;
    protected Deque<BBPhase> phases = new LinkedBlockingDeque<>();

    public BuildBattleGameType(String name) {
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

}
