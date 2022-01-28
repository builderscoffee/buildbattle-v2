package eu.builderscoffee.expresso.buildbattle.phase.bases;

import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.GameState;
import eu.builderscoffee.expresso.buildbattle.phase.BBPhase;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class EndPhase extends BBPhase {

    public EndPhase() {
        this.name = "Fin de la partie";
        this.descriptions = Arrays.asList("Mettre fin à la partie");
        this.icons = new ItemBuilder(Material.NETHER_WART).setName(name).build();
        this.unit = TimeUnit.MINUTES;
        this.state = GameState.ENDING;
        this.engine = null;
        this.time = defaultTime;
    }

    @Override
    public BukkitRunnable runnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                // Finir la partie
                ExpressoBukkit.getBuildBattle().getGameManager().endGame();
                //TODO Autre chose à faire avant de fermer le serveur ?
            }
        };
    }
}
