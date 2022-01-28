package eu.builderscoffee.expresso.buildbattle.tasks;

import eu.builderscoffee.expresso.ExpressoBukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckStartTask extends BukkitRunnable {

    /***
     * Tache permettant de checker si la partie est prête à démarrer
     */

    @Override
    public void run() {
        if (ExpressoBukkit.getBuildBattle() != null) {
            ExpressoBukkit.getBuildBattle().getGameManager().checkStart();
        }
    }
}
