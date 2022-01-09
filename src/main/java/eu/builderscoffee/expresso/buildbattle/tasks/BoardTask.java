package eu.builderscoffee.expresso.buildbattle.tasks;

import eu.builderscoffee.expresso.ExpressoBukkit;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class BoardTask extends BukkitRunnable {

    /***
     * Tache permettant de mettre à jour le scoreboards des joueurs tout les ticks
     */

    @Override
    public void run() {
        if (Objects.nonNull(ExpressoBukkit.getBbGame()) && Objects.nonNull(ExpressoBukkit.getBbGame().getBbGameTypes())) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                ExpressoBukkit.getBbGame().getBbGameTypes().getBaseBoard().update(player);
            });
        }
    }
}
