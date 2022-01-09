package eu.builderscoffee.expresso.buildbattle;

import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.expresso.board.BaseBoard;
import eu.builderscoffee.expresso.board.type.ClassicScoreBoard;
import eu.builderscoffee.expresso.board.type.ExpressoScoreboard;
import eu.builderscoffee.expresso.buildbattle.games.classic.ClassicGameType;
import eu.builderscoffee.expresso.buildbattle.games.expressos.ExpressoGameType;
import eu.builderscoffee.expresso.buildbattle.games.tournament.TournamentGameType;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum BuildBattleInstanceType {

    EXPRESSO(ExpressoGameType.class, "Expresso", new ItemBuilder(Material.LIGHT_BLUE_DYE).setName("§cExpresso").addLoreLine("§7Mélange savoureux d'arôme").build(), new ExpressoScoreboard()),
    CLASSIC(ClassicGameType.class, "Classic", new ItemBuilder(Material.CRAFTING_TABLE).setName("§cClassic").addLoreLine("§7Décaféiné").build(), new ClassicScoreBoard()),
    TOURNAMENT(TournamentGameType.class, "Tournois", new ItemBuilder(Material.BLACK_BANNER).setName("§cTournois").addLoreLine("§7Mélange maison").build(), new ExpressoScoreboard()),
    NONE(null, "Rien", null, new BaseBoard());

    @Getter
    private final Class<? extends BuildBattleGameType> buildbattleGameTypeClass;
    @Getter
    private final String buildBattleGameTypeName;
    @Getter
    private final ItemStack icon;
    @Getter
    private final BaseBoard baseBoard;

    BuildBattleInstanceType(Class<? extends BuildBattleGameType> buildbattleGameTypeClass, String buildBattleGameTypeName, ItemStack icon, BaseBoard baseBoard) {
        this.buildbattleGameTypeClass = buildbattleGameTypeClass;
        this.buildBattleGameTypeName = buildBattleGameTypeName;
        this.icon = icon;
        this.baseBoard = baseBoard;
    }
}
