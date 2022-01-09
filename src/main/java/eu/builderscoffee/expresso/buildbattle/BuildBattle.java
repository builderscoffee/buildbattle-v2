package eu.builderscoffee.expresso.buildbattle;

import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.games.classic.ClassicGameType;
import eu.builderscoffee.expresso.buildbattle.games.expressos.ExpressoGameType;
import eu.builderscoffee.expresso.buildbattle.games.expressos.ExpressoManager;
import eu.builderscoffee.expresso.buildbattle.games.tournament.TournamentGameType;
import eu.builderscoffee.expresso.buildbattle.notation.NotationManager;
import eu.builderscoffee.expresso.buildbattle.phase.BBPhase;
import eu.builderscoffee.expresso.buildbattle.teams.TeamManager;
import eu.builderscoffee.expresso.buildbattle.toolbars.ToolbarManager;
import eu.builderscoffee.expresso.events.competitor.CompetitorJoinEvent;
import eu.builderscoffee.expresso.events.competitor.CompetitorLeaveEvent;
import lombok.Data;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Data
@Accessors(chain = true)
public class BuildBattle {

    public TeamManager teamManager;
    // Etat de la partie
    public BuildBattleManager.GameState gameState = BuildBattleManager.GameState.WAITING;
    // Liste des compétiteurs
    private List<Player> competitors = new ArrayList<>();
    // Liste des jurys
    private List<Player> jurors = new ArrayList<>();
    // Type d'instance ( Expresso , BB , tournois )
    private BuildBattleInstanceType bbGameTypes;
    private BuildBattleGameType buildBattleGameType;
    private ExpressoGameType expressoGameType = null;
    private ClassicGameType classicGameType = null;
    private TournamentGameType tournamentGameType = null;
    // Manager
    private BuildBattleManager bbGameManager;
    private Object bbGameManagerClone;
    private ExpressoManager expressoManager;
    private NotationManager notationManager;
    private ToolbarManager toolbarManager;
    // Phase de la partie
    private Deque<BBPhase> instancePhases;
    // Instance Check
    private boolean isReady = false;
    private boolean isPaused = false;


    /***
     * Créer une instance d'une BBGame
     */

    public BuildBattle(BuildBattleInstanceType type) {
        this.bbGameTypes = type;
        // Définir l'instance du BuildBattleManager
        setBbGameManager(new BuildBattleManager(this));
        // Setup les managers
        teamManager = new TeamManager();
        notationManager = new NotationManager();
        toolbarManager = new ToolbarManager();
        // Setup game managers
        expressoManager = new ExpressoManager(this);
    }

    // CONFIGURE GAME TYPE

    /***
     * Définir ou redéfinir l'expresso de la partie en cours
     * @param type - Type de BuildBattleInstanceType
     */
    public final void configureGameType(BuildBattleInstanceType type) {
        switch (type) {
            case NONE:
            case CLASSIC:
                setBuildBattleGameType(classicGameType);
                break;
            case EXPRESSO:
                setBuildBattleGameType(expressoGameType);
                break;
            case TOURNAMENT:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }


    // COMPETITOR

    /**
     * Ajouter un joueur à la liste des participants
     * @param player - Joueur
     */
    public void addCompetitor(Player player) {
        competitors.add(player);
        ExpressoBukkit.getInstance().getServer().getPluginManager().callEvent(new CompetitorJoinEvent(player));
    }

    /**
     * Retirer un joueur de la liste des participants
     * @param player - Joueur
     */
    public void removeCompetitor(Player player) {
        competitors.remove(player);
        ExpressoBukkit.getInstance().getServer().getPluginManager().callEvent(new CompetitorLeaveEvent(player));
    }

    // JURY

    /**
     * Ajouter un joueur à la liste des jury
     * @param player - Joueur
     */
    public void addJury(Player player) {
        jurors.add(player);
    }

    /**
     * Retirer un joueur de la liste des jury
     * @param player - Joueur
     */
    public void removeJury(Player player) {
        jurors.remove(player);
    }

    // OTHER STUFF

    /***
     * Broadcast un message dans la partie
     * @param message - Message à broadcast
     */
    public void broadcast(final String message) {
        Bukkit.broadcastMessage(message);
    }

}
