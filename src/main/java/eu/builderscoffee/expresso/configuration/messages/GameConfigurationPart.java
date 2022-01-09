package eu.builderscoffee.expresso.configuration.messages;

import lombok.Data;

@Data
public class GameConfigurationPart {

    /* Competitor Game configuration */
    String competitorJoin = "§f%player% §aa rejoins la compétition";
    String competitorLeave = "§f%player% §ca quitté la compétition";

    /* Waiting Game Configuration */
    String plotAutoSpam = "§cVous devez attendre le lancement de la partie avant de créer votre plot";

    /* InGame Game Configuration */
    String plotAuto = "%prefix% §a/plot auto pour participer";
    String themesTitle = "Thème";
    String startInTitle = "§eDébut dans";
    String startInSubTitle = "§6%time%  §esecondes";
    String competitionBeginningIn = "%prefix% §eLa compétition commence dans %time%";
    String competitionStarting = "%prefix% §eLa compétition commence ! Bonne chance !";
    String remainingGames = "%prefix% §a %time% §fde jeux restantes !";
    String remainingTime = "§aTemps restant";
}
