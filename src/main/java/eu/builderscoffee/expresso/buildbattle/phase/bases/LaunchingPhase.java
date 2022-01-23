package eu.builderscoffee.expresso.buildbattle.phase.bases;


import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.api.bukkit.utils.Title;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.BuildBattleManager;
import eu.builderscoffee.expresso.buildbattle.phase.BBPhase;
import eu.builderscoffee.expresso.utils.MessageUtils;
import eu.builderscoffee.expresso.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class LaunchingPhase extends BBPhase {

    public LaunchingPhase(int defaultTime) {
        this.name = "Démarrage";
        this.descriptions = Arrays.asList("Lancement de la partie");
        this.icons = new ItemBuilder(Material.FIREWORK_ROCKET).setName(name).build();
        this.unit = TimeUnit.SECONDS;
        this.state = BuildBattleManager.GameState.LAUNCHING;
        this.engine = null;
        this.defaultTime = defaultTime;
        this.time = defaultTime;
    }

    @Override
    public BukkitRunnable runnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                // Checker si la partie est prête à démarrer ?
                if (!ExpressoBukkit.getBbGame().isReady()) {
                    return;
                }
                // Lancer le chrono ( Title + Level )
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    player.setLevel(time);
                    if (time == 30 || time == 20 || time == 10 || time == 5) { }
                        //new Title(MessageUtils.getMessageConfig(player).getGame().getStartInTitle(), MessageUtils.getMessageConfig(player).getGame().getStartInSubTitle().replace("%time%", String.valueOf(time)), 20, 10, 20).send(player);
                }
                // Décompte du temps dans le chat
                if (time % 10 == 0 || time == 10 || time == 5 || time == 4 || time == 3 || time == 2 || time == 1) {
                    ExpressoBukkit.getInstance().getServer().getOnlinePlayers().forEach(player -> player.sendMessage(MessageUtils.getMessageConfig(player).getGame().getCompetitionBeginningIn().replace("%prefix%", MessageUtils.getDefaultMessageConfig().getPrefix()).replace("%time%", TimeUtils.getDurationString(time))));
                    Bukkit.getOnlinePlayers().forEach(player2 -> player2.playSound(player2.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 20.0f, 20.0f));
                }
                // Lancer la compétition
                if (time < 1) {
                    ExpressoBukkit.getInstance().getServer().getOnlinePlayers().forEach(player -> player.sendMessage(MessageUtils.getMessageConfig(player).getGame().getCompetitionStarting().replace("%prefix%", MessageUtils.getDefaultMessageConfig().getPrefix())));
                    Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 20.0f, 20.0f));
                    ExpressoBukkit.getBbGame().getBbGameManager().nextPhase();
                    return;
                }
                --time;
            }
        };
    }
}
