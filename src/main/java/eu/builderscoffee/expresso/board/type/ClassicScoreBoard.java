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

public class ClassicScoreBoard extends BaseBoard {


    public ClassicScoreBoard() {
        /* Add board for Launching phase */
        this.getBoards().put(LaunchingPhase.class, player -> {
            val messages = MessageUtils.getMessageConfig(player);
            val list = new ArrayList<String>();
            list.add(this.addSeparator());
            list.add("§aSaison : " + messages.getBoard().getSeasonName());
            if (("§aClassic : §f" + ExpressoBukkit.getBuildBattle().getType().getName()).length() < 30) {
                list.add("§aClassic : §f" + ExpressoBukkit.getBuildBattle().getType().getName());
            } else {
                list.add("§aClassic : §f");
                list.add(ExpressoBukkit.getBuildBattle().getType().getName());
            }
            list.add("§aThème : " + "§f§kLait");
            list.add("§aTimer : §f" + TimeUtils.getDurationString(ExpressoBukkit.getBuildBattle().getType().getCurrentPhase().getTime() - ExpressoBukkit.getBuildBattle().getType().getCurrentPhase().getCurrentTime()));
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
            if (("§aClassic : §f" + ExpressoBukkit.getBuildBattle().getType().getName()).length() < 30) {
                list.add("§aClassic : §f" + ExpressoBukkit.getBuildBattle().getType().getName());
            } else {
                list.add("§aClassic : §f");
                list.add(ExpressoBukkit.getBuildBattle().getType().getName());
            }
            if (("§aThème : " + ExpressoBukkit.getBuildBattle().getGameManager().getTheme()).length() < 30)
                list.add("§aThème : " + ExpressoBukkit.getBuildBattle().getGameManager().getTheme());
            else {
                list.add("§aThème : ");
                list.add(ExpressoBukkit.getBuildBattle().getGameManager().getTheme());
            }
            list.add("§aTimer : §f" + TimeUtils.getDurationString(ExpressoBukkit.getBuildBattle().getType().getCurrentPhase().getTime() - ExpressoBukkit.getBuildBattle().getType().getCurrentPhase().getCurrentTime()));
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
            if (("§aClassic : §f" + ExpressoBukkit.getBuildBattle().getType().getName()).length() < 30) {
                list.add("§aClassic : §f" + ExpressoBukkit.getBuildBattle().getType().getName());
            } else {
                list.add("§aClassic : §f");
                list.add(ExpressoBukkit.getBuildBattle().getType().getName());
            }
            if (("§aThème : " + ExpressoBukkit.getBuildBattle().getGameManager().getTheme()).length() < 30)
                list.add("§aThème : " + ExpressoBukkit.getBuildBattle().getGameManager().getTheme());
            else {
                list.add("§aThème : ");
                list.add(ExpressoBukkit.getBuildBattle().getGameManager().getTheme());
            }
            list.add("§cPartie finie");
            list.add(this.addBlank());
            list.add(this.addIp());
            list.add(this.addSeparator());
            return list;
        });
    }
}