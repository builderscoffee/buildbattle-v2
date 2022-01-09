package eu.builderscoffee.expresso.listeners.redisson;

import eu.builderscoffee.api.common.events.EventListener;
import eu.builderscoffee.api.common.events.ProcessEvent;
import eu.builderscoffee.api.common.events.events.HeartBeatEvent;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.BuildBattleInstanceType;
import eu.builderscoffee.expresso.utils.TimeUtils;

import java.util.Objects;

public class HeartBeatListener implements EventListener {


    /***
     * Envois une mise à jours des informations du serveur
     * - GameType & SubType
     * - State & Timer
     * @param event
     */
    @ProcessEvent
    public void onHeartBeat(HeartBeatEvent event) {
        if (Objects.nonNull(ExpressoBukkit.getBbGame())) {
            // Type de la partie ( Expresso, Buildbattle etc... )
            if (!ExpressoBukkit.getBbGame().getBbGameTypes().equals(BuildBattleInstanceType.NONE)) {
                event.getServer().getProperties().put("GameType", ExpressoBukkit.getBbGame().getBbGameTypes().getBuildBattleGameTypeName());
            }
            // Type de sous partie ( Allongé, IlClassico etc... )
            if (Objects.nonNull(ExpressoBukkit.getBbGame().getBuildBattleGameType())) {
                event.getServer().getProperties().put("GameSubType", ExpressoBukkit.getBbGame().getBuildBattleGameType().getName());
            }
            // Etat de la partie ( In_Game , End etc... )
            if (ExpressoBukkit.getBbGame().isReady()) {
                event.getServer().getProperties().put("State", ExpressoBukkit.getBbGame().getGameState().toString());
            }
            // Timer de la partie
            if (Objects.nonNull(ExpressoBukkit.getBbGame().getBuildBattleGameType()) && Objects.nonNull(ExpressoBukkit.getBbGame().getBuildBattleGameType().getCurrentPhase())) {
                event.getServer().getProperties().put("Timer", TimeUtils.getDurationString(ExpressoBukkit.getBbGame().getBuildBattleGameType().getCurrentPhase().getTime()));
            }
        }
    }
}
