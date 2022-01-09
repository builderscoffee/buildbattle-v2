package eu.builderscoffee.expresso.buildbattle.phase.bases;


import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.BuildBattleManager;
import eu.builderscoffee.expresso.buildbattle.phase.BBPhase;
import eu.builderscoffee.expresso.buildbattle.toolbars.ToolbarManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class JuryPhase extends BBPhase {

    public JuryPhase(int defaultTime) {
        this.name = "Jury";
        this.descriptions = Arrays.asList("Notation des plots");
        this.icons = new ItemBuilder(Material.COD).setName(name).build();
        this.unit = TimeUnit.MINUTES;
        this.state = BuildBattleManager.GameState.ENDING;
        this.engine = null;
        this.defaultTime = defaultTime;
        this.time = defaultTime;
    }

    @Override
    public BukkitRunnable runnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                // Kick tous les joueurs
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.getOnlinePlayers().forEach(s -> {
                            if (Bukkit.getServer().getWhitelistedPlayers().contains(s)) {
                                return;
                            }
                            s.kickPlayer("Les plots sont en cours de notation");
                        });
                    }
                }.runTask(ExpressoBukkit.getInstance());

                // Whitelist le serveur pour permettre au jury de noter les plots
                Bukkit.getServer().setWhitelist(true);

                // Ajouter la toolbar au jury
                ExpressoBukkit.getBbGame().getJurors().forEach(jury -> ExpressoBukkit.getBbGame().getToolbarManager().addToolBar(jury, ToolbarManager.Toolbars.JURORS));
            }
        };
    }
}
