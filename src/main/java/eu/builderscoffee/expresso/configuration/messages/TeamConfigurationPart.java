package eu.builderscoffee.expresso.configuration.messages;

import lombok.Data;

@Data
public class TeamConfigurationPart {

    String infoHeader = "§7§m-------§6 Info de l''équipe §7§m-------§6";
    String infoLeader = "§6Leader:§f ";
    String infoMembers = "§aMembres :§f ";
    String playerJoin = "§7[§6Team§7] §a%target% §fa rejoint votre équipe";
    String playerQuit = "§7[§6Team§7] §c%target% §fa quitter votre équipe";
    String targetJoin = "§7[§6Team§7]§f Vous avez rejoins l''équipe de %target% !";
    String targetAlreadyInTeam = "[Equipe] Le joueur %target% est deja dans l'équipe";
    String alreadyInTeam = "§cVous êtes deja dans une équipe";
    String limitReached = "§7[§6Team§7]§f §cVous avez atteint la limite de membres dans votre équipe( %limit% )";
    String noTeam = "§7[§6Team§7]§a Vous n'avez pas d'équipe de créer !";
    String infoNoTeam = "§cVous n''avez aucune team !";
    String createTeam = "§7[§6Team§7]§a §aVous venez de créer votre équipe !";
    String alreadyCreated = "§7[§6Team§7]§c Vous avez deja crée une équipe";
    String leaderCannotLeave = "§7[§6Team§7]§c Vous devez supprimer votre groupe et non le quitter";
    String disband = "§7[§6Team§7]§c Vous avez supprimé votre groupe";
    String disbandPlayers = "§7[§6Team§7]§c Votre groupe est supprimé";
}
