package eu.builderscoffee.expresso.buildbattle.phase.bases;


import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.api.bukkit.utils.Title;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.BuildBattle;
import eu.builderscoffee.expresso.buildbattle.GameState;
import eu.builderscoffee.expresso.buildbattle.phase.BBPhase;
import eu.builderscoffee.expresso.utils.Log;
import eu.builderscoffee.expresso.utils.MessageUtils;
import eu.builderscoffee.expresso.utils.TimeUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static eu.builderscoffee.expresso.utils.TimeUtils.HOUR;
import static eu.builderscoffee.expresso.utils.TimeUtils.MIN;
import static org.bukkit.Bukkit.getOnlinePlayers;
import static org.bukkit.GameMode.CREATIVE;

public class GamePhase extends BBPhase {

    private int[] titleTime;
    private int[] bcTime;
    @Getter
    @Setter
    private BuildBattle game;

    public GamePhase(int defaultTime) {
        this.name = "En jeux";
        this.descriptions = Collections.singletonList("Représente une partie en cours");
        this.icons = new ItemBuilder(Material.CLOCK).setName(name).build();
        this.unit = TimeUnit.MINUTES;
        this.state = GameState.IN_GAME;
        this.engine = null;
        this.defaultTime = defaultTime;
        this.time = defaultTime;
    }

    @Override
    public BukkitRunnable runnable() {
        if (Objects.isNull(this.bcTime))
            this.bcTime = addTimeEach(new int[]{time - 10 * MIN, time - 30 * MIN, time / 2}, HOUR);
        if (Objects.isNull(this.titleTime))
            this.titleTime = new int[]{time - 1, time - 2, time - 3, time - 4, time - 5, time - 10, time - 20, time - 30, time - MIN};
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (currentTime == 0) {
                    // Démarrer la game en dévoilant le thème
                    // et définir la game mode en créatif pour chaques
                    // joueurs

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            getOnlinePlayers().forEach(p -> {
                                new Title(MessageUtils.getMessageConfig(p).getGame().getThemesTitle(), ExpressoBukkit.getBuildBattle().getGameManager().getTheme(), 20, 20, 20).send(p);
                                p.setGameMode(CREATIVE);
                            });
                        }
                    }.runTask(ExpressoBukkit.getInstance());

                    ExpressoBukkit.getInstance().getServer().getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.sendMessage(MessageUtils.getMessageConfig(onlinePlayer).getGame().getPlotAuto().replace("%prefix%", MessageUtils.getDefaultMessageConfig().getPrefix())));

                }
                // Log les minutes de jeu en console
                if (currentTime % 60 == 0) Log.get().info(" " + currentTime / 60 + " minutes de jeux");

                // Tout les X temps envoyé un broadcast pour le temps de jeux restant
                Arrays.stream(bcTime).filter(i -> i == currentTime).forEach(i -> ExpressoBukkit.getInstance().getServer().getOnlinePlayers().forEach(player -> player.sendMessage(MessageUtils.getMessageConfig(player).getGame().getRemainingGames().replace("%prefix%", MessageUtils.getDefaultMessageConfig().getPrefix()).replace("%time%", TimeUtils.getDurationString(time - currentTime)))));

                // Tout les X temps envoyé un title pour la dernière minute restante
                Arrays.stream(titleTime).filter(i -> i == currentTime).forEach(i -> getOnlinePlayers().forEach(p -> {
                    new Title(MessageUtils.getMessageConfig(p).getGame().getRemainingTime(), TimeUtils.getDurationString(time - currentTime), 20, 5, 20).send(p);
                }));

                // Passer à l'étape suivante si le temps est écoulé
                if (currentTime >= time) ExpressoBukkit.getBuildBattle().getGameManager().nextPhase();

                ++currentTime;
            }
        };
    }

    protected int[] addTimeEach(int[] array, int seconds) {
        int[] newArray = new int[(int) (array.length + Math.floor(time / seconds) - 1)];

        IntStream.range(0, array.length).forEach(i -> newArray[i] = array[i]);

        for (int i = 1; i < time / seconds; i++)
            newArray[array.length + i - 1] = seconds * i;

        return newArray;
    }
}
