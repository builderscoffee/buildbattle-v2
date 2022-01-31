package eu.builderscoffee.expresso.buildbattle;

import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.phase.BBPhase;
import eu.builderscoffee.expresso.buildbattle.tasks.CheckStartTask;
import eu.builderscoffee.expresso.utils.Log;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BuildBattleManager {

    // Engines
    @Getter
    @Setter
    private BuildBattleEngine gameEngine;
    // Tasks
    @Getter
    @Setter
    private BukkitTask currentTask;
    // Others
    @Getter
    @Setter
    private AtomicInteger phases;
    @Getter
    @Setter
    private BBPhase bbPhase;
    @Getter
    @Setter
    private String theme;

    /***
     * Lancer
     */
    public void startGame() {
        // La partie est prête à démarrer
        ExpressoBukkit.getBuildBattle().setReady(true);
        ExpressoBukkit.getExecutionManager().getTasks().put("checkstart", new CheckStartTask().runTaskTimer(ExpressoBukkit.getInstance(), 0L, 20L));
        //TODO Pause function deprecated
   /*
        if (!ExpressoBukkit.getBuildBattle().isPaused()) {
            // Lancer la task de check
            ExpressoBukkit.getExecutionManager().getTasks().put("checkstart", new CheckStartTask().runTaskTimer(ExpressoBukkit.getInstance(), 0L, 20L));
        } else {
            Log.get().info("Start Clone");
            ExpressoBukkit.getBuildBattle().setPaused(false);
            //ExpressoBukkit.getBuildBattle().setBbGameManager((BuildBattleManager) ExpressoBukkit.getBuildBattle().getBbGameManagerClone());
        }
     */
    }


    /***
     * Checker si la partie peux démarrer
     */
    @SneakyThrows
    public void checkStart() {
        if (this.shouldStart()) {
            // Lancer la prochaine phase
            this.nextPhase();
        }
    }

    /***
     * Retourne si la partie est prète à démarrer
     */
    public boolean shouldStart() {
        return ExpressoBukkit.getBuildBattle().getState() == GameState.WAITING
                && ExpressoBukkit.getBuildBattle().isReady();
    }

    /***
     * Check l'état de la partie
     */
    public boolean isRunning() {
        return ExpressoBukkit.getBuildBattle().getState() != GameState.WAITING;
    }

    /***
     * Annuler la partie en cours et
     * reset le système
     */
    public void cancelGame() {
        // On stoppe la phase en cours si ce n'est deja pas fait
        this.cancelPhase();
        // On stopper toutes les task
        ExpressoBukkit.getExecutionManager().cancelAllTasks();
    }

    /***
     * Mettre en pause la partie
     */
    public void PauseGame() {
        //Mettre en pause la phase
        pausePhase();
        // Cancel la phase en cours
        cancelPhase();
    }

    /***
     * Stopper la partie en cours
     */
    public void endGame() {
        // Définir l'état de fin de la partie
        ExpressoBukkit.getBuildBattle().setState(ExpressoBukkit.getBuildBattle().getType().getCurrentPhase().getState());
        // Couper la phase en cours
        this.cancelPhase();
        // Désactiver les plugins de build
        this.disablePlugins();
    }

    // PHASE SYSTEM

    /***
     * Démarrer une nouvelle phase
     * @param runnable - La task bukkit
     */
    public void startPhase(BukkitRunnable runnable) {
        if (getCurrentTask() != null) {
            getCurrentTask().cancel();
        }
        setCurrentTask(runnable.runTaskTimerAsynchronously(ExpressoBukkit.getInstance(), 0, 20));
    }

    /***
     * Stopper la phase en cours
     */
    public void cancelPhase() {
        Log.get().info("Phase cancel : " + ExpressoBukkit.getBuildBattle().getType().getCurrentPhase().getName());
        if (!this.getCurrentTask().isCancelled()) {
            System.out.println("Cancel phase task " + getCurrentTask().getTaskId());
            getCurrentTask().cancel();
        }
    }

    /***
     * Mettre en pause la partie en cours
     * TODO Redefine pause function
     */
    @SneakyThrows
    @Deprecated
    public void pausePhase() {
        Log.get().info("Phase pause : " + ExpressoBukkit.getBuildBattle().getType().getCurrentPhase().getName());
        Log.get().info("Pause BBGame");
        ExpressoBukkit.getBuildBattle().setPaused(true);
        if (ExpressoBukkit.getBuildBattle().isPaused()) {
            Log.get().info("Clone BBGame");
        }
    }

    /***
     * Démarrer la prochaine phase
     */
    @SneakyThrows
    public void nextPhase() {
        val buildbattle = ExpressoBukkit.getBuildBattle();
        // Get & Poll la prochaine phase
        buildbattle.getType().setCurrentPhase(buildbattle.getInstancePhases().poll());
        Log.get().info("Phase en cours : " + buildbattle.getType().getCurrentPhase().getName());
        // Définir le status de la prochaine phase
        buildbattle.setState(buildbattle.getType().getCurrentPhase().getState());
        // Lancer la Task de la prochaine phase
        this.startPhase(buildbattle.getType().getCurrentPhase().runnable());
        // Lancer le moteur de la partie s'il en existe un pour la phase en cours
        if (buildbattle.getType().getCurrentPhase().getEngine() != null) {
            // Lancer le moteur de la partie
            buildbattle.getType().getCurrentPhase().getEngine().load();
            // Enregister les événements propre au moteur de la partie
            buildbattle.getType().getCurrentPhase().getEngine().registerListener();
        }
    }

    // OTHER STUFF

    /***
     * Désactiver les plugins non nécessaires après la phase IN-GAME
     */
    public void disablePlugins() {
        PluginManager pm = ExpressoBukkit.getInstance().getServer().getPluginManager();
        List<String> pluginToDisable = ExpressoBukkit.getSettings().getGame_plugin_end_disable();
        pluginToDisable.forEach(s -> {
            if (pm.getPlugin(s) != null) {
                pm.disablePlugin(pm.getPlugin(s));
            }
        });
    }
}

