package eu.builderscoffee.expresso.buildbattle;

import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.notation.NotationManager;
import eu.builderscoffee.expresso.buildbattle.phase.BBPhase;
import eu.builderscoffee.expresso.buildbattle.teams.TeamManager;
import eu.builderscoffee.expresso.buildbattle.toolbars.ToolbarManager;
import eu.builderscoffee.expresso.events.competitor.CompetitorJoinEvent;
import eu.builderscoffee.expresso.events.competitor.CompetitorLeaveEvent;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.val;
import org.bukkit.entity.Player;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Data
@Accessors(chain = true)
public class BuildBattle {

    // Managers
    private final TeamManager teamManager = new TeamManager();
    private BuildBattleManager gameManager = new BuildBattleManager();
    private NotationManager notationManager = new NotationManager();
    private ToolbarManager toolbarManager = new ToolbarManager();

    // Player lists
    private final ActionArrayList<Player> competitors = new ActionArrayList<>();
    private final List<Player> jurors = new ArrayList<>();

    // BuildBattle
    private GameState state = GameState.WAITING;
    private BuildBattleType type;
    private List<BuildBattleType> types = new ArrayList<>();
    private Deque<BBPhase> instancePhases;
    private boolean isReady = false;
    private boolean isPaused = false;

    public BuildBattle() {
        // Add listeners to the competitors list
        competitors.setOnAdd(player -> ExpressoBukkit.getInstance().getServer().getPluginManager().callEvent(new CompetitorJoinEvent(player)));
        competitors.setOnRemove(player -> ExpressoBukkit.getInstance().getServer().getPluginManager().callEvent(new CompetitorLeaveEvent(player)));

        // Get all BuildBattleTypes
        val reflections = new Reflections(BuildBattleType.class.getPackage().getName());
        reflections.getSubTypesOf(BuildBattleType.class).stream()
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .forEach(clazz -> {
                    try {
                        val buildBattleType = clazz.getConstructor().newInstance();
                        if(!buildBattleType.getCategory().equals(BuildBattleCategory.NONE))
                            types.add(buildBattleType);
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
    }
}
