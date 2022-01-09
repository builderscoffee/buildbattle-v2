package eu.builderscoffee.expresso.listeners.bukkit;

import com.plotsquared.core.events.PlayerClaimPlotEvent;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.teams.Team;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PlotListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerClaim(PlayerClaimPlotEvent event) {
        Team team = ExpressoBukkit.getBbGame().getTeamManager().getPlayerTeam(event.getPlayer());
        ExpressoBukkit.getBbGame().getTeamManager().addAllMembersToPlot(team, event.getPlot());
    }
}
