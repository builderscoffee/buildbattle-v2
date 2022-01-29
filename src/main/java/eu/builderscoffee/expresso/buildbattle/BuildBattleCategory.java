package eu.builderscoffee.expresso.buildbattle;

import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.expresso.board.BaseBoard;
import eu.builderscoffee.expresso.board.type.ClassicScoreBoard;
import eu.builderscoffee.expresso.board.type.ExpressoScoreboard;
import eu.builderscoffee.expresso.buildbattle.games.classic.ClassicType;
import eu.builderscoffee.expresso.buildbattle.games.expressos.ExpressoType;
import eu.builderscoffee.expresso.buildbattle.games.tournament.TournamentType;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum BuildBattleCategory {
    NONE(null, "Rien", null, new BaseBoard()),
    EXPRESSO(ExpressoType.class, "Expresso", new ItemBuilder(Material.LIGHT_BLUE_DYE).setName("§cExpresso").addLoreLine("§7Mélange savoureux d'arôme").build(), new ExpressoScoreboard()),
    CLASSIC(ClassicType.class, "Classic", new ItemBuilder(Material.CRAFTING_TABLE).setName("§cClassic").addLoreLine("§7Décaféiné").build(), new ClassicScoreBoard()),
    TOURNAMENT(TournamentType.class, "Tournois", new ItemBuilder(Material.BLACK_BANNER).setName("§cTournois").addLoreLine("§7Mélange maison").build(), new ExpressoScoreboard());

    @Getter
    private final Class<? extends BuildBattleType> buildbattleTypeClass;
    @Getter
    private final String buildBattleGameTypeName;
    @Getter
    private final ItemStack icon;
    @Getter
    private final BaseBoard baseBoard;

    BuildBattleCategory(Class<? extends BuildBattleType> buildbattleTypeClass, @NonNull String buildBattleGameTypeName, ItemStack icon, @NonNull BaseBoard baseBoard) {
        this.buildbattleTypeClass = buildbattleTypeClass;
        this.buildBattleGameTypeName = buildBattleGameTypeName;
        this.icon = icon;
        this.baseBoard = baseBoard;
    }
}
