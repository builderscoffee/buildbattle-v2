package eu.builderscoffee.expresso.listeners.bukkit;

import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.events.competitor.CompetitorJoinEvent;
import eu.builderscoffee.expresso.events.competitor.CompetitorLeaveEvent;
import eu.builderscoffee.expresso.utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CompetitorListener implements Listener {

    @EventHandler
    public void onCompetitorJoin(CompetitorJoinEvent event) {
        final Player player = event.getCompetitor();
        ExpressoBukkit.getBbGame().getCompetitors().forEach(competitor -> competitor.sendMessage(MessageUtils.getMessageConfig(competitor).getGame().getCompetitorJoin().replace("%player%", player.getName()).replace("&", "§")));
    }

    @EventHandler
    public void onCompetitorLeave(CompetitorLeaveEvent event) {
        final Player player = event.getCompetitor();
        ExpressoBukkit.getBbGame().getCompetitors().forEach(competitor -> competitor.sendMessage(MessageUtils.getMessageConfig(competitor).getGame().getCompetitorLeave().replace("%player%", player.getName()).replace("&", "§")));
    }
}
