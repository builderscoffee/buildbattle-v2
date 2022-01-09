package eu.builderscoffee.expresso.buildbattle.phase;

import eu.builderscoffee.expresso.buildbattle.BuildBattleEngine;
import eu.builderscoffee.expresso.buildbattle.BuildBattleManager;
import lombok.Data;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.TimeUnit;

/***
 * Représente une phase/étapes d'une partie
 */
@Data
public abstract class BBPhase {

    public int time; // Temps display d'une phase
    public int currentTime; // Temps en cours de la phase
    public int defaultTime; // Temps par default de la phase
    protected String name;
    protected List<String> descriptions;
    protected ItemStack icons;
    protected TimeUnit unit;
    protected BuildBattleManager.GameState state;
    protected BuildBattleEngine engine;
    protected BukkitRunnable runnable; // Bukkit runnable de la phase

    /**
     * Retourne le nom d'une phase
     *
     * @return
     */
    public final String getName() {
        return name;
    }

    /**
     * Retourne la description d'une phase
     *
     * @return
     */
    public final List<String> getDescription() {
        return descriptions;
    }

    /**
     * Retourne l'icon de la phase
     *
     * @return
     */
    public final ItemStack getIcon() {
        return icons;
    }

    /**
     * Retourne l'unité de temps de la phase
     *
     * @return
     */
    public final TimeUnit getTimeUnit() {
        return unit;
    }

    /**
     * Retourne l'état de la phase
     *
     * @return
     */
    public final BuildBattleManager.GameState getState() {
        return state;
    }

    /**
     * Retourne le moteur chargé de la partie
     */
    public final BuildBattleEngine getEngine() {
        return engine;
    }

    /**
     * Bukkit runnable de la phase
     *
     * @return
     */
    public abstract BukkitRunnable runnable();
}
