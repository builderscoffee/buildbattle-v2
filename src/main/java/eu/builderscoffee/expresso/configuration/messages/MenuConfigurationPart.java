package eu.builderscoffee.expresso.configuration.messages;

import lombok.Data;

@Data
public class MenuConfigurationPart {


    /* Jury Base Inventory */
    String baseMenuName = "§fMenu Jury";
    String plotItem = "§aPlot §f# %plot%";
    String previousItem = "§6Précédente";
    String nextItem = "§6Suivant";
    String closeItem = "§cFermer";
    String pageItem = "§aPage §f%page%";

    /* Jury Notation Inventory */
    String notationMenuName = "Menu Notation";
    // Items category
    String beautyCategory = "§bBeauté/Technicité";
    String creativeCategory = "§bCréativité/Originalité";
    String amenagementCategory = "§bAménagement/Finalité";
    String folkloreCategory = "§bFolklore %plot%";
    String funCategory = "§bFun";
    // Others Items
    String pointsItem = "§6 %points% Points";
    String validedNotationItem = "§bValider mon verdict";

    /* Jury Teleportation Inventory */
    String teleportsMenu = "§fList des participants";
}
