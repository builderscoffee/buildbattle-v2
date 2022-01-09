package eu.builderscoffee.expresso.configuration.messages;

import lombok.Data;

@Data
public class InvitationConfigurationPart {

    String receiveTarget = "§7[§6Team§7]§a Vous avez reçu une invitation de §f%sender%";
    String receiveAcceptance = "§aAccepter";
    String receiveDenyance = "§cRefuser";
    String send = "§7[§6Team§7]§a Vous avez envoyé une invitation à §f%target%";
    String denyTarget = "§7[§6Team§7] §aVous avez refusé l'invitation de §c%sender%";
    String denySender = "§7[§6Team§7]§f %target% §ca refusé votre invitation";
    String notAvaliable = "§7[§6Team§7]§c L'invitation de §f%sender% §cn'est plus valable !";
    String notInviteYourself = "§7[§6Team§7]§c Vous ne pouvez pas vous invitez vous même !";
}
