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
import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

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
    public Team getPlayerTeam(@NonNull Player player) {
        return teams.stream()
                .filter(team -> team.getMembers().contains(player))
                .findFirst().orElse(null);
    }

    /***
     * Retourne si la team à atteint sont maximum de membres
     * @param player - Joueur
     */
    public boolean isMembersReachLimit(@NonNull Player player) {
        Team team = getPlayerTeam(player);
        if(Objects.isNull(team))
            return false;
        return team.members.size() <= team.maxPlayers;
    }

    /***
     * Retourne si la target à la même team que le player
     * @param player - Joueur
     * @param target - Joueur
     */
    public boolean isSameTeam(@NonNull Player player, @NonNull Player target) {
        val team = getPlayerTeam(player);
        if(Objects.isNull(team))
            return false;
        return team.members.contains(target);
    }

    /***
     * Retourne si player est le leader de la team
     * @param player - Joueur
     */
    public boolean isTeamLeader(@NonNull Player player) {
        val team = getPlayerTeam(player);
        if(Objects.isNull(team))
            return false;
        return team.getLeader().equals(player);
    }

    /***
     * Retourne si le joueur à une team
     * @param player - Joueur
     */
    public boolean hasTeam(@NonNull Player player) {
        return teams.stream()
                .flatMap(team -> team.getMembers().stream())
                .anyMatch(member -> member.getName().equals(player.getName()));
    }

    /***
     * Voir la team du sender
     * @param player - Joueur
     */
    public void viewTeam(@NonNull Player player) {
        viewTargetTeam(player, player);
    }


    /***
     * Voir la team de la target
     * @param player - Joueur
     */
    public void viewTargetTeam(@NonNull Player player, @NonNull Player target) {
        val team = getPlayerTeam(target);
        val messages = MessageUtils.getMessageConfig(player);
        if (Objects.nonNull(team)) {
            player.sendMessage(messages.getTeam().getInfoHeader());
            player.sendMessage(messages.getTeam().getInfoLeader() + team.leader.getName());
            player.sendMessage(messages.getTeam().getInfoMembers() + String.join(", ", team.getMembers().stream()
                    .filter(m -> Objects.nonNull(team.getLeader()) || !m.getName().equals(team.getLeader().getName()))
                    .map(Player::getName)
                    .collect(Collectors.toList())));
        } else if (!player.equals(target)) {
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
    public void addPlayerToTeam(@NonNull Player player, @NonNull Player target) {
        val team = getPlayerTeam(player);
        val messages = MessageUtils.getMessageConfig(player);

        if (!hasTeam(target)) {
            team.members.add(target);
            Bukkit.getPluginManager().callEvent(new TeamJoinEvent(target, team.members)); // Call event
        } else if (isSameTeam(player, target)) {
            player.sendMessage(messages.getTeam().getAlreadyInTeam().replace("%target%", target.getName()));
        } else if (isMembersReachLimit(player)) {
            player.sendMessage(messages.getTeam().getLimitReached().replace("%limit%", String.valueOf(settings.getTeam_maxplayer())));
        }
    }

    /***
     * Retirer un joueur d'une team
     * @param player - Le joueur à retirer
     */
    public void removePlayerFromTeam(@NonNull Player player) {
        if(Objects.isNull(getPlayerTeam(player)))
            return;

        if (!isTeamLeader(player)) {
            val team = getPlayerTeam(player);
            team.members.remove(player);
            Bukkit.getPluginManager().callEvent(new TeamLeaveEvent(player, team.members));
        } else {
            player.sendMessage(MessageUtils.getMessageConfig(player).getTeam().getLeaderCannotLeave());
        }
    }

    /***
     * Enregistrer une team
     * @param player - Membre du groupe
     */
    public void registerTeam(@NonNull Player player) {
        val messages = MessageUtils.getMessageConfig(player);
        if (!hasTeam(player)) {
            val team = new Team(player.getName(), player.getName(), settings.getTeam_maxplayer(), player, new ArrayList<>());
            team.members.add(player); // Ajouter le leader à la liste des membres
            teams.add(team);
            player.sendMessage(messages.getTeam().getCreateTeam());
            Bukkit.getPluginManager().callEvent(new TeamCreateEvent(player)); // Call event
        } else {
            player.sendMessage(messages.getTeam().getAlreadyCreated());
        }
    }

    /***
     * Supprimer une team
     * @param player - Leader du groupe
     */
    public void unregisterTeam(@NonNull Player player) {
        if (Objects.isNull(getPlayerTeam(player)) || !isTeamLeader(player))
            return;

        val team = getPlayerTeam(player);
        teams.remove(team);
        Bukkit.getPluginManager().callEvent(new TeamDisbandEvent(player, team.getMembers())); // Call event
        player.sendMessage(MessageUtils.getMessageConfig(player).getTeam().getDisband());
    }

    // Invitation Part

    /***
     * Envoyer une invitation à un joueur
     * @param player - Joueur
     * @param target - Joueur
     */
    public void sendInvitation(@NonNull Player player, @NonNull Player target) {
        val messages = MessageUtils.getMessageConfig(player);
        // Check si le sender et la target ne sont pas les mêmes joueurs
        if (!player.equals(target)) {
            // Check si l'invitation à deja été créé
            invitations.add(new Invitation(player, target));
            player.sendMessage(messages.getInvitation().getSend().replace("%target%", target.getName()));
            //TODO change fancy message systems
            //new FancyMessage(messages.getInvitation().getReceiveTarget().replace("%sender%", player.getName())).then(messages.getInvitation().getReceiveAcceptance()).command("/group invite " + player.getName() + " accept").then(" ou ").then(messages.getInvitation().getReceiveDenyance()).command("/group invite " + player.getName() + " deny").send(target);
        } else {
            player.sendMessage(messages.getInvitation().getNotInviteYourself());
        }
    }

    /***
     * Accepter l'invitation d'un joueur
     * @param receiver - Joueur qui reçoit l'invitation
     * @param sender - Joueur qui envois l'invitation
     */
    public void acceptInvitation(@NonNull Player receiver, @NonNull Player sender) {
        val invitation = getInvitation(sender, receiver);

        if(Objects.isNull(invitation))
            return;

        if (!hasTeam(sender)) {
            registerTeam(sender);
        }
        addPlayerToTeam(sender, receiver);
        invitations.remove(invitation); // Clean l'invitation accepter
    }

    /***
     * Refuser l'invitation d'un joueur
     * @param receiver - Joueur qui reçoit l'invitation
     * @param sender - Joueur qui envoi l'invitation
     */
    public void DenyInvitation(@NonNull Player receiver, @NonNull Player sender) {
        val messagesReceiver = MessageUtils.getMessageConfig(receiver);
        val messagesSender = MessageUtils.getMessageConfig(sender);
        Invitation invitation = getInvitation(sender, receiver);
        if (invitation != null) {
            if (invitation.getTarget() != null)
                receiver.sendMessage(messagesReceiver.getInvitation().getDenyTarget().replace("%sender%", sender.getName()));
            sender.sendMessage(messagesSender.getInvitation().getDenySender().replace("%target%", receiver.getName()));
            invitations.remove(invitation); // Clean l'invitation accepter
        } else
            receiver.sendMessage(messagesReceiver.getInvitation().getNotAvaliable().replace("%sender%", sender.getName()));
    }

    /***
     * Retourne l'invitation envoyer à un joueur
     * @param sender - Joueur
     * @param receiver - Joueur
     */
    public Invitation getInvitation(@NonNull Player sender, @NonNull Player receiver) {
        return invitations.stream()
                .filter(invitation -> invitation.getSender() == sender && invitation.getTarget() == receiver)
                .findFirst()
                .get();
    }

    // Plot part

    /***
     * Ajouté tous les membres du groupe aux plots du leader
     * @param team - Object Team
     */
    public void addAllMembersToPlot(@NonNull Team team, @NonNull Plot plot) {
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
    public void addMemberToAllPlot(@NonNull Player player) {
        val team = getPlayerTeam(player);
        new PlotAPI().getPlayerPlots(Objects.requireNonNull(plotAPI.wrapPlayer(team.getLeader().getUniqueId())))
                .forEach(plot -> plot.addTrusted(player.getUniqueId()));
    }

    /***
     * Retiré un joueur de tous les plots de la team
     * @param player - Joueur à retirer
     */
    public void removeMemberFromAllPlot(@NonNull Player player) {
        val team = getPlayerTeam(player);
        new PlotAPI().getPlayerPlots(Objects.requireNonNull(plotAPI.wrapPlayer(team.getLeader().getUniqueId())))
                .forEach(plot -> plot.removeTrusted(player.getUniqueId()));
    }

}
