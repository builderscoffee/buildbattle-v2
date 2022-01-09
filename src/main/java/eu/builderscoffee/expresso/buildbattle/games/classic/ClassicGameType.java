package eu.builderscoffee.expresso.buildbattle.games.classic;

import eu.builderscoffee.expresso.buildbattle.BuildBattleGameType;
import eu.builderscoffee.expresso.buildbattle.phase.bases.EndPhase;
import eu.builderscoffee.expresso.buildbattle.phase.bases.GamePhase;
import eu.builderscoffee.expresso.buildbattle.phase.bases.LaunchingPhase;

import static eu.builderscoffee.expresso.utils.TimeUtils.HOUR;

public class ClassicGameType extends BuildBattleGameType {

    public ClassicGameType() {
        super("BuildBattle Classic");
        this.phases.add(new LaunchingPhase(30));
        this.phases.add(new GamePhase(2 * HOUR));
        this.phases.add(new EndPhase());
    }
}