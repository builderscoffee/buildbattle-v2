package eu.builderscoffee.expresso.configuration.messages;

import lombok.Data;

@Data
public class CommandConfigurationPart {

    /* Global Style */
    String footerText = "§0§7§m------";

    /* Global Command Configuration */
    String mustBePlayer = "%prefix% §cVous devez être un joueur pour executer cette commande";
    String badSyntaxe = "%prefix% §cMauvaise syntaxe de la commande";

    /* Plot Command Configuration */
    String plotDefault = "§a/eplot §b: Aide du plugin Expresso";
    String plotInfo = "§a/eplot info§b: Voir les information du plot";
    String plotInfoHeader = "§0§7§m--- §fPlot §0§7§m---";
    String plotInfoId = "§aId: §7 %id%";
    String plotInfoOwner = "§aOwner : §7 %owner%";
    String plotInfoMembers = "§aMembers : §7 %members";
    String plotNotClaim = "%prefix% §cCe plot n'est pas claim";
    String plotNotIn = "%prefix% §cTu n'est pas sur un plot, espèce de café moulu";
    String plotAlReadyNoted = "§cTu as déjà noté ce plot, espèce de café moulu";
    String plotNotNoted = "§cCe plot n''a aucune notation";
    String plotNoteSize = "Ce plot a %size% notation";

    /* Group Command Configuration */
    String groupDefaults = "§a/group §b: Aide du système de group";
    String groupAdd = "§a/group add <joueur> §b: Ajouter un joueur dans votre group";
    String groupRemove = "§a/group remove <joueur> §b: Retirer un joueur de votre group";
    String groupLeave = "§a/group leave §b: Quitter le groupe votre groupe ( membre uniquement";
    String groupDisband = "§a/group disband §b: Supprimer votre groupe ( leader uniquement";
    String groupInvite = "§a/group invite <player> accept/deny §b: Accepter ou refuser l'invite d'un joueur";
    String groupInfo = "§a/group info <player> §b: Voir les informations d'un groupe";

}
