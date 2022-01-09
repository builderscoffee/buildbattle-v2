package eu.builderscoffee.expresso.listeners.bukkit;

import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.events.team.TeamCreateEvent;
import eu.builderscoffee.expresso.events.team.TeamDisbandEvent;
import eu.builderscoffee.expresso.events.team.TeamJoinEvent;
import eu.builderscoffee.expresso.events.team.TeamLeaveEvent;
import eu.builderscoffee.expresso.utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class TeamListeners implements Listener {

    @EventHandler
    public void onTeamCreateEvent(TeamCreateEvent event) {
        // Nothing
    }

    @EventHandler
    public void onTeamDisbandEvent(TeamDisbandEvent event) {
        // Nothing
    }

    @EventHandler
    public void onTeamJoinEvent(TeamJoinEvent event) {
        List<Player> members = event.getMemberList();
        // Envoyer un message quand le joueur rejoins
        members.forEach(member -> member.sendMessage(MessageUtils.getMessageConfig(member).getTeam().getPlayerJoin().replace("%target%", event.getPlayer().getName())));
        // Ajouter le joueur au plot du leader du groupe
        ExpressoBukkit.getBbGame().getTeamManager().addMemberToAllPlot(event.getPlayer());
    }


    @EventHandler
    public void onTeamLeaveEvent(TeamLeaveEvent event) {
        List<Player> members = event.getMemberList();
        //Envoyer un message quand le joueur quitte
        members.forEach(member -> member.sendMessage(MessageUtils.getMessageConfig(member).getTeam().getPlayerQuit()));
        // Retirer le joueur au plot du leader du groupe
        ExpressoBukkit.getBbGame().getTeamManager().removeMemberFromAllPlot(event.getPlayer());
    }

}