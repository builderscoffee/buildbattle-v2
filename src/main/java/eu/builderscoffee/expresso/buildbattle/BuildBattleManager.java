package eu.builderscoffee.expresso.buildbattle;

import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.games.expressos.ExpressoManager;
import eu.builderscoffee.expresso.buildbattle.phase.BBPhase;
import eu.builderscoffee.expresso.buildbattle.tasks.CheckStartTask;
import eu.builderscoffee.expresso.utils.Log;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BuildBattleManager implements Cloneable {

    // Instances
    @Getter
    private final ExpressoBukkit expressoBukkit;
    @Getter
    private final BuildBattle game;
    // Managers
    @Getter
    @Setter
    private ExpressoManager expressoManager;
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

    public BuildBattleManager(final BuildBattle game) {
        // Instances
        this.expressoBukkit = ExpressoBukkit.getInstance();
        this.game = game;
        // Managers
        setExpressoManager(game.getExpressoManager());
        // Définir la phase par default
        this.getGame().setGameState(GameState.WAITING);
    }

    // GAME MANAGEMENT

    /***
     * Lancer
     */
    public void startGame() {
        // La partie est prête à démarrer
        ExpressoBukkit.getBbGame().setReady(true);
        if (!ExpressoBukkit.getBbGame().isPaused()) {
            // Lancer la task de check
            ExpressoBukkit.getExecutionManager().getTasks().put("checkstart", new CheckStartTask().runTaskTimer(expressoBukkit, 0L, 20L));
        } else {
            Log.get().info("Start Clone");
            ExpressoBukkit.getBbGame().setPaused(false);
            ExpressoBukkit.getBbGame().setBbGameManager((BuildBattleManager) ExpressoBukkit.getBbGame().getBbGameManagerClone());
        }
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
        return this.getGame().getGameState() == GameState.WAITING
                && this.getGame().isReady();
    }

    /***
     * Check l'état de la partie
     */
    public boolean isRunning() {
        return this.getGame().getGameState() != GameState.WAITING;
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
        this.getGame().setGameState(this.game.getBuildBattleGameType().getCurrentPhase().getState());
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
        setCurrentTask(runnable.runTaskTimerAsynchronously(expressoBukkit, 0, 20));
    }

    /***
     * Stopper la phase en cours
     */
    public void cancelPhase() {
        Log.get().info("Phase cancel : " + this.game.getBuildBattleGameType().getCurrentPhase().getName());
        if (!this.getCurrentTask().isCancelled()) {
            System.out.println("Cancel phase task " + getCurrentTask().getTaskId());
            getCurrentTask().cancel();
        }
    }

    /***
     * Mettre en pause la partie en cours
     */
    @SneakyThrows
    public void pausePhase() {
        Log.get().info("Phase pause : " + this.game.getBuildBattleGameType().getCurrentPhase().getName());
        Log.get().info("Pause BBGame");
        game.setPaused(true);
        if (game.isPaused()) {
            Log.get().info("Clone BBGame");
            game.setBbGameManagerClone(this.clone());
        }
    }

    /***
     * Démarrer la prochaine phase
     */
    @SneakyThrows
    public void nextPhase() {
        // Get & Poll la prochaine phase
        this.game.getBuildBattleGameType().setCurrentPhase(this.game.getInstancePhases().poll());
        Log.get().info("Phase en cours : " + this.game.getBuildBattleGameType().getCurrentPhase().getName());
        // Définir le status de la prochaine phase
        this.getGame().setGameState(this.game.getBuildBattleGameType().getCurrentPhase().getState());
        // Lancer la Task de la prochaine phase
        this.startPhase(this.game.getBuildBattleGameType().getCurrentPhase().runnable());
        // Lancer le moteur de la partie s'il en existe un pour la phase en cours
        if (this.game.getBuildBattleGameType().getCurrentPhase().getEngine() != null) {
            // Lancer le moteur de la partie
            this.game.getBuildBattleGameType().getCurrentPhase().getEngine().load();
            // Enregister les évenements propre au moteur de la partie
            this.game.getBuildBattleGameType().getCurrentPhase().getEngine().registerListener();
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

    // STATE

    /***
     * État d'une partie en cours
     */
    public enum GameState {
        NONE,
        WAITING,
        LAUNCHING,
        IN_GAME,
        ENDING
    }
}

