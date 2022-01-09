package eu.builderscoffee.expresso.buildbattle.games.expressos.phases;

import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.games.expressos.engine.HazarEngine;
import eu.builderscoffee.expresso.buildbattle.phase.bases.GamePhase;
import org.bukkit.scheduler.BukkitRunnable;

public class HazarPhase extends GamePhase {

    public static HazarEngine hazarEngine;

    public HazarPhase(int defaultTime) {
        super(defaultTime);
        this.name = "Game Hazard";
        // Enregistrer l'engine de la partie
        hazarEngine = new HazarEngine(ExpressoBukkit.getInstance());
        engine = hazarEngine;
    }

    @Override
    public BukkitRunnable runnable() {
        return super.runnable();
    }

}
