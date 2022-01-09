package eu.builderscoffee.expresso.board.type;

import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.board.BaseBoard;
import eu.builderscoffee.expresso.buildbattle.phase.bases.EndPhase;
import eu.builderscoffee.expresso.buildbattle.phase.bases.GamePhase;
import eu.builderscoffee.expresso.buildbattle.phase.bases.LaunchingPhase;
import eu.builderscoffee.expresso.utils.MessageUtils;
import eu.builderscoffee.expresso.utils.TimeUtils;
import lombok.val;

import java.util.ArrayList;

public class TournamentScoreboard extends BaseBoard {

    public TournamentScoreboard() {
        /* Add board for Launching phase */
        this.getBoards().put(LaunchingPhase.class, player -> {
            val messages = MessageUtils.getMessageConfig(player);
            val list = new ArrayList<String>();
            list.add(this.addSeparator());
            list.add("§aSaison : " + messages.getBoard().getSeasonName());
            list.add("§aTournois : §f" + ExpressoBukkit.getBbGame().getBuildBattleGameType().getName());
            list.add("§aThème : " + "§f§kLait");
            list.add("§aTimer : §f" + TimeUtils.getDurationString(ExpressoBukkit.getBbGame().getBuildBattleGameType().getCurrentPhase().getTime() - ExpressoBukkit.getBbGame().getBuildBattleGameType().getCurrentPhase().getCurrentTime()));
            list.add(this.addBlank());
            list.add(this.addIp());
            list.add(this.addSeparator());
            return list;
        });

        /* Add board for Game phase */
        this.getBoards().put(GamePhase.class, player -> {
            val messages = MessageUtils.getMessageConfig(player);
            val list = new ArrayList<String>();
            list.add(this.addSeparator());
            list.add("§aSaison : " + messages.getBoard().getSeasonName());
            list.add("§aTournois : §f" + ExpressoBukkit.getBbGame().getBuildBattleGameType().getName());
            if (("§aThème : " + ExpressoBukkit.getBbGame().getBbGameManager().getTheme()).length() < 30)
                list.add("§aThème : " + ExpressoBukkit.getBbGame().getBbGameManager().getTheme());
            else {
                list.add("§aThème : ");
                list.add(ExpressoBukkit.getBbGame().getBbGameManager().getTheme());
            }
            list.add("§aTimer : §f" + TimeUtils.getDurationString(ExpressoBukkit.getBbGame().getBuildBattleGameType().getCurrentPhase().getTime() - ExpressoBukkit.getBbGame().getBuildBattleGameType().getCurrentPhase().getCurrentTime()));
            list.add(this.addBlank());
            list.add(this.addIp());
            list.add(this.addSeparator());
            return list;
        });

        /* Add board for End phase */
        this.getBoards().put(EndPhase.class, player -> {
            val messages = MessageUtils.getMessageConfig(player);
            val list = new ArrayList<String>();
            list.add(this.addSeparator());
            list.add("§aSaison : " + messages.getBoard().getSeasonName());
            list.add("§aTournois : §f" + ExpressoBukkit.getBbGame().getBuildBattleGameType().getName());
            if (("§aThème : " + ExpressoBukkit.getBbGame().getBbGameManager().getTheme()).length() < 30)
                list.add("§aThème : " + ExpressoBukkit.getBbGame().getBbGameManager().getTheme());
            else {
                list.add("§aThème : ");
                list.add(ExpressoBukkit.getBbGame().getBbGameManager().getTheme());
            }
            list.add("§cPartie finie");
            list.add(this.addBlank());
            list.add(this.addIp());
            list.add(this.addSeparator());
            return list;
        });
    }
}
