package eu.builderscoffee.expresso.buildbattle.teams;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.plot.Plot;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.configuration.SettingsConfiguration;
import eu.builderscoffee.expresso.events.team.TeamCreateEvent;
import eu.builderscoffee.expresso.events.team.TeamDisbandEvent;
import eu.builderscoffee.expresso.events.team.TeamJoinEvent;
import eu.builderscoffee.expresso.events.team.TeamLeaveEvent;
import eu.builderscoffee.expresso.utils.MessageUtils;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamManager {

    public List<Team> teams;
    public List<Invitation> invitations;
    // Configuration
    public SettingsConfiguration settings = ExpressoBukkit.getSettings();
    // Instance
    private final PlotAPI plotAPI = new PlotAPI();

    public TeamManager() {
        // Init les variables
        teams = new ArrayList<>();
        invitations = new ArrayList<>();
    }

    /***
     * Retourne la team du joueur
     * @param player - Joueur
     */
    public Team getPlayerTeam(Player player) {
        return teams.stream()
                .filter(team -> team.getMembers().contains(player))
                .findFirst().orElse(null);
    }

    /***
     * Retourne si la team à atteint sont maximun de membres
     * @param player - Joueur
     */
    public boolean IsMembersReachLimit(Player player) {
        Team team = getPlayerTeam(player);
        return team.members.size() <= team.maxPlayers;
    }

    /***
     * Retourne si la target à la même team que le player
     * @param player - Joueur
     * @param target - Joueur
     */
    public boolean IsSameTeam(Player player, Player target) {
        Team team = getPlayerTeam(player);
        return team.members.contains(target);
    }

    /***
     * Retourne si player est le leader de la team
     * @param player - Joueur
     */
    public boolean IsTeamLeader(Player player) {
        Team team = getPlayerTeam(player);
        return team.getLeader().equals(player);
    }

    /***
     * Retourne si le joueur à une team
     * @param player - Joueur
     */
    public boolean AsNoTeam(Player player) {
        return teams.stream().flatMap(team -> team.getMembers().stream()).anyMatch(member -> member.getName().equals(player.getName()));
    }

    /***
     * Voir la team du sender
     * @param player - Joueur
     */
    public void viewTeam(Player player) {
        viewTargetTeam(player, player);
    }

    /***
     * Voir la team de la target
     * @param player - Joueur
     */
    public void viewTargetTeam(Player player, Player target) {
        Team team = getPlayerTeam(target);
        val messages = MessageUtils.getMessageConfig(player);
        if (team != null) {
            player.sendMessage(messages.getTeam().getInfoHeader());
            player.sendMessage(messages.getTeam().getInfoLeader() + team.leader.getName());
            StringJoiner joiner = new StringJoiner(", ");
            for (Player m : team.getMembers()) {
                if (team.getLeader() != null || !m.getName().equals(team.getLeader().getName())) {
                    String displayName = m.getName();
                    joiner.add(displayName);
                }
            }
            player.sendMessage(messages.getTeam().getInfoMembers() + joiner);
        } else if (!Objects.deepEquals(player, target)) {
            player.sendMessage(messages.getTeam().getNoTeam());
        } else {
            player.sendMessage(messages.getTeam().getInfoNoTeam().replace("%target%", target.getName()));
        }
    }

    /***
     * Ajouter un joueur à une team
     * @param player - Joueur
     * @param target - Joueur
     */
    public void addPlayerToTeam(Player player, Player target) {
        Team team = getPlayerTeam(player);
        val messages = MessageUtils.getMessageConfig(player);

        if (!AsNoTeam(target)) {
            team.members.add(target);
            TeamJoinEvent joinEvent = new TeamJoinEvent(target, team.members); // Fire TeamJoin Event
            Bukkit.getPluginManager().callEvent(joinEvent); // Call event
        } else if (IsSameTeam(player, target)) {
            player.sendMessage(messages.getTeam().getAlreadyInTeam().replace("%target%", target.getName()));
        } else if (IsMembersReachLimit(player)) {
            player.sendMessage(messages.getTeam().getLimitReached().replace("%limit%", String.valueOf(settings.getTeam_maxplayer())));
        }
    }

    /***
     * Retirer un joueur d'une team
     * @param player - Le joueur à retirer
     */
    public void removePlayerFromTeam(Player player) {
        if (getPlayerTeam(player) != null) {
            if (!IsTeamLeader(player)) {
                Team team = getPlayerTeam(player);
                team.members.remove(player);
                TeamLeaveEvent leaveEvent = new TeamLeaveEvent(player, team.members); // Fire TeamLeave Event
                Bukkit.getPluginManager().callEvent(leaveEvent);
            } else {
                player.sendMessage(MessageUtils.getMessageConfig(player).getTeam().getLeaderCannotLeave());
            }
        }
    }

    /***
     * Enregistrer une team
     * @param player - Membre du groupe
     */
    public void registerTeam(Player player) {
        val messages = MessageUtils.getMessageConfig(player);
        if (!AsNoTeam(player)) {
            Team _team = new Team(player.getName(), player.getName(), settings.getTeam_maxplayer(), player, new ArrayList<>());
            _team.members.add(player); // Ajouter le leader à la liste des membres
            teams.add(_team);
            player.sendMessage(messages.getTeam().getCreateTeam());
            TeamCreateEvent createEvent = new TeamCreateEvent(player); // Fire TeamCreate Event
            Bukkit.getPluginManager().callEvent(createEvent); // Call event
        } else {
            player.sendMessage(messages.getTeam().getAlreadyCreated());
        }
    }

    /***
     * Supprimer une team
     * @param player - Leader du groupe
     */
    public void unregisterTeam(Player player) {
        if (getPlayerTeam(player) != null && IsTeamLeader(player)) {
            Team team = getPlayerTeam(player);
            TeamDisbandEvent disbandEvent = new TeamDisbandEvent(player, team.getMembers()); // Fire TeamDisband Event
            Bukkit.getPluginManager().callEvent(disbandEvent); // Call event
            teams.remove(team);
            player.sendMessage(MessageUtils.getMessageConfig(player).getTeam().getDisband());
        }
    }

    // Invitation Part

    /***
     * Envoyer une invitation à un joueur
     * @param player - Joueur
     * @param target - Joueur
     */
    public void SendInvitation(Player player, Player target) {
        val messages = MessageUtils.getMessageConfig(player);
        // Check si le sender et la target ne sont pas les mêmes joueurs
        if (!Objects.deepEquals(player, target)) {
            Invitation invitation = new Invitation(player, target);
            // Check si l'invitation à deja été créé
            invitations.add(invitation);
            player.sendMessage(messages.getInvitation().getSend().replace("%target%", target.getName()));
            //TODO change fancy message systems
            //new FancyMessage(messages.getInvitation().getReceiveTarget().replace("%sender%", player.getName())).then(messages.getInvitation().getReceiveAcceptance()).command("/group invite " + player.getName() + " accept").then(" ou ").then(messages.getInvitation().getReceiveDenyance()).command("/group invite " + player.getName() + " deny").send(target);
        } else {
            player.sendMessage(messages.getInvitation().getNotInviteYourself());
        }
    }

    /***
     * Accepter l'invitation d'un joueur
     * @param receiver - Joueur qui reçois l'invitation
     * @param sender - Joueur qui envois l'invitation
     */
    public void AcceptInvitation(Player receiver, Player sender) {
        Invitation invitation = getInvitation(sender, receiver);
        if (invitation.getTarget() != null) {
            if (!AsNoTeam(sender)) {
                registerTeam(sender);
            }
            addPlayerToTeam(sender, receiver);
            invitations.remove(invitation); // Clean l'invitation accepter
        }
    }

    /***
     * Refuser l'invitation d'un joueur
     * @param receiver - Joueur qui reçoit l'invitation
     * @param sender - Joueur qui envoi l'invitation
     */
    public void DenyInvitation(Player receiver, Player sender) {
        val messagesReceiver = MessageUtils.getMessageConfig(receiver);
        val messagesSender = MessageUtils.getMessageConfig(sender);
        Invitation invitation = getInvitation(sender, receiver);
        if (invitation != null) {
            if (invitation.getTarget() != null)
                receiver.sendMessage(messagesReceiver.getInvitation().getDenyTarget().replace("%sender%", sender.getName()));
            sender.sendMessage(messagesSender.getInvitation().getDenySender().replace("%target%", receiver.getName()));
            invitations.remove(invitation); // Clean l'invitation accepter
        } else {
            receiver.sendMessage(messagesReceiver.getInvitation().getNotAvaliable().replace("%sender%", sender.getName()));
        }
    }

    /***
     * Retourne l'invitation envoyer à un joueur
     * @param sender - Joueur
     * @param receiver - Joueur
     * @return
     */
    public Invitation getInvitation(Player sender, Player receiver) {
        return invitations.stream()
                .filter(invitation -> invitation.getSender() == sender && invitation.getTarget() == receiver)
                .findFirst()
                .get();
    }

    /***
     * Nettoyer la liste des invitations en cours
     */
    public void CleanInvitations() {
        invitations.clear();
    }


    // Plot part

    /***
     * Ajouté tous les membres du groupe aux plots du leader
     * @param team - L'object Team
     */
    public void addAllMembersToPlot(Team team, Plot plot) {
        team.getMembers().forEach(member -> {
            if (member != team.getLeader()) {
                plot.addTrusted(member.getUniqueId());
            }
        });
    }


    /***
     * Ajouter un joueur aux plots de la team
     * @param player - Joueur à ajouter
     */
    public void addMemberToAllPlot(Player player) {
        Team team = ExpressoBukkit.getBbGame().getTeamManager().getPlayerTeam(player);
        Set<Plot> plots = new PlotAPI().getPlayerPlots(Objects.requireNonNull(plotAPI.wrapPlayer(team.getLeader().getUniqueId())));
        plots.forEach(plot -> {
            plot.addTrusted(player.getUniqueId());
        });
    }

    /***
     * Retiré un joueur de tous les plots de la team
     * @param player - Joueur à retirer
     */
    public void removeMemberFromAllPlot(Player player) {
        Team team = ExpressoBukkit.getBbGame().getTeamManager().getPlayerTeam(player);
        Set<Plot> plots = new PlotAPI().getPlayerPlots(Objects.requireNonNull(plotAPI.wrapPlayer(team.getLeader().getUniqueId())));
        plots.forEach(plot -> {
            plot.removeTrusted(player.getUniqueId());
        });
    }

}
