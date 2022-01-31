package eu.builderscoffee.expresso.buildbattle.phase;

import eu.builderscoffee.expresso.buildbattle.BuildBattleEngine;
import eu.builderscoffee.expresso.buildbattle.BuildBattleManager;
import eu.builderscoffee.expresso.buildbattle.GameState;
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
    protected GameState state;
    protected BuildBattleEngine engine;
    protected BukkitRunnable runnable; // Bukkit runnable de la phase

    /**
     * Retourne le nom d'une phase
     * @return - Le nom
     */
    public final String getName() {
        return name;
    }

    /**
     * Retourne la description d'une phase
     * @return - La liste de lore de la phase
     */
    public final List<String> getDescription() {
        return descriptions;
    }

    /**
     * Retourne l'icône de la phase
     * @return - L'itemstack
     */
    public final ItemStack getIcon() {
        return icons;
    }

    /**
     * Retourne l'unité de temps de la phase
     * @return - L'unité de temps
     */
    public final TimeUnit getTimeUnit() {
        return unit;
    }

    /**
     * Retourne l'état de la phase
     * @return - L'état de partie
     */
    public final GameState getState() {
        return state;
    }

    /**
     * Retourne le moteur chargé de la partie
     * @return - Le moteur
     */
    public final BuildBattleEngine getEngine() {
        return engine;
    }

    /**
     * Bukkit runnable de la phase
     */
    public abstract BukkitRunnable runnable();
}
